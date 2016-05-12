package edu.washington.cs.knowitall.extractor;

import com.google.common.collect.Iterables;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import edu.washington.cs.knowitall.extractor.chunking.SubsentenceExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubsentenceExtractorTest {

    private ChunkedSentence sent1;
    private ChunkedSentence sent2;
    private ChunkedSentence sent3;


    @Before
    public void setUp() throws Exception {
        sent1 = new ChunkedSentence(
            new String[]{"Jugendliche", "haben", "die", "Möglichkeit", "zum", "Tischfußball", "und",
                         "Billardspielen", "."},
            new String[]{"NN", "VAFIN", "ART", "NN", "APPRART", "NN", "KON", "NN", "$."},
            new String[]{"B-NP", "B-VP", "B-NP", "I-NP", "B-PP", "I-PP", "O", "B-NP", "O"}
        );
        sent2 = new ChunkedSentence(
            new String[]{"Im", "Jahr", "2000", "übernimmt", "Uwe", "Lammeck", "die",
                         "Geschäftsführung", "."},
            new String[]{"APPRART", "NN", "CARD", "VVFIN", "NE", "NE", "ART", "NN", "$."},
            new String[]{"B-PP", "I-PPP", "I-PP", "B-VP", "B-NP", "I-NP", "B-NP", "I-NP", "O"}
        );
        sent3 = new ChunkedSentence(
            new String[]{"Aral", "Diesel", "ist", "für", "alle", "Fahrzeuge", "mit", "Dieselmotor",
                         "geeignet", "."},
            new String[]{"NN", "NN", "VAFIN", "APPR", "PIAT", "NN", "APPR", "NN", "VVPP", "$."},
            new String[]{"B-NP", "I-NP", "B-VP", "B-NP", "I-NP", "I-PP", "B-PP", "I-PP", "B-VP",
                         "O"}
        );
    }

    @Test
    public void testExtract1() throws Exception {
        SubsentenceExtractor extractor = new SubsentenceExtractor();
        Iterable<ChunkedSentence> subsentences = extractor.extract(sent1);

        assertEquals(1, Iterables.size(subsentences));
        assertFalse(Iterables.isEmpty(subsentences));

        String sent = ((ArrayList) subsentences).get(0).toString();
        assertEquals("Jugendliche haben die Möglichkeit zum Tischfußball und", sent);
    }

    @Test
    public void testExtract2() throws Exception {
        SubsentenceExtractor extractor = new SubsentenceExtractor();
        Iterable<ChunkedSentence> subsentences = extractor.extract(sent2);

        assertTrue(Iterables.isEmpty(subsentences));
    }

    @Test
    public void testExtract3() throws Exception {
        SubsentenceExtractor extractor = new SubsentenceExtractor();
        Iterable<ChunkedSentence> subsentences = extractor.extract(sent3);

        assertEquals(1, Iterables.size(subsentences));
        assertFalse(Iterables.isEmpty(subsentences));

        String sent = ((ArrayList) subsentences).get(0).toString();
        assertEquals("Aral Diesel ist für alle Fahrzeuge mit Dieselmotor geeignet .", sent);
    }

}
