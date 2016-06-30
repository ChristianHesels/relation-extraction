package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.dependency_parse_tree.DepReVerbExtractor;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;


/**
 * Utility class to call ReVerb III.
 * ReVerb III uses dependency parse trees to extract relations from strings.
 */
public class DepReVerb extends ExtractorDependencyParseTrees {

    private DepReVerbExtractor extractor;

    /**
     * Constructor of ReVerb
     */
    public DepReVerb() {
        this(false);
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public DepReVerb(boolean debug) {
        super(debug);
        this.extractor = new DepReVerbExtractor();
    }

    /**
     * Constructor of ReVerb with arguments
     * @param debug                enable debug mode?
     * @param considerAllArguments consider arguments of child nodes for root nodes?
     * @param weSubject            extract we as subject?
     */
    public DepReVerb(boolean debug, boolean considerAllArguments, boolean weSubject) {
        super(debug);
        this.extractor = new DepReVerbExtractor(considerAllArguments, weSubject);
    }

    @Override
    protected Iterable<TreeBinaryExtraction> extract(DependencyParseTree tree) {
        return this.extractor.extract(tree);
    }
}
