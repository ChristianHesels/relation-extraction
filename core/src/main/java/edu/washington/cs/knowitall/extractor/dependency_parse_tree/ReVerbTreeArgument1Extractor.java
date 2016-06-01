package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class ReVerbTreeArgument1Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        List<TreeExtraction> extrs = new ArrayList<>();

        List<Node> subjectNodes = rel.getRootNode().getChildrenOfType("subj");

        // There should only be one subject root node
        assert(subjectNodes.size() == 1);

        Node subjectRoot = subjectNodes.get(0);

        // Check if there exists a conjunction of subjects
        List<Node> konNodes = new ArrayList<>();
        Node.getKonNodes(subjectRoot, konNodes);

        // Add the main subject
        extrs.add(createTreeExtraction(rel.getRootNode(), subjectRoot));

        // Add a extraction for each subject in the conjunction
        extrs.addAll(konNodes.stream()
                         .map(kon -> createTreeExtraction(rel.getRootNode(), kon))
                         .collect(Collectors.toList()));

        return extrs;
    }

    /**
     * Creates a tree extraction with the given sentence root and subject root.
     * @param sentRoot    the sentence root
     * @param subjectRoot the subject root
     * @return a tree extraction
     */
    private TreeExtraction createTreeExtraction(Node sentRoot, Node subjectRoot) {
        // Get the conjunction nodes and removes them from the subject nodes
        List<Node> konChildren = subjectRoot.getKonChildren();
        List<Node> allChildren = subjectRoot.toList();
        allChildren.removeAll(konChildren);
        // Get ids of subjectRoot and all underlying nodes
        List<Integer> ids = allChildren.stream()
            .filter(c -> ! (
                c.getLabelToParent().equals("adv")    // adverb
            )).map(Node::getId).collect(Collectors.toList());
        // Create new tree extraction
        return new TreeExtraction(sentRoot, ids);
    }



}
