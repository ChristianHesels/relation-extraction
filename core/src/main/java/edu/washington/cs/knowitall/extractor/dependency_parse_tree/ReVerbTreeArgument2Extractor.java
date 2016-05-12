package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class ReVerbTreeArgument2Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    private String pattern;

    public ReVerbTreeArgument2Extractor(String pattern) {
        this.pattern = pattern;
    }

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction source)
        throws ExtractorException {
        List<Integer> ids = new ArrayList<>();
        for (Node n : source.getTree().find(this.pattern)) {
            ids.add(n.id);
        }

        List<TreeExtraction> extrs = new ArrayList<>();
        if (ids.size() > 0) {
            extrs.add(new TreeExtraction(source.getTree(), ids));
        }
        return extrs;
    }


}
