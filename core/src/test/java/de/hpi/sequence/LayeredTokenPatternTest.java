package de.hpi.sequence;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.hpi.nlp.chunking.TreeTaggerSentenceChunker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LayeredTokenPatternTest {


    private SimpleLayeredSequence seq;

    public List<String> split(String s) {
        String[] toks = s.split(" ");
        ArrayList<String> result = new ArrayList<String>(toks.length);
        for (String t : toks) {
            result.add(t);
        }
        return result;
    }

    @Before
    public void setUp() throws Exception {

        String[] words = "Es gibt fünf Arten von Eulen .".split(" ");
        String[] pos = "PPER VVFIN CARD NN APPR NN $.".split(" ");
        String[] np = "B-NP B-VP B-NP I-NP B-PP I-PP O".split(" ");

        seq = new SimpleLayeredSequence(words.length);
        seq.addLayer("w", words);
        seq.addLayer("p", pos);
        seq.addLayer("n", np);
    }

    @Test
    public void testMatcher1() throws SequenceException {
        String patternStr = "Es_w gibt_w CARD_p [B-NP_n I-NP_n]+ (APPR_p [B-PP_n I-PP_n]+)*";
        LayeredTokenPattern pat = new LayeredTokenPattern(patternStr);
        LayeredTokenMatcher m = pat.matcher(seq);
        assertTrue(m.find());
        assertEquals(0, m.start());
        assertEquals(6, m.end());
    }

    @Test
    public void testMatcher2() throws SequenceException {
        String patternStr = "B-NP_n I-NP_n*";
        LayeredTokenPattern pat = new LayeredTokenPattern(patternStr);
        LayeredTokenMatcher m = pat.matcher(seq);
        assertTrue(m.find());
        assertEquals(0, m.start());
        assertEquals(1, m.end());
        assertTrue(m.find());
        assertEquals(2, m.start());
        assertEquals(4, m.end());
        assertFalse(m.find());
    }

    @Test
    public void testMatcher3() throws SequenceException {
        String patternStr = "B-PP_n I-PP_n* $._p?$";
        LayeredTokenPattern pat = new LayeredTokenPattern(patternStr);
        LayeredTokenMatcher m = pat.matcher(seq);
        assertTrue(m.find());
        assertEquals(4, m.start());
        assertEquals(7, m.end());
        assertFalse(m.find());
    }

    @Test
    public void testMatcher4() throws SequenceException {
        String patternStr = "...";
        LayeredTokenPattern pat = new LayeredTokenPattern(patternStr);
        LayeredTokenMatcher m = pat.matcher(seq);
        assertTrue(m.find());
        assertEquals(0, m.start());
        assertEquals(3, m.end());
        assertTrue(m.find());
        assertEquals(3, m.start());
        assertEquals(6, m.end());
        assertFalse(m.find());
    }

    @Test(expected = SequenceException.class)
    public void testMatcher5() throws SequenceException {
        String patternStr = "^ [^A_x B_x] C_x $";
        @SuppressWarnings("unused")
        LayeredTokenPattern pat = new LayeredTokenPattern(patternStr);
    }

    @Test(expected = SequenceException.class)
    public void testMatcher6() throws Exception {

        String patternStr = "B-NP_np I-NP_np* from_word the_word B-NP_np I-NP_np*";
        LayeredTokenPattern pattern = new LayeredTokenPattern(patternStr);
        TreeTaggerSentenceChunker chunker = new TreeTaggerSentenceChunker();
        pattern.matcher(chunker.chunkSentence("Hello, world."));

    }
}
