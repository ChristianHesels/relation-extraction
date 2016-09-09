package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.chunking.GermanReVerbExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;

import java.io.IOException;


/**
 * Utility class to call German ReVerb.
 * German ReVerb uses chunking to extract relations from German sentences.
 */
public class GermanReVerb extends ExtractorChunks {

    private GermanReVerbExtractor extractor;

    /**
     * Constructor of German ReVerb
     * @throws IOException if the treetagger resource could not be read
     */
    public GermanReVerb() throws IOException {
        this(false);
    }

    /**
     * Constructor of German ReVerb
     * @param debug  enable debug mode?
     * @throws IOException if the treetagger resource could not be read
     */
    public GermanReVerb(boolean debug) throws IOException {
        super(debug);
        this.extractor = new GermanReVerbExtractor();
    }

    /**
     * Constructor of German ReVerb
     * @param debug enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of German Reverb?
     * @param combineVerbs combine separated verbs?
     * @param reflexiveVerbs add the reflexive pronoun always to the relation phrase?
     * @param useMorphologyLexicon use a morphology lexicon?
     * @param extractSubsentences divide the sentence into subsentence before extracting relations?
     * @throws IOException if the treetagger resource could not be read
     */
    public GermanReVerb(boolean debug, int minFreq, boolean useLexSynConstraints,
                        boolean combineVerbs, boolean reflexiveVerbs, boolean useMorphologyLexicon, boolean extractSubsentences) throws IOException {
        super(debug);
        this.extractor = new GermanReVerbExtractor(minFreq, useLexSynConstraints, combineVerbs, reflexiveVerbs, useMorphologyLexicon, extractSubsentences);
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }
}
