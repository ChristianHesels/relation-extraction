package edu.washington.cs.knowitall.extractor;

import edu.washington.cs.knowitall.extractor.mapper.SentenceMergeOverlappingMapper;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.sequence.SequenceException;


public class SubsentenceExtractor extends ExtractorUnion<ChunkedSentence, ChunkedSentence> {

    public static final String VP = "B-VP_np (I-VP_np)*";
    public static final String NP = "B-NP_np (I-NP_np)*";
    public static final String PP = "B-PP_np (I-PP_np)*";
    public static final String O = "O_np";

    public static final String WORD = "[ADJA_pos ADJD_pos ADV_pos APPR_pos APPRART_pos APPO_pos APZR_pos ART_pos CARD_pos NN_pos NE_pos PDS_pos PDAT_pos PIS_pos PIAT_pos PIDAT_pos PPER_pos PPOSS_pos PPOSAT_pos PRELS_pos PRELAT_pos PRF_pos PWS_pos PWAT_pos PWAV_pos PROAV_pos PTKA_pos PTKNEG_pos TRUNC_pos]";

    // The pattern representing some text including a NP
    public static final String TEXT = String.format("%s* %s %s*", WORD, NP, WORD);

    // The pattern NP+ VP TEXT O
    public static final String PATTERN_I =
        String.format("(%s+ %s %s %s)", NP, VP, TEXT, O);

    // The pattern NP+ VP TEXT VP O
    public static final String PATTERN_II =
        String.format("(%s+ %s %s %s %s)", NP, VP, TEXT, VP, O);


    String[] patterns = {PATTERN_I, PATTERN_II};

    public SubsentenceExtractor() throws ExtractorException {
        for (String pattern : patterns) {
            try {
                this.addExtractor(new RegexSentenceExtractor(pattern));
            } catch (SequenceException e) {
                throw new ExtractorException(
                    "Unable to initialize pattern extractor", e);
            }
        }

        this.addMapper(new SentenceMergeOverlappingMapper());
    }

}

