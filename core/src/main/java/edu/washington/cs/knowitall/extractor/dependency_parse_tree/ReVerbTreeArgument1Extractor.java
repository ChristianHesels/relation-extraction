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

        List<Integer> subjectNodes = rel.getSentenceRoot().getChildren().stream()
            .filter(x -> x.getLabelToParent().equals("subj"))
            .map(Node::getId)
            .collect(Collectors.toList());

        extrs.add(new TreeExtraction(rel.getTree(), subjectNodes, rel.getSentenceRoot()));

        return extrs;
    }

}
