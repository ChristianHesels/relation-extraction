package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.ComPathExtractor;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;

public class ComPath extends ExtractorDependencyParseTrees {

    private ComPathExtractor extractor;

    /**
     * Constructor of ComPath
     */
    public ComPath() {
        this(false);
    }

    /**
     * Constructor of ComPath
     * @param debug  enable debug mode?
     */
    public ComPath(boolean debug) {
        super(debug);
        this.extractor = new ComPathExtractor();
    }

    @Override
    protected Iterable<TreeBinaryExtraction> extract(DependencyParseTree tree) {
        return extractor.extract(tree);
    }

}
