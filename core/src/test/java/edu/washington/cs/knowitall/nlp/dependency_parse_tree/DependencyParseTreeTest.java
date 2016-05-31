package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DependencyParseTreeTest {

    ParZuSentenceParser parser = new ParZuSentenceParser();

    @Test
    public void testGetRootElements1() throws Exception {
        // Create tree
        List<DependencyParseTree>
            trees = parser.parseSentence("Er sagte, vergiss es.");
        DependencyParseTree tree = trees.get(0);

        // Expected roots
        List<Node> expectedRoots = new ArrayList<>();
        expectedRoots.add(tree.getTree().getChildren().get(0).getChildren().get(1));
        expectedRoots.add(tree.getTree().getChildren().get(0));

        // Execute functionality
        List<Node> actualRoots = tree.getRootElements();

        // Check
        assertThat(actualRoots, is(expectedRoots));
    }

    @Test
    public void testGetRootElements2() throws Exception {
        // Create tree
        List<DependencyParseTree>
            trees = parser.parseSentence("Wir haben gelernt, um zu bestehen.");
        DependencyParseTree tree = trees.get(0);

        // Expected roots
        List<Node> expectedRoots = new ArrayList<>();
        expectedRoots.add(tree.getTree().getChildren().get(0).getChildren().get(2));
        expectedRoots.add(tree.getTree().getChildren().get(0));

        // Execute functionality
        List<Node> actualRoots = tree.getRootElements();

        // Check
        assertThat(actualRoots, is(expectedRoots));
    }


}
