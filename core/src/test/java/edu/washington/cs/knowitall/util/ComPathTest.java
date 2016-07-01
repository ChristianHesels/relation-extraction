package edu.washington.cs.knowitall.util;

import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class ComPathTest {

    @Test
    public void extractRelations() throws Exception {

        String sent = "<comp>Microsoft</comp> kauft <comp>LinkedIn</comp> für 26 Millionen US-Dollar.";

        ComPath comPath = new ComPath();
        List<TreeBinaryExtraction> extraction = comPath.extractRelations(sent);

        assertEquals("Microsoft # kauft # LinkedIn (none)", extraction.get(0).toString());
    }

}
