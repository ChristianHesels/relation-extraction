package de.hpi.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;
import de.hpi.extractor.FilterMapper;
import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;

/**
 * Filters out arguments, which consists of a token from the given list.
 */
public class TokenNotEqualsFilter extends FilterMapper<TreeExtraction> {

    private List<String> tokens;

    public TokenNotEqualsFilter(List<String> tokens) {
        this.tokens = tokens;
    }

    public boolean doFilter(TreeExtraction extraction) {
        if (Iterables.size(extraction.getNodeIds()) == 1) {
            List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());
            Node n = nodes.get(0);
            return !tokens.contains(n.getWord());
        }
        return true;
    }

}
