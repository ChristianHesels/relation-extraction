package edu.washington.cs.knowitall.extractor.chunking.mapper;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedRelationExtraction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReVerbRelationDictionaryFilterTest {

    ChunkedRelationExtraction relation1;
    ChunkedRelationExtraction relation2;
    ChunkedRelationExtraction relation3;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence = new ChunkedSentence(
            new String[]{"Grün", "ist", "eine", "Farbe", "."},
            new String[]{"ADJD", "VAFIN", "ART", "NN", "$."},
            new String[]{"B-NP", "B-VP", "B-NP", "I-NP", "O"}
        );
        relation1 = new ChunkedRelationExtraction(sentence, new Range(1, 1));

        sentence = new ChunkedSentence(
            new String[]{"Chips", "machen", "süchtig", "."},
            new String[]{"NN", "VAFIN", "ADV", "$."},
            new String[]{"B-NP", "B-VP", "O", "O"}
        );
        relation2 = new ChunkedRelationExtraction(sentence, new Range(1, 1));

        sentence = new ChunkedSentence(
            new String[]{"Hunde", "müssen", "draußen", "bleiben", "."},
            new String[]{"NN", "VAFIN", "ART", "NN", "$."},
            new String[]{"B-NP", "B-VP", "B-NP", "I-NP", "O"}
        );
        relation3 = new ChunkedRelationExtraction(sentence, new Range(1, 1));
    }

    @Test
    public void testFilter1() throws Exception {
        InputStream in = ReVerbRelationDictionaryFilterTest.class.getClassLoader().getResourceAsStream("rel_dict_freq.txt");
        ReVerbRelationDictionaryFilter filter = new ReVerbRelationDictionaryFilter(in, 20);

        assertTrue(filter.doFilter(relation1));
    }

    @Test
    public void testFilter2() throws Exception {
        InputStream in = ReVerbRelationDictionaryFilterTest.class.getClassLoader().getResourceAsStream("rel_dict_freq.txt");
        ReVerbRelationDictionaryFilter filter = new ReVerbRelationDictionaryFilter(in, 20);

        assertFalse(filter.doFilter(relation2));
    }

    @Test
    public void testFilter3() throws IOException {
        InputStream in = ReVerbRelationDictionaryFilterTest.class.getClassLoader().getResourceAsStream("rel_dict_freq.txt");
        ReVerbRelationDictionaryFilter filter = new ReVerbRelationDictionaryFilter(in, 20);

        assertFalse(filter.doFilter(relation3));
    }
}
