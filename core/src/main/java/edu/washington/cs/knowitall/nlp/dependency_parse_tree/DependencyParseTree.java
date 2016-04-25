package edu.washington.cs.knowitall.nlp.dependency_parse_tree;


import java.util.List;

/**
 * Represents a dependency parse tree.
 */
public class DependencyParseTree {

    private Node rootElement;

    public DependencyParseTree(Node rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * @return the root element.
     */
    public Node getRootElement() {
        return this.rootElement;
    }

    /**
     * @param rootElement the root element to set.
     */
    public void setRootElement(Node rootElement) {
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


}
