package de.hpi.util;


import de.hpi.extractor.chunking.ReVerbExtractor;
import de.hpi.nlp.chunking.ChunkedSentence;
import de.hpi.nlp.extraction.chunking.ChunkedBinaryExtraction;

import java.io.IOException;


/**
 * Utility class to call ReVerb.
 * ReVerb extracts relations from strings using chunking.
 */
public class ReVerb extends ExtractorChunks {

    private ReVerbExtractor extractor;

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     * @throws IOException if the treetagger resource could not be read
     */
    public ReVerb(boolean debug) throws IOException {
        super(debug);
        this.extractor = new ReVerbExtractor();
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of ReVerb?
     * @throws IOException if the treetagger resource could not be read
     */
    public ReVerb(boolean debug, int minFreq, boolean useLexSynConstraints) throws IOException {
        super(debug);
        this.extractor = new ReVerbExtractor(minFreq, useLexSynConstraints);
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }

}
