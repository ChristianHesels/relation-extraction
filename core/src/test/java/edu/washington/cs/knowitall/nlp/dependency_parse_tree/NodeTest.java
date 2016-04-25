package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeTest {

    @Test
    public void testParse1() {
        String data = "(ADJD-MO-Pos\\/V Gleichzeitig)";
        LeafNode node = new LeafNode(data);

        assertEquals("ADJD", node.pos);
        assertEquals("MO", node.label);
        assertEquals("Pos", node.morphology);
        assertEquals("Gleichzeitig", node.word);
    }

    @Test
    public void testParse2() {
        String data = "(KON-CD und)";
        LeafNode node = new LeafNode(data);

        assertEquals("KON", node.pos);
        assertEquals("CD", node.label);
        assertEquals("und", node.word);
    }

    @Test
    public void testParse3() {
        String data = "(S-TOP";
        InnerNode node = new InnerNode(data);

        assertEquals("S", node.feature);
        assertEquals("TOP", node.label);
    }
}
