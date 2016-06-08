package edu.washington.cs.knowitall.nlp.dependency_parse_tree;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a dependency parse tree.
 */
public class DependencyParseTree {

    private Node tree;
    private String sentence;
    private String conllFormat;

    public DependencyParseTree() {}

    public DependencyParseTree(Node tree) {
        this.tree = tree;
    }


    /**
     * Returns the tree as a list of node objects. The elements of the
     * list are generated from a pre-order traversal of the tree.
     * @return a list of nodes
     */
    public List<Node> toList() {
        return tree.toList();
    }

    /**
     * Returns a string representation of the tree. The elements are generated
     * from a pre-order traversal of the tree.
     * @return the string representation of the tree.
     */
    public String toString() {
        return tree.toString();
    }

    /**
     * Finds the nodes to the given ids.
     * @param ids a list of ids
     * @return a list of nodes
     */
    public List<Node> find(Iterable<Integer> ids) {
        return tree.find(ids);
    }

    /**
     * Get the top level nodes of the tree.
     * There can be multiple words in the top level, if the dependency parser has split the
     * sentence.
     * @return a list of root nodes
     */
    public List<Node> getRootElements() {
        List<Node> subRoots = tree.find("s", "neb", "objc");
        List<Node> directRoots = this.tree.getChildren().stream()
            .filter(x -> !x.getPosGroup().equals("$.") && !x.getPosGroup().equals("$,") && !x.getPosGroup().equals("$("))
            .collect(Collectors.toList());

        subRoots.addAll(directRoots);

        return subRoots;
    }

    /**
     * Prunes nodes from this tree, which are not relevant.
     * Such nodes are:
     * rel, vok, par, empty labels, expl, kom
     */
    public void prune() {
        this.getTree().prune();
    }

    /**
     * GETTER AND SETTER
     */

    public Node getTree() {
        return this.tree;
    }

    public void setTree(Node tree) {
        this.tree = tree;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getConllFormat() {
        return conllFormat;
    }

    public void setConllFormat(String conllFormat) {
        this.conllFormat = conllFormat;
    }

}
