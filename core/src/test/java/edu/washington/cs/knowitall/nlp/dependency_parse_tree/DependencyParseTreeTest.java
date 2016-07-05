package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DependencyParseTreeTest {

    ParZuSentenceParser parser = new ParZuSentenceParser();

    private DependencyParseTree getTree(String sentence) {
        List<DependencyParseTree> tree = parser.convert(Arrays.asList(sentence.split("\n")));
        return tree.get(0);
    }

    @Test
    public void testGetRootElements1() throws Exception {
        DependencyParseTree tree = getTree(
                "1\tEr\ter\tPRO\tPPER\t3|Sg|Masc|Nom\t2\tsubj\t_\t_ \n" +
                "2\tsagte\tsagen\tV\tVVFIN\t3|Sg|Past|_\t0\troot\t_\t_ \n" +
                "3\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n" +
                "4\tvergiss\tvergiss\tV\tVVFIN\t3|Sg|_|_\t2\ts\t_\t_ \n" +
                "5\tes\tes\tPRO\tPPER\t3|Sg|Neut|Nom\t4\tsubj\t_\t_ \n" +
                "6\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ ");

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
        DependencyParseTree tree = getTree(
                "1\tWir\twir\tPRO\tPPER\t1|Pl|_|Nom\t2\tsubj\t_\t_ \n" +
                "2\thaben\thaben\tV\tVAFIN\t1|Pl|Pres|_\t0\troot\t_\t_ \n" +
                "3\tgelernt\tlernen\tV\tVVPP\t_\t2\taux\t_\t_ \n" +
                "4\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n" +
                "5\tum\tum\tKOUI\tKOUI\t_\t7\tkonj\t_\t_ \n" +
                "6\tzu\tzu\tPTKZU\tPTKZU\t_\t7\tpart\t_\t_ \n" +
                "7\tbestehen\tbestehen\tV\tVVINF\t_\t2\tneb\t_\t_ \n" +
                "8\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n");

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
