package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeTest {

    @Test
    public void testParse1() {
        String data = "1\tDas\tdie\tPRO\tPDS\tNeut|Nom|Sg\t2\tsubj\t_\t_ \n";
        Node node = new Node(data);

        assertEquals("die", node.getLemma());
        assertEquals("Das", node.getWord());
        assertEquals("subj", node.getLabelToParent());
        assertEquals("Neut|Nom|Sg", node.getMorphology());
        assertEquals("PDS", node.getPos());
        assertEquals("PRO", node.getPosGroup());
        assertEquals(1, node.getId());
        assertEquals(2, node.getParentId());
    }

}
