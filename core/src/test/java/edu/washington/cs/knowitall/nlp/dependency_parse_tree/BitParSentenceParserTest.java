package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitParSentenceParserTest {

    @Test
    public void testCountWhitespace() {
        BitParSentenceParser parser = new BitParSentenceParser();

        String line = "   Dies ist ein Test. ";
        int actual = parser.countLeadingWhitespace(line);

        assertEquals(3, actual);

        line = "Dies ist ein Test. ";
        actual = parser.countLeadingWhitespace(line);

        assertEquals(0, actual);
    }

    @Test
    public void testConvert() {
        BitParSentenceParser parser = new BitParSentenceParser();

        DependencyParseTree tree = parser.parseSentence(
            "Franz Beckenbauer war ein Fußballspieler.");

        assertEquals("Franz Beckenbauer war ein Fußballspieler .", tree.toString());
    }

    @Test
    public void testRemove() {
        BitParSentenceParser parser = new BitParSentenceParser();
        DependencyParseTree tree = parser.parseSentence(
            "Franz Beckenbauer war ein Fußballspieler.");

        tree.getRootElement().children.get(0).children.get(0).children.get(0).remove();

        assertEquals("war ein Fußballspieler .", tree.toString());
    }
}
