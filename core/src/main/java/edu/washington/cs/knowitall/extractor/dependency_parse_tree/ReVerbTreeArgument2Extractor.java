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

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        List<TreeExtraction> extrs = new ArrayList<>();

        List<Node> candidates = extractObjectComplementCandidates(rel);

        // There are no possible objects/complements of verb
        if (candidates.isEmpty()) return extrs;

        // If there is only one candidate, the candidate is the object
        if (candidates.size() == 1) {
            extrs.add(new TreeExtraction(rel.getRootNode(),
                                         candidates.get(0).toList().stream().map(Node::getId).collect(Collectors.toList())));
            return extrs;
        }

        // There is no valid case with more than two objects
        if (candidates.size() > 2) {
            System.out.println("Too much argument2 candidates: " + candidates);
            return extrs;
        }

        // Extract possible candidates
        Node objp = candidates.stream().filter(x -> x.getLabelToParent().equals("objp")).findFirst().orElse(null);
        Node objd = candidates.stream().filter(x -> x.getLabelToParent().equals("objd")).findFirst().orElse(null);
        Node objg = candidates.stream().filter(x -> x.getLabelToParent().equals("objg")).findFirst().orElse(null);
        Node obja = candidates.stream().filter(x -> x.getLabelToParent().equals("obja")).findFirst().orElse(null);
        Node obja2 = candidates.stream().filter(x -> x.getLabelToParent().equals("obja2")).findFirst().orElse(null);
        Node pred = candidates.stream().filter(x -> x.getLabelToParent().equals("pred")).findFirst().orElse(null);

        // Handle the occurrence of objp
        Node other = getOther(objd, objg, objg, obja, obja2, pred);
        if (objp != null && other != null) {
            // Add the objp to the relation
            addToRelation(rel, objp);
            // The remaining candidate is the object
            extrs.add(createTreeExtraction(rel, other));
            return extrs;
        }

        if (obja != null && obja2 != null) {
            // Add obja to the relation
            addToRelation(rel, obja);
            extrs.add(createTreeExtraction(rel, obja2));
            return extrs;
        }

        if (obja != null && objd != null) {
            // Add obja to the relation
            addToRelation(rel, objd);
            extrs.add(createTreeExtraction(rel, obja));
            return extrs;
        }

        System.out.print("The argument combination is new: ");
        candidates.stream().forEach(c -> System.out.print(c.getWord() + " - " + c.getLabelToParent() + " ; "));
        System.out.println(" ");

        return extrs;
    }

    private TreeExtraction createTreeExtraction(TreeExtraction rel, Node object) {
        return new TreeExtraction(rel.getRootNode(),
                                  object.toList().stream().map(Node::getId).collect(Collectors.toList()));
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
        List<Node> relNodes = rel.getRootNode().find(rel.getNodeIds());

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
