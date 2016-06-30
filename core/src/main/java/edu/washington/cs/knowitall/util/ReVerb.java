package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.chunking.ReVerbExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;


/**
 * Utility class to call ReVerb.
 */
public class ReVerb extends ExtractorChunks {

    private ReVerbExtractor extractor;

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public ReVerb(boolean debug) {
        super(debug);
        this.extractor = new ReVerbExtractor();
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of Reverb?
     */
    public ReVerb(boolean debug, int minFreq, boolean useLexSynConstraints) {
        super(debug);
        this.extractor = new ReVerbExtractor(minFreq, useLexSynConstraints);
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }



}
