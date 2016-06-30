package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.chunking.GermanReVerbExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;


/**
 * Utility class to call ReVerb.
 */
public class GermanReVerb extends ExtractorChunks {

    private GermanReVerbExtractor extractor;

    /**
     * Constructor of ReVerb
     */
    public GermanReVerb() {
        this(false);
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public GermanReVerb(boolean debug) {
        super(debug);
        this.extractor = new GermanReVerbExtractor();
    }

    /**
     * Constructor of ReVerb
     * @param debug enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of Reverb?
     * @param combineVerbs combine separated verbs?
     * @param useMorphologyLexicon use a morphology lexicon?
     */
    public GermanReVerb(boolean debug, int minFreq, boolean useLexSynConstraints,
                        boolean combineVerbs, boolean useMorphologyLexicon) {
        super(debug);
        this.extractor = new GermanReVerbExtractor(minFreq, useLexSynConstraints, combineVerbs, useMorphologyLexicon);
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }
}
