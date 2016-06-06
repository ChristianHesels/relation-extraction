package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Dependency Tree Parser.
 */
public class ParZuSentenceParser {

    private static final String PARZU_HOME = "/opt/ParZu/"; //System.getenv("PARZU_HOME");
    private static final String PARSE_COMMAND = "python " + PARZU_HOME + "parzu.py";

    /**
     * Parses the sentence and converts it into dependency parse trees.
     * There can be multiple parse trees, if the parser decides to split the sentence.
     * @param sent the sentence
     * @return a list of dependency parse trees
     */
    public List<DependencyParseTree> parseSentence(String sent) {
        try {
            List<String> dependencyParseTreeStr = parse(sent);
            List<DependencyParseTree> trees = convert(dependencyParseTreeStr);
            for (DependencyParseTree tree : trees) {
                tree.setSentence(sent);
                tree.setConllFormat(Joiner.on("\n").join(dependencyParseTreeStr));
            }
            return trees;
        } catch (Exception e) {
            System.out.println("BitPar: Could not process sentence '" + sent + "'");
            return new ArrayList<>();
        }
    }


    /**
     * Get the dependency parse tree string (ConLL format) using ParZu.
     *
     * @param str the string
     * @return a dependency parse tree as string
     * @throws IOException  if the ParZu command could not be executed or if the result
     *                      could not be read
     * @throws InterruptedException if the process, which executes ParZu, got interrupted.
     */
    public List<String> parse(String str) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(new String[]{
            "/bin/sh", "-c", "echo \"" + str + "\" | " + PARSE_COMMAND});

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        p.waitFor();

        List<String> output = new ArrayList<>();
        String line;
        while ((line = stdInput.readLine()) != null) {
            output.add(line);
        }

        stdInput.close();
        p.destroy();

        return output;
    }


    /**
     * Given the output of the ParZu, convert it into a dependency parse tree.
     *
     * @param content the output of the ParZu
     * @return a dependency parse tree
     */
    public List<DependencyParseTree> convert(List<String> content) {
        List<DependencyParseTree> trees = new ArrayList<>();

        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0));

        for (String line: content) {
            if (line.isEmpty()) {
                // sentence is splitted
                trees.add(toTree(nodes));

                nodes = new ArrayList<>();
                nodes.add(new Node(0));
            } else {
                Node node = new Node(line);
                nodes.add(node);
            }
        }

        if (nodes.size() > 1) trees.add(toTree(nodes));

        return trees;
    }

    /**
     * Given a list of nodes, create a tree.
     * @param nodes the nodes
     * @return a tree
     */
    private DependencyParseTree toTree(List<Node> nodes) {
        DependencyParseTree tree = null;
        for (Node n : nodes) {
            // create tree, if we find root node
            if (n.getParentId() == -1) {
                tree = new DependencyParseTree(n);
                continue;
            };

            // find parent node
            Optional<Node> optionalParentNode = nodes.stream()
                .filter(x -> x.getId() == n.getParentId())
                .findFirst();
            Node parentNode = null;
            if (optionalParentNode.isPresent()) {
                parentNode = optionalParentNode.get();

                // set parent node and child node of parent
                n.setParent(parentNode);
                parentNode.addChild(n);
            }
        }
        return tree;
    }
}
