package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;

import java.util.List;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class FirstPosTagNotEqualsFilter extends FilterMapper<TreeExtraction> {

    private List<String> posTags;

    public FirstPosTagNotEqualsFilter(List<String> posTags) {
        this.posTags = posTags;
    }

    public boolean doFilter(TreeExtraction extraction) {
        if (Iterables.size(extraction.getNodeIds()) > 0) {
            List<Node> nodes = extraction.getTree().find(extraction.getNodeIds());
            Node firstNode = nodes.get(0);
            return ! posTags.contains(firstNode.getPos());
        }
        return true;
    }

}
