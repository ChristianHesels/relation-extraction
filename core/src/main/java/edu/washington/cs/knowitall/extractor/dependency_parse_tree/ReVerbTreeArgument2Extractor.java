package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class ReVerbTreeArgument2Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    // TODO
    // Prune candidates: remove pp
    // obja: sich ?
    // conjunction


    private boolean considerAllArguments;

    public ReVerbTreeArgument2Extractor() {
        this(false);
    }

    /**
     * Creates a argument 2 extractor.
     * @param considerAllArguments consider arguments of child nodes for root nodes?
     */
    public ReVerbTreeArgument2Extractor(boolean considerAllArguments) {
        this.considerAllArguments = considerAllArguments;
    }

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        List<TreeExtraction> extrs = new ArrayList<>();

        List<Node> candidates = extractObjectComplementCandidates(rel);

        // There are no possible objects/complements of verb
        if (candidates.isEmpty()) return extrs;

        // If there is only one candidate, the candidate is the object
        if (candidates.size() == 1) {
            extrs.addAll(createTreeExtraction(rel, candidates.get(0)));
            return extrs;
        }

        // There is no valid case with more than two objects
        if (candidates.size() > 2) {
            System.out.println("Too much argument2 candidates: ");
            candidates.stream()
                .forEach(c -> System.out.print(c.getWord() + " - " + c.getLabelToParent() + " ; "));
            return extrs;
        }

        // Extract possible candidates
        // There can only be multiple accusative objects and prepositional objects
        List<Node> objpList = candidates.stream().filter(x -> x.getLabelToParent().equals("objp")).collect(
            Collectors.toList());
        Node objd = candidates.stream().filter(x -> x.getLabelToParent().equals("objd")).findFirst().orElse(null);
        Node objg = candidates.stream().filter(x -> x.getLabelToParent().equals("objg")).findFirst().orElse(null);
        List<Node> objaList = candidates.stream().filter(x -> x.getLabelToParent().equals("obja")).collect(
            Collectors.toList());
        Node obja2 = candidates.stream().filter(x -> x.getLabelToParent().equals("obja2")).findFirst().orElse(null);
        Node pred = candidates.stream().filter(x -> x.getLabelToParent().equals("pred")).findFirst().orElse(null);

        // We have only two accusative objects
        handleTwoAccusativeObjects(rel, extrs, objaList);
        if (!extrs.isEmpty()) return extrs;

        // We have only two prepositional objects
        handleTwoPrepositionalObjects(rel, extrs, objpList);
        if (!extrs.isEmpty()) return extrs;

        // Extract obja and objp
        Node obja = null;
        if (objaList.size() == 1) obja = objaList.get(0);
        Node objp = null;
        if (objpList.size() == 1) objp = objpList.get(0);

        // Handle accusative object
        Node other = getOther(objp, objd, objg, pred);
        handleAcc(rel, extrs, obja, other);
        if (!extrs.isEmpty()) return extrs;

        // We have one prepositional object and one other object
        other = getOther(objd, objg, pred);
        handle(extrs, rel, objp, other);
        if (!extrs.isEmpty()) return extrs;

        // We have one obja and one obja2
        handle(extrs, rel, obja, obja2);
        if (!extrs.isEmpty()) return extrs;

        // We have one obja and one objd
        handle(extrs, rel, objd, obja);
        if (!extrs.isEmpty()) return extrs;

        // We have a predicate and one other object
        other = getOther(objd, objg, obja);
        handle(extrs, rel, other, pred);
        if (!extrs.isEmpty()) return extrs;

        System.out.print("The argument combination is new: ");
        candidates.stream()
            .forEach(c -> System.out.print(c.getWord() + " - " + c.getLabelToParent() + " ; "));
        System.out.println(" ");

        return extrs;
    }

    /**
     * Handles the case if there are exactly two prepositional objects and nothing else.
     * The prepositional object, which comes first, is the complement of the verb.
     * The second prepositional object is the object.
     * @param rel       the relation
     * @param extrs     the list of extractions
     * @param objpList  the prepositional objects
     */
    private void handleTwoPrepositionalObjects(TreeExtraction rel, List<TreeExtraction> extrs,
                                               List<Node> objpList) {
        if (objpList.size() == 2) {
            Node objpFirst = objpList.get(0);
            Node objpSecond = objpList.get(1);
            handle(extrs, rel, objpFirst, objpSecond);
        }
    }

    /**
     * Handles the case if there are exactly two accusative objects and nothing else.
     * The accusative object, which is equal to 'sich' becomes the complement of the verb.
     * If there is no 'sich', the first accusative object is the complement and the other is the
     * object.
     * @param rel       the relation
     * @param extrs     the list of extractions
     * @param objaList  the accusative objects
     */
    private void handleTwoAccusativeObjects(TreeExtraction rel, List<TreeExtraction> extrs,
                                            List<Node> objaList) {
        if (objaList.size() == 2) {
            Node objaFirst = objaList.get(0);
            Node objaSecond = objaList.get(1);

            if (isSich(objaSecond)) {
                handle(extrs, rel, objaSecond, objaFirst);
            } else {
                handle(extrs, rel, objaFirst, objaSecond);
            }
        }
    }

    private void handleAcc(TreeExtraction rel, List<TreeExtraction> extrs, Node obja, Node other) {
        // Check if obja is "sich"
        if (isSich(obja)) {
            handle(extrs, rel, obja, other);
        } else {
            handle(extrs, rel, other, obja);
        }
    }

    private boolean isSich(Node n) {
        return n != null && n.getWord().equals("sich");
    }
    /**
     * Adds a tree extraction to the given list of extractions.
     * An extraction is only created if the complement and the object is not null.
     * The complement is added to the given relation and the object is used to create the new
     * tree extraction.
     * @param extrs      the list of existing tree extractions
     * @param relation   the relation
     * @param complement the complement of the relation
     * @param object     the object
     */
    private void handle(List<TreeExtraction> extrs, TreeExtraction relation, Node complement, Node object) {
        if (complement != null && object != null) {
            // Add the complement to the relation
            addToRelation(relation, complement);
            // The remaining candidate is the object
            extrs.addAll(createTreeExtraction(relation, object));
        }
    }

    /**
     * Creates a tree extraction for the given object.
     * @param rel    the relation for getting the root node
     * @param object the object
     * @return tree extraction
     */
    private List<TreeExtraction> createTreeExtraction(TreeExtraction rel, Node object) {
        List<TreeExtraction> extractions = new ArrayList<>();

        // Check if there exists a conjunction of objects
        List<Node> konNodes = new ArrayList<>();
        Node.getKonNodes(object, konNodes);

        // Add the main object
        extractions.add(new TreeExtraction(rel.getRootNode(), getIds(object)));

        // Add a extraction for each subject in the conjunction
        extractions.addAll(konNodes.stream()
                         .map(kon -> new TreeExtraction(rel.getRootNode(), getIds(kon)))
                         .collect(Collectors.toList()));

        return extractions;
    }

    /**
     * List the ids of the underlying children.
     * Child nodes, which belong to a conjunction, are removed.
     * @param object the object root
     * @return a list of ids
     */
    private List<Integer> getIds(Node object) {
        // Get the conjunction nodes and removes them from the object nodes
        List<Node> konChildren = object.getKonChildren();
        List<Node> allChildren = object.toList();
        allChildren.removeAll(konChildren);
        // Get ids of object and all underlying nodes
        return allChildren.stream().map(Node::getId).collect(Collectors.toList());
    }

    /**
     * Adds the complement nodes to the given relation.
     * @param rel        the relation
     * @param complement the complement
     */
    private void addToRelation(TreeExtraction rel, Node complement) {
        List<Integer> ids = complement.toList().stream().map(Node::getId).collect(Collectors.toList());
        ids.addAll((Collection<? extends Integer>) rel.getNodeIds());
        rel.setNodeIds(ids);
    }

    /**
     * @param others a list of nodes
     * @return returns the first node, which is not null
     */
    private Node getOther(Node... others) {
        for (Node o : others) {
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    /**
     * Extract candidates for objects and verb complements.
     * @param rel relation extraction
     * @return list of root nodes of candidates
     */
    private List<Node> extractObjectComplementCandidates(TreeExtraction rel) {
        // First check if there is a argument directed connected to the relation node
        List<Node> relNodes = rel.getRootNode().find(rel.getNodeIds());
        List<Node> arguments = getArguments(relNodes);

        if (this.considerAllArguments) {
            // If not, check if there are arguments connected to conjunction child nodes
            if (arguments.isEmpty()) {
                relNodes = rel.getRootNode().find(rel.getKonNodeIds());
                arguments = getArguments(relNodes);
            }
            // If not, check if the root node of the relation has an argument
            if (arguments.isEmpty()) {
                relNodes = new ArrayList<>();
                relNodes.add(rel.getRootNode());
                arguments = getArguments(relNodes);
            }
        }

        return arguments;
    }

    private List<Node> getArguments(List<Node> relNodes) {
        // objects are directly connected to verbs
        List<Node> arguments = new ArrayList<>();
        for (Node n : relNodes) {
            List<Node> a = n.getChildren().stream()
                .filter(x -> x.getLabelToParent().equals("obja") ||
                             x.getLabelToParent().equals("obja2") ||
                             x.getLabelToParent().equals("objd") ||
                             x.getLabelToParent().equals("objg") ||
                             x.getLabelToParent().equals("objp") ||
                             x.getLabelToParent().equals("pred"))
                .collect(Collectors.toList());
            arguments.addAll(a);
        }
        return arguments;
    }

}
