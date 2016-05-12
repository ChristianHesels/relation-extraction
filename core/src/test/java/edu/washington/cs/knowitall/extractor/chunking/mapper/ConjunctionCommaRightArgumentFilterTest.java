package edu.washington.cs.knowitall.extractor.chunking.mapper;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConjunctionCommaRightArgumentFilterTest {

    ChunkedArgumentExtraction extraction1;
    ChunkedArgumentExtraction extraction2;
    ChunkedArgumentExtraction extraction3;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence1 = new ChunkedSentence(
            new String[]{"ist", ",", "BMW", "."},
            new String[]{"VAFIN", "$,", "NE", "$."},
            new String[]{"B-VP", "O", "B-NP", "O"}
        );
        ChunkedRelationExtraction
            relation1 = new ChunkedRelationExtraction(sentence1, new Range(0, 1));
        extraction1 = new ChunkedArgumentExtraction(sentence1, new Range(2, 1), relation1);

        ChunkedSentence sentence2 = new ChunkedSentence(
            new String[]{"ist", "und", "BMW", "."},
            new String[]{"VAFIN", "$,", "NE", "$."},
            new String[]{"B-VP", "O", "B-NP", "O"}
        );
        ChunkedRelationExtraction relation2 = new ChunkedRelationExtraction(sentence2, new Range(0, 1));
        extraction2 = new ChunkedArgumentExtraction(sentence2, new Range(2, 1), relation2);

        ChunkedSentence sentence3 = new ChunkedSentence(
            new String[]{"sind", "BMW", "und", "Audi"},
            new String[]{"VAFIN", "NE", "KON", "NE"},
            new String[]{"B-VP", "B-NP", "O", "B-NP"}
        );
        ChunkedRelationExtraction relation3 = new ChunkedRelationExtraction(sentence3, new Range(0, 1));
        extraction3 = new ChunkedArgumentExtraction(sentence3, new Range(1, 1), relation3);

    }

    @Test
    public void test() throws Exception {
        ConjunctionCommaRightArgumentFilter filter = new ConjunctionCommaRightArgumentFilter();

        assertFalse(filter.doFilter(extraction1));
        assertFalse(filter.doFilter(extraction2));
        assertTrue(filter.doFilter(extraction3));
    }
}
