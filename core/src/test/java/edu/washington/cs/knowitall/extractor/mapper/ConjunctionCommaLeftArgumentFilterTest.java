package edu.washington.cs.knowitall.extractor.mapper;

import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConjunctionCommaLeftArgumentFilterTest {

    ChunkedArgumentExtraction extraction1;
    ChunkedArgumentExtraction extraction2;
    ChunkedArgumentExtraction extraction3;
    ChunkedArgumentExtraction extraction4;
    ChunkedArgumentExtraction extraction5;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence1 = new ChunkedSentence(
            new String[]{"BMW", ",", "ist"},
            new String[]{"NE", "$,", "VAFIN"},
            new String[]{"B-NP", "O", "B-VP"}
        );
        ChunkedRelationExtraction relation1 = new ChunkedRelationExtraction(sentence1, new Range(2, 1));
        extraction1 = new ChunkedArgumentExtraction(sentence1, new Range(0, 1), relation1);

        ChunkedSentence sentence2 = new ChunkedSentence(
            new String[]{"BMW", "und", "ist"},
            new String[]{"NE", "KON", "VAFIN"},
            new String[]{"B-NP", "O", "B-VP"}
        );
        ChunkedRelationExtraction relation2 = new ChunkedRelationExtraction(sentence2, new Range(2, 1));
        extraction2 = new ChunkedArgumentExtraction(sentence2, new Range(0, 1), relation2);

        ChunkedSentence sentence3 = new ChunkedSentence(
            new String[]{"BMW", "und", "Audi", "sind"},
            new String[]{"NE", "KON", "NN", "VAFIN"},
            new String[]{"B-NP", "O", "B-NP", "B-VP"}
        );
        ChunkedRelationExtraction relation3 = new ChunkedRelationExtraction(sentence3, new Range(3, 1));
        extraction3 = new ChunkedArgumentExtraction(sentence3, new Range(0, 1), relation3);


        ChunkedSentence sentence4 = new ChunkedSentence(
            new String[]{"BMW", ",", "Audi", "und", "Daimler", "sind"},
            new String[]{"NE", "$,", "NE", "KON", "NE", "VAFIN"},
            new String[]{"B-NP", "O", "B-NP", "O", "B-NP", "B-VP"}
        );
        ChunkedRelationExtraction relation4 = new ChunkedRelationExtraction(sentence4, new Range(5, 1));
        extraction4 = new ChunkedArgumentExtraction(sentence4, new Range(0, 1), relation4);
        extraction5 = new ChunkedArgumentExtraction(sentence4, new Range(4, 1), relation4);
    }

    @Test
    public void test() throws Exception {
        ConjunctionCommaLeftArgumentFilter filter = new ConjunctionCommaLeftArgumentFilter();

        assertFalse(filter.doFilter(extraction1));
        assertFalse(filter.doFilter(extraction2));
        assertFalse(filter.doFilter(extraction3));
        assertFalse(filter.doFilter(extraction4));
        assertTrue(filter.doFilter(extraction5));
    }
}
