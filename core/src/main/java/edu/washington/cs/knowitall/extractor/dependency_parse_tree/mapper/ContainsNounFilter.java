package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class ContainsNounFilter extends FilterMapper<TreeExtraction> {

    @Override
    public boolean doFilter(TreeExtraction extraction) {
        List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());

        List<Node> nounNodes = nodes.stream().filter(x -> x.getPosGroup().equals("N")).collect(
            Collectors.toList());

        return !nounNodes.isEmpty() ;
    }
}
