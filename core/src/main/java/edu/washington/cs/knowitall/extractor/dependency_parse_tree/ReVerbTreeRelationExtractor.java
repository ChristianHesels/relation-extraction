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

        // check if there is a negation
        List<Node> negNodes = getPtkNodes(rootNode);
        verbNodes.addAll(negNodes);

        verbNodes.add(0, rootNode);

        // check if there is a conjunction of verbs
        List<Node> konNodes = new ArrayList<>();
        for (Node verb : verbNodes) {
            Node.getKonNodes(verb, konNodes);
        }

        // Add a extraction for the main verb
        rels.add(createTreeExtraction(verbNodes, rootNode));
        rels.get(0).setKonNodeIds(konNodes.stream().map(Node::getId).collect(Collectors.toList()));

        for (Node kon : konNodes) {
            List<Node> verbs = kon.getChildrenOfType("aux");
            List<Node> avz = kon.getChildrenOfType("avz");
            List<Node> ptk = getPtkNodes(kon);

            // if the conjunction comes from a auxiliary verb,
            // all verbs of the conjunction should be auxiliary verbs too
            // so we need to add the main verb (-> root)
            if (kon.getPos().endsWith("PP") && verbs.isEmpty()) {
                verbs.add(rootNode);
                verbs.add(kon);
                verbs.addAll(avz);
                verbs.addAll(ptk);
                rels.add(createTreeExtraction(verbs, rootNode));
            } else {
                verbs.add(kon);
                verbs.addAll(avz);
                verbs.addAll(ptk);
                rels.add(createTreeExtraction(verbs, rootNode));
            }
        }

        return rels;
    }

    /**
     * @param rootNode the root node
     * @return a list of child nodes, which have the pos tags 'PTKNEG' or 'PTKZU'
     */
    private List<Node> getPtkNodes(Node rootNode) {
        return rootNode.getChildrenOfType("adv", "part").stream()
            .filter(x -> x.getPos().equals("PTKNEG") || x.getPos().equals("PTKZU")).collect(
            Collectors.toList());
    }

    /**
     * Creates a tree extraction with the given nodes.
     * @param nodes additional nodes
     * @param root  the root node
     * @return a tree extraction
     */
    private TreeExtraction createTreeExtraction(List<Node> nodes, Node root) {
        List<Integer> verbIds = nodes.stream().map(Node::getId).collect(Collectors.toList());
        return new TreeExtraction(root, verbIds);
    }

}

