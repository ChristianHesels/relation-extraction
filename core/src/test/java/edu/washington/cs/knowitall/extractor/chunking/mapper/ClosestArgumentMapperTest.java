package edu.washington.cs.knowitall.extractor.chunking.mapper;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

import static org.junit.Assert.assertTrue;

public class ClosestArgumentMapperTest {

    ChunkedArgumentExtraction extraction1;
    ChunkedArgumentExtraction extraction2;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence = new ChunkedSentence(
            new String[]{"Aral", "Diesel", "ist", "für", "alle", "Fahrzeuge", "mit", "Dieselmotor",
                         "geeignet", "."},
            new String[]{"NN", "NN", "VAFIN", "APPR", "PIAT", "NN", "APPR", "NN", "VVPP", "$."},
            new String[]{"B-NP", "I-NP", "B-VP", "B-NP", "I-NP", "I-PP", "B-PP", "I-PP", "B-VP",
                         "O"}
        );
        ChunkedRelationExtraction relation = new ChunkedRelationExtraction(sentence, new Range(2, 1));
        extraction1 = new ChunkedArgumentExtraction(sentence, new Range(0, 1), relation);
        extraction2 = new ChunkedArgumentExtraction(sentence, new Range(1, 2), relation);
    }

    @Test
    public void testDoFilter() throws Exception {
        ClosestArgumentMapper mapper = new ClosestArgumentMapper();

        assertTrue(mapper.doValueMap(extraction1) < mapper.doValueMap(extraction2));
    }

}
