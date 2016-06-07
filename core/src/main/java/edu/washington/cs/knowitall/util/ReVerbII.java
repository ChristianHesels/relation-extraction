package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.chunking.ReVerbIIExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;


/**
 * Utility class to call ReVerb.
 */
public class ReVerbII extends ChunkedReVerb {

    private ReVerbIIExtractor extractor;

    /**
     * Constructor of ReVerb
     */
    public ReVerbII() {
        this(false);
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public ReVerbII(boolean debug) {
        super(debug);
        this.extractor = new ReVerbIIExtractor();
    }

    /**
     * Constructor of ReVerb
     * @param debug enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of Reverb?
     * @param combineVerbs combine separated verbs?
     * @param useMorphologyLexicon use a morphology lexicon?
     */
    public ReVerbII(boolean debug, int minFreq, boolean useLexSynConstraints,
                    boolean combineVerbs, boolean useMorphologyLexicon) {
        super(debug);
        this.extractor = new ReVerbIIExtractor(minFreq, useLexSynConstraints, combineVerbs, useMorphologyLexicon);
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }
}
