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
    // Prune candidates (include pp?, ...)
    // obja: sich ?

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        List<TreeExtraction> extrs = new ArrayList<>();

        List<Node> candidates = extractObjectComplementCandidates(rel);

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

        // there can be a maximum of one objp
        Node objp = candidates.stream().filter(x -> x.getLabelToParent().equals("objp")).findFirst().orElse(null);
        Node other = candidates.stream().filter(x -> !x.getLabelToParent().equals("objp")).findFirst().orElse(null);

        if (objp != null && other != null) {
            // Add the objp to the relation
            List<Integer> ids = objp.toList().stream().map(Node::getId).collect(Collectors.toList());
            ids.addAll((Collection<? extends Integer>) rel.getNodeIds());
            rel.setNodeIds(ids);

            // The remaining candidate is the object
            extrs.add(new TreeExtraction(rel.getRootNode(), other.toList().stream().map(Node::getId).collect(
                Collectors.toList())));
            return extrs;
        }

        System.out.println("There is no objp or both arguments are objp: " + candidates);

        return extrs;
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
