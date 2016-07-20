package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;
import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;


public class FirstTokenNotEqualsFilter extends FilterMapper<TreeExtraction> {

    private List<String> tokens;

    public FirstTokenNotEqualsFilter(List<String> tokens) {
        this.tokens = tokens;
    }

    public boolean doFilter(TreeExtraction extraction) {
        if (Iterables.size(extraction.getNodeIds()) > 0) {
            List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());
            Node firstNode = nodes.get(0);
            return ! tokens.contains(firstNode.getWord().toLowerCase());
        }
        return true;
    }

}
