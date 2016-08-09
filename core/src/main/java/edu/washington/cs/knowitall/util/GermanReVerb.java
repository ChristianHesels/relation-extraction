package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.chunking.GermanReVerbExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;


/**
 * Utility class to call German ReVerb.
 * German ReVerb uses chunking to extract relations from German sentences.
 */
public class GermanReVerb extends ExtractorChunks {

    private GermanReVerbExtractor extractor;

    /**
     * Constructor of German ReVerb
     */
    public GermanReVerb() {
        this(false);
    }

    /**
     * Constructor of German ReVerb
     * @param debug  enable debug mode?
     */
    public GermanReVerb(boolean debug) {
        super(debug);
        this.extractor = new GermanReVerbExtractor();
    }

    /**
     * Constructor of German ReVerb
     * @param debug enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of German Reverb?
     * @param combineVerbs combine separated verbs?
     * @param useMorphologyLexicon use a morphology lexicon?
     * @param extractSubsentences divide the sentence into subsentence before extracting relations?
     */
    public GermanReVerb(boolean debug, int minFreq, boolean useLexSynConstraints,
                        boolean combineVerbs, boolean useMorphologyLexicon, boolean extractSubsentences) {
        super(debug);
        this.extractor = new GermanReVerbExtractor(minFreq, useLexSynConstraints, combineVerbs, useMorphologyLexicon, extractSubsentences);
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }
}
