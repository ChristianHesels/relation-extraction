package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;


import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Argument2 {

    protected Node rootNode;
    protected TreeExtraction relation;
    protected String name;

    private static final int MAX_PP_SIZE = 10;

    public Argument2(Node rootNode, TreeExtraction relation, String name) {
        this.rootNode = rootNode;
        this.relation = relation;
        this.name = name;
    }

    /**
     * @return the distance to the relation
     */
    public int distanceToRelation() {
        return this.relation.getRootNode().getId() - rootNode.getId();
    }

    /**
     * @return the role of this argument (complement, object or both)
     */
    public abstract Role getRole();

    /**
     * @return true, if the argument is a prepositional object, false otherwise
     */
    public abstract Node getPreposition();

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
        extractions.add(new TreeExtraction(this.relation.getRootNode(), getIds(this.rootNode), this.getPreposition()));
        // Add a extraction for each subject in the conjunction
        extractions.addAll(resolveConjunction().stream()
                               .map(kon -> new TreeExtraction(this.relation.getRootNode(), getIds(kon), this.getPreposition()))
                               .collect(Collectors.toList()));

        return extractions;
    }

    /**
     * @return a list of ids belonging to this argument
     */
    public List<Integer> getIds() {
        return getIds(this.rootNode, true);
    }

    /**
     * @param removeKon remove kon child nodes?
     * @return a list of ids belonging to this argument
     */
    public List<Integer> getIds(boolean removeKon) {
        return getIds(this.rootNode, removeKon);
    }

    /**
     * @param n root node
     * @return a list of ids belonging to this argument
     */
    public List<Integer> getIds(Node n) {
        return getIds(n, true);
    }

    /**
     * List the ids of the underlying children.
     * Child nodes, which belong to a conjunction, are removed.
     * @param n root node
     * @param removeKon remove kon child nodes?
     * @return a list of ids
     */
    protected List<Integer> getIds(Node n, boolean removeKon) {
        // Get the conjunction nodes and removes them from the object nodes
        List<Node> konChildren = n.getKonChildren();
        List<Node> allChildren = n.toList();
        if (removeKon) allChildren.removeAll(konChildren);

        allChildren = removePPNodes(allChildren);
        allChildren = removeAPPNodes(allChildren, n);

        // Filter adverbs
        return allChildren.stream()
            .filter(c -> ! (
                c.getLabelToParent().equals("adv") && c.getPos().equals("ADV")   // adverb
            )).map(Node::getId).collect(Collectors.toList());
    }

    /**
     * Removes all pp nodes, which start with a 'Pronominaladverb' (deswegen, dafür, ...) or are too long and therefore too specific.
     * @param all the list of all nodes
     * @return the pruned list
     */
    private List<Node> removePPNodes(List<Node> all) {
        List<Node> ppChildren = all.stream().filter(
            c -> c.getLabelToParent().equals("pp") && (c.getPos().equals("PROAV") || c.toList().size() > MAX_PP_SIZE)
        ).flatMap(x -> x.toList().stream()).collect(Collectors.toList());

        all.removeAll(ppChildren);
        return all;
    }

    /**
     * Remove all app nodes, which follow after a comma
     * @param all the list of all nodes
     * @return the pruned list
     */
    private List<Node> removeAPPNodes(List<Node> all, Node root) {
        List<Node> appChildren = all.stream()
                .filter(x -> x.getLabelToParent().equals("app") && root.commaBefore(x.getId()))
                .flatMap(x -> x.toList().stream())
                .collect(Collectors.toList());

        all.removeAll(appChildren);
        return all;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public TreeExtraction getRelation() {
        return relation;
    }

    public String getName() {
        return name;
    }
}
