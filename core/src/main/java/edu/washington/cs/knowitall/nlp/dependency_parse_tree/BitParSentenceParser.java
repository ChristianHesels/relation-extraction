package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.NlpException;

public class BitParSentenceParser {

    // TODO: BitPar splits the sentence on words like 'bzw.' 'Dr.'

    private static final String
        GERMANPARSER_HOME = "/opt/GermanParser/"; // System.getenv("GERMANPARSER_HOME");
    private static final String PARSE_COMMAND = "sh " + GERMANPARSER_HOME + "parse.sh";

    public DependencyParseTree parseSentence(String sent) {
        try {
            List<String> dependencyParseTreeStr = parse(sent);
            return convert(dependencyParseTreeStr);
        } catch (Exception e) {
            throw new NlpException("Could not process sentence '" + sent + "'", e);
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
        // skip the first two lines
        stdInput.readLine();
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
    private DependencyParseTree convert(List<String> content) throws IOException {
        Node rootElement = new InnerNode("TOP");
        DependencyParseTree tree = new DependencyParseTree(rootElement);

        int lastCount = 0;
        Node lastNode = rootElement;
        for(String line : content) {
            int currentCount = countLeadingWhitespace(line);

            line = line.trim();
            Node node;
            if (line.endsWith(")")) {
                node = new LeafNode(line);
            } else {
                node = new InnerNode(line);
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
                for (int i = 0; i < diff; i++) {
                    lastNode = lastNode.parent;
                }
                node.parent = lastNode;
                lastNode.addChild(node);
            }

            lastNode = node;
            lastCount = currentCount;
        }

        return tree;
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

        while(chars[i] == ' ') {
            count++;
            i++;
        }

        return count;
    }

}
