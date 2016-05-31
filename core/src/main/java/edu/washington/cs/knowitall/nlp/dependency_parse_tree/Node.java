package edu.washington.cs.knowitall.nlp.dependency_parse_tree;


import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a node of the dependency parse tree.
 */
public class Node {

    private int id;
    private List<Node> children;
    private Node parent;
    private int parentId;
    private String word;
    private String lemma;
    private String posGroup;
    private String pos;
    private String morphology;
    private String labelToParent;

    public Node(int id) {
        this.id = id;
        this.children = null;
        this.parent = null;
        this.parentId = -1;
        this.word = "";
        this.lemma = "";
        this.posGroup = "";
        this.pos = "";
        this.morphology = "";
        this.labelToParent = "";
    }

    public Node(String data) {
        parse(data);
        this.children = null;
        this.parent = null;
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
     * Returns the sub-tree as a list of node objects. The elements of the
     * list are generated from a pre-order traversal of the tree.
     * @return a list of nodes
     */
    public List<Node> toList() {
        List<Node> list = new ArrayList<>();
        walk(this, list);
        list.sort((n1, n2) -> n1.id - n2.id);
        return list;
    }

    /**
     * Returns a string representation of the tree. The elements are generated
     * from a pre-order traversal of the tree.
     * @return the string representation of the tree.
     */
    @Override
    public String toString() {
        List<String> strings = new ArrayList<>();
        for (Node n : toList()) {
            strings.add(n.word);
        }
        return Joiner.on(" ").join(strings);
    }

    /**
     * Return all children, which are connected to this node via the given label.
     * @param label the label
     * @return list of nodes
     */
    public List<Node> getChildrenOfType(String label) {
        return this.getChildren().stream()
            .filter(x -> x.getLabelToParent().equals(label))
            .collect(Collectors.toList());
    }

    /**
     * Return all children, which are connected to this node via the given labels.
     * @param labels the labels
     * @return list of nodes
     */
    public List<Node> getChildrenOfType(String... labels) {
        List<String> l = new ArrayList<>(Arrays.asList(labels));
        return this.getChildren().stream()
            .filter(x -> l.contains(x.getLabelToParent()))
            .collect(Collectors.toList());
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
     * @param data the data string to parse
     */
    protected void parse(String data) {
        String[] parts = data.split("\t");

        // The conll format has 10 fields
        assert(parts.length == 10);

        this.id = Integer.parseInt(parts[0]);
        this.word = parts[1];
        this.lemma = parts[2];
        this.posGroup = parts[3];
        this.pos = parts[4];
        this.morphology = parts[5];
        this.labelToParent = parts[7];
        this.parentId = Integer.parseInt(parts[6]);
    };

    public boolean isInnerNode() {
        return !isLeafNode();
    };

    public boolean isLeafNode() {
        return this.children == null || this.children.size() == 0;
    };

    /**
     * @param labels a list of labels
     * @return true, if the current node has a label of the given list, false otherwise
     */
    public boolean matchLabel(List<String> labels) {
        return labels.contains(this.labelToParent);
    };

    /**
     * @param posTags a list of labels
     * @return true, if the current node has a pos tag of the given list, false otherwise
     */
    public boolean matchPosTag(List<String> posTags) {
        return posTags.contains(this.pos);};

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
     * Find nodes with the given ids.
     * @param ids the ids
     * @return a list of nodes
     */
    public List<Node> find(Iterable<Integer> ids) {
        return this.toList().stream()
            .filter((x -> Iterables.contains(ids, x.getId())))
            .collect(Collectors.toList());
    }

    /**
     * GETTER AND SETTER
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Node> getChildren() {
        if (this.children == null) {
            return new ArrayList<>();
        }
        return this.children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getPosGroup() {
        return posGroup;
    }

    public void setPosGroup(String posGroup) {
        this.posGroup = posGroup;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getMorphology() {
        return morphology;
    }

    public void setMorphology(String morphology) {
        this.morphology = morphology;
    }

    public String getLabelToParent() {
        return labelToParent;
    }

    public void setLabelToParent(String labelToParent) {
        this.labelToParent = labelToParent;
    }
}
