package edu.washington.cs.knowitall.nlp.dependency_parse_tree;


import java.util.List;

/**
 * Represents a dependency parse tree.
 */
public class DependencyParseTree {

    private Node rootElement;

    public DependencyParseTree() {}

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

    /**
     * @return a list of all leaf nodes of the tree
     */
    public List<Node> getLeafNodes() {
        return rootElement.getLeafNodes();
    }

    /**
     * Finds all nodes, which match the given pattern.
     * @param pattern the pattern
     * @return list of nodes
     */
    public List<Node> find(String pattern) {
        return rootElement.find(pattern);
    }

    /**
     * Find the nodes with the given ids.
     * @param ids the list of ids
     * @return the list of nodes
     */
    public List<Node> find(Iterable<Integer> ids) {
        return rootElement.find(ids);
    }

    /**
     * Find the node with the given id.
     * @param id the id
     * @return the node
     */
    public Node find(int id) {
        return rootElement.find(id);
    }

    /**
     * Prints the tree.
     * @return the tree as string
     */
    public String printTree() {
        StringBuilder sb = new StringBuilder();
        rootElement.printTree(sb);
        return sb.toString().trim();
    }

}
