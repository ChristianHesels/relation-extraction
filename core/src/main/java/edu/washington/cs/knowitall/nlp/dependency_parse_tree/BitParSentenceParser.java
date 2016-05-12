package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BitParSentenceParser {

    private static final String
        GERMANPARSER_HOME = System.getenv("GERMANPARSER_HOME");
    private static final String PARSE_COMMAND = "sh " + GERMANPARSER_HOME + "parse.sh";

    /**
     * Parses the sentence and converts it into dependency parse trees.
     * @param sent the sentence
     * @return a list of dependency parse trees
     */
    public List<DependencyParseTree> parseSentence(String sent) {
        try {
            sent = clean(sent);
            List<String> dependencyParseTreeStr = parse(sent);
            return convert(dependencyParseTreeStr);
        } catch (Exception e) {
            System.out.println("BitPar: Could not process sentence '" + sent + "'");
            return new ArrayList<>();
        }
    }


    /**
     * Get the dependency parse tree string using BitPar.
     *
     * @param str the string
     * @return a dependency parse tree as string
     * @throws java.io.IOException  if the BitPar command could not be executed or if the result
     *                              could not be read
     * @throws InterruptedException if the process, which executes BitPar, got interrupted.
     */
    private List<String> parse(String str) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",
                                                           "echo \"" + str + "\" | " + PARSE_COMMAND}
        );

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        p.waitFor();

        List<String> output = new ArrayList<>();
        String line;
        // skip the first line
        stdInput.readLine();
        while ((line = stdInput.readLine()) != null) {
            output.add(line);
        }

        stdInput.close();
        p.destroy();

        return output;
    }


    /**
     * Given the output of the BitPar, convert it into a dependency parse tree.
     *
     * @param content the output of the BitPar
     * @return a dependency parse tree
     */
    public List<DependencyParseTree> convert(List<String> content) {
        int nodeId = 0;
        Node rootElement = new InnerNode("TOP", nodeId);
        DependencyParseTree tree = new DependencyParseTree(rootElement);

        List<DependencyParseTree> trees = new ArrayList<>();

        int lastCount = 0;
        Node lastNode = rootElement;
        for(int i = 1; i < content.size(); i++) {
            String line = content.get(i);

            if (line.startsWith("No parse for:")) {
                continue;
            }

            if (line.isEmpty()) {
                trees.add(tree);
                nodeId = 0;
                rootElement = new InnerNode("TOP", nodeId);
                tree = new DependencyParseTree(rootElement);
                lastCount = 0;
                lastNode = rootElement;
                i = i + 1;
                continue;
            }

            int currentCount = countLeadingWhitespace(line);
            line = line.trim();

            Node node;
            nodeId += 1;
            if (line.endsWith(")")) {
                node = new LeafNode(line, nodeId);
            } else {
                node = new InnerNode(line, nodeId);
            }

            if (currentCount == lastCount) {
                node.parent = lastNode.parent;
                lastNode.parent.addChild(node);
            }

            else if (currentCount > lastCount) {
                node.parent = lastNode;
                lastNode.addChild(node);
            }

            else {
                int diff = ((lastCount - currentCount) / 2) + 1;
                for (int j = 0; j < diff; j++) {
                    lastNode = lastNode.parent;
                }
                node.parent = lastNode;
                lastNode.addChild(node);
            }

            lastNode = node;
            lastCount = currentCount;
        }

        trees.add(tree);

        return trees;
    }

    /**
     * Counts the whitespace at the beginning of a line.
     * @param line the line as string.
     * @return the number of leading whitespace
     */
    protected int countLeadingWhitespace(String line) {
        int count = 0;
        int i = 0;
        char[] chars = line.toCharArray();

        while(i < chars.length && chars[i] == ' ') {
            count++;
            i++;
        }

        return count;
    }

    /**
     * BitPar split a string on every '.', which occurs in the string, even if the '.' comes form
     * an abbreviation. To avoid this, we remove those '.'.
     * @param str the string
     * @return the cleaned string
     */
    private String clean(String str) {
        String pattern = ".*(\\w\\.).+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);

        while (m.find()) {
            str = str.replace(m.group(1), m.group(1).substring(0, 1));
            m = r.matcher(str);
        }

        return str;
    }

}
