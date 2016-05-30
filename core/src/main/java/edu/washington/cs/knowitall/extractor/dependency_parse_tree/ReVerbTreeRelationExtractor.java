package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * Extracts the relation phrase from the tree.
 */
public class ReVerbTreeRelationExtractor extends Extractor<DependencyParseTree, TreeExtraction> {

    private static final String VERB = "VVFIN VVINF VVIZU VAFIN VAINF VMINF VMFIN PTKVZ VMPP VAPP VVPP";

    // TODO neb

    @Override
    protected Iterable<TreeExtraction> extractCandidates(DependencyParseTree source)
        throws ExtractorException {
        List<TreeExtraction> rels = new ArrayList<>();

        // get the main verbs
        List<Node> rootChildren = source.getRootElement().getChildren();
        List<Node> verbNodes = rootChildren.stream().filter(x -> x.matchPosTag(toList(VERB))).collect(Collectors.toList());

        // for each main verb, check if there are auxiliary verbs
        for (Node verb : verbNodes) {
            List<Integer> verbIds = verb.getChildren().stream()
                .filter(x -> x.getLabelToParent().equals("aux") || x.getLabelToParent().equals("avz"))
                .map(Node::getId)
                .collect(Collectors.toList());

            verbIds.add(0, verb.getId());

            TreeExtraction extraction = new TreeExtraction(source, verbIds, verb);
            rels.add(extraction);
        }

        return rels;
    }

    /**
     * Converts the given string into a list by splitting the string at whitespace.
     * @param str the string
     * @return a list
     */
    private List<String> toList(String str) {
        return new ArrayList<>(Arrays.asList(str.split(" ")));

    }

}

