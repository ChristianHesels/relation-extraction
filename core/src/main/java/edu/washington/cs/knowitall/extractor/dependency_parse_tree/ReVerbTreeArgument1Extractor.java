package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extracts the subject of the relation.
 */
public class ReVerbTreeArgument1Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    // TODO
    // subj - app - app - kon - app - app - kon - cj - app
    // do not include app if it has a clause as child ?
    // how to handle PN as child ?
    // how to handle NP2 ? "der Wanderer liebt die schöne Müllerin und die Müllerin den Jäger"
    // comma directly before app: do not include app

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        List<TreeExtraction> extrs = new ArrayList<>();

        // First check if there is a subject on the relation itself
        List<Node> relationNodes = rel.getRootNode().find(rel.getNodeIds());
        List<Node> subjectNodes = relationNodes.stream()
            .flatMap(x -> x.getChildrenOfType("subj").stream())
            .collect(Collectors.toList());

        // If the relation has no direct subject, take a look at the root.
        // This can happen, if there exists a conjunction of verbs with the same subject
        if (subjectNodes.isEmpty()) {
            subjectNodes = rel.getRootNode().getChildrenOfType("subj");
        }

        // There exists no subject
        if (subjectNodes.isEmpty()) return extrs;

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
        // Remove all app children, which follow after a comma
        List<Node> appChildren = allChildren.stream().filter(x -> x.getLabelToParent().equals("app") && sentRoot.commaBefore(x.getId())).collect(Collectors.toList());

        allChildren.removeAll(konChildren);
        allChildren.removeAll(appChildren);

        // Get ids of subjectRoot and all underlying nodes
        List<Integer> ids = allChildren.stream()
            .filter(c -> ! (
                c.getLabelToParent().equals("adv")    // adverb
            )).map(Node::getId).collect(Collectors.toList());
        // Create new tree extraction
        return new TreeExtraction(sentRoot, ids);
    }



}
