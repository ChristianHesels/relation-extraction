package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * Extracts the relation phrase from the tree.
 */
public class ReVerbTreeRelationExtractor extends Extractor<Node, TreeExtraction> {


    /**
     * V -> aux
     *   -> kon -> cj -> aux
     *          -> aux
     *
     * V -> kon -> cj
     *
     * V -> aux -> kon -> kon -> ... -> cj
     */


    @Override
    protected Iterable<TreeExtraction> extractCandidates(Node rootNode)
        throws ExtractorException {
        List<TreeExtraction> rels = new ArrayList<>();

        // if the root node is not a verb, the sentence is not parsed properly
        // no relation is extracted from such sentences
        if (!rootNode.getPosGroup().equals("V")) {
            return rels;
        }

        // check if there are auxiliary verbs
        List<Node> verbNodes = rootNode.getChildrenOfType("aux", "avz");

        // Add a extraction for the main verb
        rels.add(createTreeExtraction(verbNodes, rootNode));

        // Add the root node to the verb nodes
        verbNodes.add(0, rootNode);

        // check if there is a conjunction of verbs
        List<Node> konNodes = new ArrayList<>();
        for (Node verb : verbNodes) {
            Node.getKonNodes(verb, konNodes);
        }

        for (Node kon : konNodes) {
            List<Node> verbs = kon.getChildrenOfType("aux");
            List<Node> avz = kon.getChildrenOfType("avz");

            // if the conjunction comes from a auxiliary verb,
            // all verbs of the conjunction should be auxiliary verbs too
            // so we need to add the main verb (-> root)
            if (kon.getPos().endsWith("PP") && verbs.isEmpty()) {
                verbs.add(kon);
                verbs.addAll(avz);
                rels.add(createTreeExtraction(verbs, rootNode));
            } else {
                verbs.addAll(avz);
                rels.add(createTreeExtraction(verbs, kon));
            }
        }

        return rels;
    }

    /**
     * Creates a tree extraction with the given nodes.
     * @param nodes additional nodes
     * @param root  the root node
     * @return a tree extraction
     */
    private TreeExtraction createTreeExtraction(List<Node> nodes, Node root) {
        List<Integer> verbIds = nodes.stream().map(Node::getId).collect(Collectors.toList());
        verbIds.add(0, root.getId());
        return new TreeExtraction(root, verbIds);
    }

}

