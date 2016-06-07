package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.chunking.ReVerbIExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;


/**
 * Utility class to call ReVerb.
 */
public class ReVerbI extends ChunkedReVerb {

    private ReVerbIExtractor extractor;

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public ReVerbI(boolean debug) {
        super(debug);
        this.extractor = new ReVerbIExtractor();
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of Reverb?
     */
    public ReVerbI(boolean debug, int minFreq, boolean useLexSynConstraints) {
        super(debug);
        this.extractor = new ReVerbIExtractor(minFreq, useLexSynConstraints);
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }



}
