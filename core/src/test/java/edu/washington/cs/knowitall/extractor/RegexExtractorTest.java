package edu.washington.cs.knowitall.extractor;


import com.google.common.collect.Iterables;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;

import static org.junit.Assert.assertEquals;

public class RegexExtractorTest {

    private ChunkedSentence sent1;
    private String verb = "[VAFIN_pos]";
    private String np = "(B-NP_np I-NP_np*) | (B-PP_np I-PP_np*)";
    private String prep = "[APPR_pos]";


    @Before
    public void setUp() throws Exception {
        sent1 = new ChunkedSentence(
                new String[] { "Obama", "war", "ein", "Professor", "für", "Recht", "an", "der", "Universität", "in", "Chicago", "." },
                new String[] { "NE", "VAFIN", "ART", "NN", "APPR", "NN", "APPR", "ART", "NN", "APPR", "NE", "$." },
                new String[] { "B-NP", "B-VP", "B-NP", "I-NP", "B-PP", "I-PP", "B-PP", "I-PP", "I-PP", "B-PP", "IPP", "O" }
        );
    }

    @Test
    public void testExtract1() throws Exception {
        String pattern = "(" + verb + np + prep + "|" + verb + ")+";

        RegexExtractor extractor = new RegexExtractor(pattern);
        Iterable<ChunkedExtraction> extrIter = extractor.extract(sent1);
        List<ChunkedExtraction> extrs = new ArrayList<ChunkedExtraction>();
        Iterables.addAll(extrs, extrIter);

        assertEquals(1, extrs.size());

        ChunkedExtraction extr = extrs.get(0);
        assertEquals("war ein Professor für Recht an", extr.toString());
    }

}
