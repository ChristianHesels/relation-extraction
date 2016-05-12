package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;


public class ReVerbTreeArgumentExtractor extends Extractor<DependencyParseTree, Node> {

    private String pattern;

    public ReVerbTreeArgumentExtractor(String pattern) {
        this.pattern = pattern;
    }

    @Override
    protected Iterable<Node> extractCandidates(DependencyParseTree source)
        throws ExtractorException {
        return source.find(this.pattern);
    }


}
