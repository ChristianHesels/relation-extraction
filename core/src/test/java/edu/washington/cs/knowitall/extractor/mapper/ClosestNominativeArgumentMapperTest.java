package edu.washington.cs.knowitall.extractor.mapper;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

import static org.junit.Assert.assertTrue;


public class ClosestNominativeArgumentMapperTest {

    ChunkedArgumentExtraction extraction1;
    ChunkedArgumentExtraction extraction2;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence = new ChunkedSentence(
            new String[]{"Die", "Schale", "des", "Apfels", "ist", "gesund", "."},
            new String[]{"ART", "NN", "ART", "NN", "VAFIN", "ADJD", "$."},
            new String[]{"B-NP", "I-NP", "B-NP", "I-NP", "B-VP", "B-NP", "O"}
        );
        ChunkedRelationExtraction relation = new ChunkedRelationExtraction(sentence, new Range(4, 1));
        extraction1 = new ChunkedArgumentExtraction(sentence, new Range(0, 2), relation);
        extraction2 = new ChunkedArgumentExtraction(sentence, new Range(2, 4), relation);
    }

    @Test
    public void testDoFilter() throws Exception {
        ClosestNominativeArgumentMapper mapper = new ClosestNominativeArgumentMapper(true);

        assertTrue(mapper.doValueMap(extraction1) > mapper.doValueMap(extraction2));
    }

}
