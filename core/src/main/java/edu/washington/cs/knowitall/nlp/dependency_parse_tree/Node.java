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
        if (ids == null) return new ArrayList<>();
        return this.toList().stream()
            .filter((x -> Iterables.contains(ids, x.getId())))
            .collect(Collectors.toList());
    }

    /**
     * Find the node with the given id.
     * @param id the ids
     * @return the node or null
     */
    public Node find(int id) {
        return this.toList().stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseGet(null);
    }


    /**
     * Find nodes with the given labels.
     * @param labels the labels
     * @return a list of nodes
     */
    public List<Node> find(String... labels) {
        List<String> l = new ArrayList<>(Arrays.asList(labels));
        return this.toList().stream()
            .filter((x -> l.contains(x.getLabelToParent())))
            .collect(Collectors.toList());
    }

    /**
     * Get all nodes, which belong to a conjunction.
     * @param root      the current root node of the conjunction
     * @param konNodes  all nodes listed in the conjunction
     */
    public static void getKonNodes(Node root, List<Node> konNodes) {
        getKonNodes(root, konNodes, root.getPos().equals("TRUNC"));
    }

    /**
     * Get all nodes, which belong to a conjunction.
     * @param root      the current root node of the conjunction
     * @param konNodes  all nodes listed in the conjunction
     * @param isTrunc   true, if we are currently in a 'trunc', false otherwise
     */
    private static void getKonNodes(Node root, List<Node> konNodes, Boolean isTrunc) {
        List<Node> kon = root.getChildrenOfType("kon");
        List<Node> cj = root.getChildrenOfType("cj");

        Node curr;
        if (kon.size() == 1) {
            curr = kon.get(0);
        }
        else if (cj.size() == 1) {
            curr = cj.get(0);
        }
        else return;

        // Only add nodes to the conjunction nodes, if they are not a conjunction (and, or).
        // Do not add nodes, which have '&' as parent, to the conjunction nodes.
        // DO not add nodes, which are connected to a 'TRUNC' node.
        if (!curr.getPosGroup().equals("KON") &&
            !(curr.parent.getPosGroup().equals("KON") && curr.parent.getWord().equals("&")) &&
            !isTrunc) konNodes.add(curr);

        // Update 'isTrunc':
        // 'cj' marks the end of a trunc
        // if the current node is a trunc, the following nodes should not be added
        if (cj.size() == 1) {
            isTrunc = false;
        } else if (curr.getPos().equals("TRUNC")) {
            isTrunc = true;
        }

        getKonNodes(curr, konNodes, isTrunc);
    }

    /**
     * Prunes nodes from this tree, which are not relevant.
     * Such nodes are:
     * rel, vok, par, empty labels, expl, kom
     */
    public void prune() {
        List<Node> childsToRemove = getChildren().stream()
            .filter(n -> n.labelToParent.equals("vok") ||
                         n.labelToParent.equals("par") ||
                         n.labelToParent.equals("expl") ||
                         n.labelToParent.equals("")
            ).collect(Collectors.toList());
        childsToRemove.stream().forEach(Node::remove);
        this.getChildren().stream().forEach(Node::prune);
    }

    /**
     * Returns a list of all subordered kon-nodes.
     * @return a list of nodes
     */
    public List<Node> getKonChildren() {
        List<Node> konChildren = this.getChildrenOfType("kon");

        // There are not conjunctions
        if (konChildren.isEmpty()) {
            return konChildren;
        }

        // Conjunctions starting with '&' are not conjunctions
        if (konChildren.get(0).getWord().equals("&")) {
            return new ArrayList<>();
        }

        List<Node> all = konChildren.get(0).toList();

        // Keep conjunctions "weder" and "sowohl"
        List<Node> exceptionNodes = all.stream().filter(n -> n.getWord().toLowerCase().equals("weder") || n.getWord().toLowerCase().equals("sowohl")).collect(Collectors.toList());
        all.removeAll(exceptionNodes);

        // 'TRUNC' nodes are not conjunctions
        if (this.getPos().equals("TRUNC")) {
            // Remove all TRUNC nodes from the konChildren list
            while (!all.isEmpty() && !all.get(0).getLabelToParent().equals("cj")) {
                all.remove(0);
            }
            // Last element of the TRUNC nodes
            if (!all.isEmpty()) all.remove(0);
        }

        return all;
    }

    /**
     * @param nodeId the node id
     * @return true, if there is a comma before the given node id, false otherwise
     */
    public boolean commaBefore(int nodeId) {
        List<Node> nodes = this.toList().stream().filter(x -> x.getId() == nodeId - 1).collect(Collectors.toList());

        return nodeId > 1 && nodes.isEmpty();
    }

    /**
     * @return all neighbours of this node
     */
    public List<Node> getNeighbours() {
        List<Node> neighbours = new ArrayList<>(getChildren());
        neighbours.add(getParent());
        return neighbours;
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
