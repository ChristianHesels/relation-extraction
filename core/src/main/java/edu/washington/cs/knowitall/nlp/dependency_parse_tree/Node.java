package edu.washington.cs.knowitall.nlp.dependency_parse_tree;


import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node of the dependency parse tree.
 */
public abstract class Node {

    public Node parent;
    public List<Node> children;

    public Node() {}

    public Node(String data) {
        parse(data);
    }

    /**
     * @return the children of this node
     */
    public List<Node> getChildren() {
        if (this.children == null) {
            return new ArrayList<>();
        }
        return this.children;
    }

    /**
     * @param children the list of child nodes to set.
     */
    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /**
     * Adds a child node.
     * @param node the child node to add.
     */
    public void addChild(Node node) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(node);
    }

    /**
     * @return parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @param parent the parent node to set
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }


    /**
     * Returns the sub-tree as a list of node objects. The elements of the
     * list are generated from a pre-order traversal of the tree.
     * @return a list of nodes
     */
    public List<Node> toList() {
        List<Node> list = new ArrayList<>();
        walk(this, list);
        return list;
    }

    /**
     * Returns a string representation of the tree. The elements are generated
     * from a pre-order traversal of the tree.
     * @return the string representation of the tree.
     */
    public String toString() {
        List<String> strings = new ArrayList<>();
        List<Node> nodes = toList();
        for (Node n : nodes) {
            if (n.isLeafNode()) {
                strings.add(((LeafNode) n).word);
            }
        }
        return Joiner.on(" ").join(strings);
    }

    /**
     * Remove this node from the tree.
     */
    public void remove() {
        parent.children.remove(this);
    }

    /**
     * Remove all children of this node.
     */
    public void removeChildren() {
        this.children = new ArrayList<>();
    }

    /**
     * @return a list of all leaf nodes in pre-order
     */
    public List<Node> getLeafNodes() {
        List<Node> list = new ArrayList<>();
        walkChildren(this, list);
        return list;
    }

    /**
     * @param data the data string to parse
     */
    protected abstract void parse(String data);

    public abstract boolean isInnerNode();

    public abstract boolean isLeafNode();

    /**
     * @param labels a list of labels
     * @return true, if the current node has a label of the given list, false otherwise
     */
    public abstract boolean matchLabel(List<String> labels);
    /**
     * @param features a list of features
     * @return true, if the current node has a feature of the given list, false otherwise
     */
    public abstract boolean matchFeature(List<String> features);
    /**
     * @param posTags a list of labels
     * @return true, if the current node has a pos tag of the given list, false otherwise
     */
    public abstract boolean matchPosTag(List<String> posTags);

    /**
     * Finds all nodes of the tree, which match the given pattern.
     * @param pattern the pattern
     * @return a list of nodes
     */
    public List<Node> find(String pattern) {
        String[] parts = pattern.split(" ");

        List<String> posTags = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> features = new ArrayList<>();

        for (String p : parts) {
            if (p.endsWith("_pos")) {
                posTags.add(p.substring(0, p.length() - 4));
            } else if (p.endsWith("_lab")) {
                labels.add(p.substring(0, p.length() - 4));
            } else if (p.endsWith("_fea")) {
                features.add(p.substring(0, p.length() - 4));
            }
        }

        List<Node> nodes = new ArrayList<>();
        walkMatchPattern(this, nodes, labels, features, posTags);
        return nodes;
    }

    /**
     * Walks the tree in pre-order style.
     * @param element the starting element.
     * @param list the output of the walk.
     */
    private void walk(Node element, List<Node> list) {
        list.add(element);
        for (Node data : element.getChildren()) {
            walk(data, list);
        }
    }

    /**
     * Walks the tree in pre-order style and collects all leaf nodes.
     * @param element the starting element.
     * @param list the output of the walk.
     */
    private void walkChildren(Node element, List<Node> list) {
        if (element.isLeafNode()) {
            list.add(element);
        }
        for (Node data : element.getChildren()) {
            walk(data, list);
        }
    }

    /**
     * Walks the tree in pre-order style and collects all nodes, which match one of the given
     * patterns.
     * @param element   the starting element
     * @param list      the output of the walk
     * @param labels    a list of labels
     * @param features  a list of features
     * @param posTags   a list of pos tags
     */
    private void walkMatchPattern(Node element, List<Node> list, List<String> labels, List<String> features, List<String> posTags) {
        if (element.matchFeature(features) || element.matchLabel(labels) || element.matchPosTag(posTags)) {
            list.add(element);
        }
        for (Node data : element.getChildren()) {
            walkMatchPattern(data, list, labels, features, posTags);
        }
    }
}
