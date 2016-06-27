package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;
import java.util.stream.Collectors;


public class ContainsNounFilter extends FilterMapper<TreeExtraction> {

    private boolean allowWe;

    public ContainsNounFilter() {
        this(false);
    }

    public ContainsNounFilter(boolean allowWe) {
        this.allowWe = allowWe;
    }

    @Override
    public boolean doFilter(TreeExtraction extraction) {
        List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());

        List<Node> nounNodes = nodes.stream()
                .filter(x -> x.getPosGroup().equals("N") || (this.allowWe && x.getWord().toLowerCase().equals("wir")))
                .collect(Collectors.toList());

        return !nounNodes.isEmpty() ;
    }
}
