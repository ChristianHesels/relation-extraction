package edu.washington.cs.knowitall.extractor.chunking;

import java.io.IOException;

import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.extractor.ExtractorUnion;
import edu.washington.cs.knowitall.extractor.chunking.mapper.ReVerbRelationDictionaryFilter;
import edu.washington.cs.knowitall.extractor.chunking.mapper.ReVerbRelationMappers;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;
import edu.washington.cs.knowitall.sequence.SequenceException;


public class ReVerbRelationExtractor extends
                                     ExtractorUnion<ChunkedSentence, ChunkedRelationExtraction> {

    /**
     * Definition of the "verb" of the relation pattern.
     */
    public static final String VERB =
        // Optional adverb
        "ADV_pos? PAV_pos? PTKNEG_pos? " +
        // Modal or other verbs
        "[VVFIN_pos VVINF_pos VVIZU_pos VVPP_pos VAFIN_pos VAINF_pos VAPP_pos VMFIN_pos VMINF_pos VMPP_pos PTKVZ_pos PTKNEG_pos] "
        +
        // Optional particle/adverb
        "PTKNEG_pos? PTKVZ_pos? ADV_pos? PAV_pos? sich_tok?";

    /**
     * Definition of the "non-verb/prep" part of the relation pattern.
     */
    public static final String WORD =
        "["
        + "NE_pos NN_pos "           // noun
        + "PPOSAT_pos "              // pronoun
        + "CARD_pos "                // number
        + "PIAT_pos PIDAT_pos "      // determiner
        + "ADJA_pos "                // adjective
        + "ADV_pos "                 // adverb
        + "ART_pos "                 // article
        + "]";

    /**
     * Definition of the "preposition" part of the relation pattern.
     */
    public static final String PREP =
        "ADV_pos? PAV_pos? [PTKNEG_pos PTKVZ_pos APPR_pos APPRART_pos] ADV_pos? PAV_pos? sich_tok?";

    /**
     * The pattern (V(W*P)?)+
     */
    public static final String LONG_RELATION_PATTERN =
        String.format("(%s (%s* (%s)+)?)+", VERB, WORD, PREP);

    /**
     * The pattern (VP?)+
     */
    public static final String SHORT_RELATION_PATTERN =
        String.format("(%s (%s)?)+", VERB, PREP);

    /**
     * Constructs a new extractor using the default relation pattern, relation mappers, and argument
     * mappers.
     *
     * @throws edu.washington.cs.knowitall.extractor.ExtractorException if unable to initialize the extractor
     */
    public ReVerbRelationExtractor() throws ExtractorException {
        init();
    }


    /**
     * Constructs a new extractor using the default relation pattern, relation mappers, and argument
     * mappers.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     * @param mergeOverlapRels     - Merge overlapping relations?
     * @param combineVerbs         - Combine separated verbs?
     * @throws ExtractorException if unable to initialize the extractor
     */
    public ReVerbRelationExtractor(int minFreq, boolean useLexSynConstraints,
                                   boolean mergeOverlapRels, boolean combineVerbs)
        throws ExtractorException {
        init(
            minFreq, useLexSynConstraints, mergeOverlapRels, combineVerbs);
    }

    /**
     * Wrapper for default initialization of the reverb relation extractor. Use lexical and
     * syntactic constraints, merge overlapping relations, require a minimum of 20 distinct arguments
     * for support, do not combine separated verbs
     */
    protected void init() throws ExtractorException {
        init(ReVerbRelationDictionaryFilter.defaultMinFreq, true, true, false);
    }

    /**
     * Initialize relation extractor.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     * @param mergeOverlapRels     - Merge overlapping relations?
     * @param combineVerbs         - Combine separated verbs?
     * @throws ExtractorException if unable to initialize the extractor
     */
    protected void init(int minFreq, boolean useLexSynConstraints,
                        boolean mergeOverlapRels, boolean combineVerbs)
        throws ExtractorException {

        try {
            this.addExtractor(new RegexExtractor(SHORT_RELATION_PATTERN));
        } catch (SequenceException e) {
            throw new ExtractorException(
                "Unable to initialize short pattern extractor", e);
        }

        try {
            this.addExtractor(new RegexExtractor(LONG_RELATION_PATTERN));
        } catch (SequenceException e) {
            throw new ExtractorException(
                "Unable to initialize long pattern extractor", e);
        }

        try {
            this.addMapper(
                new ReVerbRelationMappers(minFreq, useLexSynConstraints, mergeOverlapRels,
                                          combineVerbs));
        } catch (IOException e) {
            throw new ExtractorException(
                "Unable to initialize relation mappers", e);
        }
    }

}

