package edu.washington.cs.knowitall.extractor.mapper;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContainsVerbFilterTest {

    ChunkedRelationExtraction relation1;
    ChunkedRelationExtraction relation2;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence = new ChunkedSentence(
            new String[]{"Die", "Schale", "des", "Apfels", "ist", "gesund", "."},
            new String[]{"ART", "NN", "ART", "NN", "VAFIN", "ADJD", "$."},
            new String[]{"B-NP", "I-NP", "B-NP", "I-NP", "B-VP", "B-NP", "O"}
        );
        relation1 = new ChunkedRelationExtraction(sentence, new Range(4, 1));
        relation2 = new ChunkedRelationExtraction(sentence, new Range(5, 1));
    }

    @Test
    public void testDoFilter() throws Exception {
        ContainsVerbFilter filter = new ContainsVerbFilter();

        assertTrue(filter.doFilter(relation1));
        assertFalse(filter.doFilter(relation2));
    }

}
