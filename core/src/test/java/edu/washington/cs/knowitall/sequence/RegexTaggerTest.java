package edu.washington.cs.knowitall.sequence;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RegexTaggerTest {

    public static List<String> listize(String s) {
        String[] split = s.split(" ");
        List<String> results = new ArrayList<String>();
        for (String str : split) {
            results.add(str);
        }
        return results;
    }

    public List<String> extract(String patternStr, String test) throws SequenceException {
        LayeredTokenPattern pattern = new LayeredTokenPattern(patternStr);
        RegexTagger tagger = new RegexTagger(pattern, "R");
        List<String> testList = listize(test);
        SimpleLayeredSequence seq = new SimpleLayeredSequence(testList.size());
        seq.addLayer("w", testList);
        return tagger.tag(seq);
    }

    @Test
    public void testTag1() throws SequenceException {
        String patternStr = "[sie_w sah_w Muscheln_w]";
        List<String> result = extract(patternStr, "sie sah Muscheln am Meeresstrand wo sie saß");
        List<String> expected = listize("R R R O O O R O");
        assertEquals(expected, result);
    }

    @Test
    public void testTag2() throws SequenceException {
        String patternStr = ". sah_w .";
        List<String> result = extract(patternStr, "sie sah Muscheln am Meeresstrand");
        List<String> expected = listize("R R R O O");
        assertEquals(expected, result);
    }

}
