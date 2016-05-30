package edu.washington.cs.knowitall.nlp.dependency_parse_tree;


import java.util.List;

/**
 * Represents a dependency parse tree.
 */
public class DependencyParseTree {

    private Node rootElement;
    private String sentence;
    private String conllFormat;

    public DependencyParseTree() {}

    public DependencyParseTree(Node rootElement) {
        this.rootElement = rootElement;
    }


    /**
     * Returns the tree as a list of node objects. The elements of the
     * list are generated from a pre-order traversal of the tree.
     * @return a list of nodes
     */
    public List<Node> toList() {
        return rootElement.toList();
    }

    /**
     * Returns a string representation of the tree. The elements are generated
     * from a pre-order traversal of the tree.
     * @return the string representation of the tree.
     */
    public String toString() {
        return rootElement.toString();
    }

    /**
     * Finds the nodes to the given ids.
     * @param ids a list of ids
     * @return a list of nodes
     */
    public List<Node> find(Iterable<Integer> ids) {
        return rootElement.find(ids);
    }

    /**
     * GETTER AND SETTER
     */

    public Node getRootElement() {
        return this.rootElement;
    }

    public void setRootElement(Node rootElement) {
        this.rootElement = rootElement;
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
