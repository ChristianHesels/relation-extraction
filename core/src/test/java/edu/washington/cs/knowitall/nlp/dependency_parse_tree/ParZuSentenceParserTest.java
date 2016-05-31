package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParZuSentenceParserTest {

    @Test
    public void testConvert() {
        ParZuSentenceParser parser = new ParZuSentenceParser();

        DependencyParseTree tree = parser.parseSentence(
            "Franz Beckenbauer war z.B. ein Fußballspieler.").get(0);

        assertEquals("Franz Beckenbauer war z.B. ein Fußballspieler.", tree.getSentence());

        assertEquals(2, tree.getTree().getChildren().size());
        assertEquals("Franz Beckenbauer war z.B. ein Fußballspieler", tree.getTree().getChildren().get(0).toString());
    }

}
