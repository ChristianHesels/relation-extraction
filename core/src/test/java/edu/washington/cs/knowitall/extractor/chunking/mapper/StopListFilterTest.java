package edu.washington.cs.knowitall.extractor.chunking.mapper;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StopListFilterTest {

    ChunkedRelationExtraction relation;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence = new ChunkedSentence(
            new String[]{"Aral", "Diesel", "ist", "für", "alle", "Fahrzeuge", "mit", "Dieselmotor",
                         "geeignet", "."},
            new String[]{"NN", "NN", "VAFIN", "APPR", "PIAT", "NN", "APPR", "NN", "VVPP", "$."},
            new String[]{"B-NP", "I-NP", "B-VP", "B-NP", "I-NP", "I-PP", "B-PP", "I-PP", "B-VP",
                         "O"}
        );
        relation = new ChunkedRelationExtraction(sentence, new Range(2, 1));
    }

    @Test
    public void testDoFilter1() throws Exception {
        StopListFilter filter = new StopListFilter();

        filter.addStopToken("ist");

        assertFalse(filter.doFilter(relation));
    }

    @Test
    public void testDoFilter2() throws Exception {
        StopListFilter filter = new StopListFilter();

        filter.addStopToken("sind");

        assertTrue(filter.doFilter(relation));
    }

    @Test
    public void testDoFilter3() throws Exception {
        StopListFilter filter = new StopListFilter();

        filter.addStopPosTag("VAFIN");

        assertFalse(filter.doFilter(relation));
    }

    @Test
    public void testDoFilter4() throws Exception {
        StopListFilter filter = new StopListFilter();

        filter.addStopPosTag("ADJD");

        assertTrue(filter.doFilter(relation));
    }
}
