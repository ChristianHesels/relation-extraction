package de.hpi.extractor.chunking;

import de.hpi.extractor.ExtractorException;
import de.hpi.extractor.ExtractorUnion;
import de.hpi.extractor.chunking.mapper.ReVerbRelationDictionaryFilter;
import de.hpi.extractor.chunking.mapper.ReVerbRelationMappers;
import de.hpi.nlp.chunking.ChunkedSentence;
import de.hpi.nlp.extraction.chunking.ChunkedRelationExtraction;
import de.hpi.sequence.SequenceException;

import java.io.IOException;


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
        "PTKNEG_pos? PTKVZ_pos? ADV_pos? PAV_pos? ADJD_pos?";

    /**
     * Definition of the "non-verb/prep" part of the relation pattern.
     */
    public static final String WORD =
        "["
        + "NE_pos NN_pos "           // noun
        + "PPOSAT_pos "              // pronoun
        + "PIAT_pos PIDAT_pos "      // determiner
        + "ADJA_pos "                // adjective
        + "ADV_pos "                 // adverb
        + "ART_pos "                 // article
        + "PRF_pos "                 // reflexive pronoun
        + "]";

    /**
     * Definition of the "preposition" part of the relation pattern.
     */
    public static final String PREP =
        "ADV_pos? PAV_pos? [PTKNEG_pos PTKVZ_pos APPR_pos APPRART_pos] ADV_pos? PAV_pos?";

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
     * Definition of the "verb" of the relation pattern.
     */
    public static final String VERB_PRF =
            // Optional adverb
            "ADV_pos? PAV_pos? PTKNEG_pos? " +
            // Modal or other verbs
            "[VVFIN_pos VVINF_pos VVIZU_pos VVPP_pos VAFIN_pos VAINF_pos VAPP_pos VMFIN_pos VMINF_pos VMPP_pos PTKVZ_pos PTKNEG_pos] "
            +
            // Optional particle/adverb
            "PTKNEG_pos? PTKVZ_pos? ADV_pos? PAV_pos? ADJD_pos? PRF_pos?";

    /**
     * Definition of the "non-verb/prep" part of the relation pattern.
     */
    public static final String WORD_PRF =
            "["
                    + "NE_pos NN_pos "           // noun
                    + "PPOSAT_pos "              // pronoun
                    + "PIAT_pos PIDAT_pos "      // determiner
                    + "ADJA_pos "                // adjective
                    + "ADV_pos "                 // adverb
                    + "ART_pos "                 // article
                    + "PRF_pos "                 // reflexive pronoun
                    + "]";

    /**
     * Definition of the "preposition" part of the relation pattern.
     */
    public static final String PREP_PRF =
            "ADV_pos? PAV_pos? [PTKNEG_pos PTKVZ_pos APPR_pos APPRART_pos] ADV_pos? PAV_pos? PRF_pos?";

    /**
     * The pattern (V(W*P)?)+
     */
    public static final String LONG_RELATION_PATTERN_PRF =
            String.format("(%s (%s* (%s)+)?)+", VERB_PRF, WORD_PRF, PREP_PRF);

    /**
     * The pattern (VP?)+
     */
    public static final String SHORT_RELATION_PATTERN_PRF =
            String.format("(%s (%s)?)+", VERB_PRF, PREP_PRF);

    /**
     * Constructs a new extractor using the default relation pattern, relation mappers, and argument
     * mappers.
     *
     * @throws de.hpi.extractor.ExtractorException if unable to initialize the extractor
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
     * @param reflexiveVerbs       - Add the reflexive pronoun always to the relation phrase?
     * @throws ExtractorException if unable to initialize the extractor
     */
    public ReVerbRelationExtractor(int minFreq, boolean useLexSynConstraints,
                                   boolean mergeOverlapRels, boolean combineVerbs, boolean reflexiveVerbs)
        throws ExtractorException {
        init(
            minFreq, useLexSynConstraints, mergeOverlapRels, combineVerbs, reflexiveVerbs);
    }

    /**
     * Wrapper for default initialization of the reverb relation extractor. Use lexical and
     * syntactic constraints, merge overlapping relations, require a minimum of 20 distinct arguments
     * for support, do not combine separated verbs
     */
    protected void init() throws ExtractorException {
        init(ReVerbRelationDictionaryFilter.defaultMinFreq, true, true, false, false);
    }

    /**
     * Initialize relation extractor.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     * @param mergeOverlapRels     - Merge overlapping relations?
     * @param combineVerbs         - Combine separated verbs?
     * @param reflexiveVerbs       - Add reflexive pronouns to the relation phrase?
     * @throws ExtractorException if unable to initialize the extractor
     */
    protected void init(int minFreq, boolean useLexSynConstraints,
                        boolean mergeOverlapRels, boolean combineVerbs, boolean reflexiveVerbs)
        throws ExtractorException {

        try {
            if (reflexiveVerbs) {
                this.addExtractor(new RegexExtractor(SHORT_RELATION_PATTERN_PRF));
            } else {
                this.addExtractor(new RegexExtractor(SHORT_RELATION_PATTERN));
            }
        } catch (SequenceException e) {
            throw new ExtractorException(
                "Unable to initialize short pattern extractor", e);
        }

        try {
            if (reflexiveVerbs) {
                this.addExtractor(new RegexExtractor(LONG_RELATION_PATTERN_PRF));
            } else {
                this.addExtractor(new RegexExtractor(LONG_RELATION_PATTERN));
            }
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

