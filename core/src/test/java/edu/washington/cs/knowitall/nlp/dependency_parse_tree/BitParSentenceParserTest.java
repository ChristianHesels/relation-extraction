package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import java.util.List;

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
    public void testConvert1() {
        BitParSentenceParser parser = new BitParSentenceParser();

        DependencyParseTree tree = parser.parseSentence(
            "Franz Beckenbauer war z.B. ein Fußballspieler.").get(0);

        assertEquals("Franz Beckenbauer war zB ein Fußballspieler .", tree.toString());
    }

    @Test
    public void testConvert2() {
        BitParSentenceParser parser = new BitParSentenceParser();

        List<DependencyParseTree> trees = parser.parseSentence(
            "Franz Beckenbauer war z.B. ein Fußballspieler! Er war großartig.");

        assertEquals("Franz Beckenbauer war zB ein Fußballspieler !", trees.get(0).toString());
        assertEquals("Er war großartig .", trees.get(1).toString());
    }

    @Test
    public void testRemove() {
        BitParSentenceParser parser = new BitParSentenceParser();
        DependencyParseTree tree = parser.parseSentence(
            "Franz Beckenbauer war ein Fußballspieler.").get(0);

        tree.getRootElement().children.get(0).children.get(0).children.get(0).remove();

        assertEquals("war ein Fußballspieler .", tree.toString());
    }

    @Test
    public void testPrintTree() {
        BitParSentenceParser parser = new BitParSentenceParser();
        DependencyParseTree tree = parser.parseSentence(
            "Franz Beckenbauer war ein Fußballspieler.").get(0);

        String treeStr = tree.printTree();

        assertEquals("( TOP ( S-TOP ( NP-SB ( PN-HD ( NE-PNC-Nom.Sg.Masc Franz ) ( NE-PNC-Nom.Sg.Masc Beckenbauer ) ) ) ( VAFIN-HD-Sg war ) ( NP-PD ( ART-HD-Nom.Sg.Masc ein ) ( NN-HD-Nom.Sg.Masc Fußballspieler ) ) ) ( $. . ) )", treeStr);
    }
}
