package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

public abstract class Argument2 {

    protected Node rootNode;
    protected TreeExtraction relation;

    public Argument2(Node rootNode, TreeExtraction relation) {
        this.rootNode = rootNode;
        this.relation = relation;
    }

    /**
     * @return the distance to the relation
     */
    public int distanceToRelation() {
        return Math.abs(this.relation.getRootNode().getId() - rootNode.getId());
    }

    /**
     * @return the role of this argument (complement, object or both)
     */
    public abstract Role getRole();

    /**
     * Follows the conjunction starting at this argument.
     * @return a list of nodes, which belong to the conjunction
     */
    protected List<Node> resolveConjunction() {
        List<Node> konNodes = new ArrayList<>();
        Node.getKonNodes(this.rootNode, konNodes);
        return konNodes;
    }

    /**
     * @return a list of tree extraction
     */
    public List<TreeExtraction> createTreeExtractions() {
        List<TreeExtraction> extractions = new ArrayList<>();

        // Add the main object
        extractions.add(new TreeExtraction(this.relation.getRootNode(), getIds(this.rootNode)));
        // Add a extraction for each subject in the conjunction
        extractions.addAll(resolveConjunction().stream()
                               .map(kon -> new TreeExtraction(this.relation.getRootNode(), getIds(kon)))
                               .collect(Collectors.toList()));

        return extractions;
    }

    /**
     * @return a list of ids belonging to this argument
     */
    public List<Integer> getIds() {
        return getIds(this.rootNode);
    }

    /**
     * List the ids of the underlying children.
     * Child nodes, which belong to a conjunction, are removed.
     * @param n root node
     * @return a list of ids
     */
    protected List<Integer> getIds(Node n) {
        // Get the conjunction nodes and removes them from the object nodes
        List<Node> konChildren = n.getKonChildren();
        List<Node> allChildren = n.toList();
        allChildren.removeAll(konChildren);
        // Get ids of object and all underlying nodes
        return allChildren.stream().map(Node::getId).collect(Collectors.toList());
    }


    public Node getRootNode() {
        return rootNode;
    }

    public TreeExtraction getRelation() {
        return relation;
    }

}
