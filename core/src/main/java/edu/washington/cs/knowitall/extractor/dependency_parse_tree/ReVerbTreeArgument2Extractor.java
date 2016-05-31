package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class ReVerbTreeArgument2Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        List<TreeExtraction> extrs = new ArrayList<>();

        // TODO

        List<Node> relNodes = rel.getRootNode().find(rel.getNodeIds());

        // objects are directly connected to verbs
        List<Integer> ids = new ArrayList<>();
        for (Node n : relNodes) {
            List<Node> arguments = n.getChildren().stream()
                .filter(x -> x.getLabelToParent().equals("obja") ||
                             x.getLabelToParent().equals("objd") ||
                             x.getLabelToParent().equals("objg") ||
                             x.getLabelToParent().equals("objp") ||
                             x.getLabelToParent().equals("pred"))
                .collect(Collectors.toList());

            ids.addAll(arguments.stream().map(Node::getId).collect(Collectors.toList()));
        }

        extrs.add(new TreeExtraction(rel.getRootNode(), ids));

        return extrs;
    }

}
