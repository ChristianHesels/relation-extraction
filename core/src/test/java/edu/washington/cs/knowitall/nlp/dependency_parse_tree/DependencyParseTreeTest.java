package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testShortestPath1() throws Exception {
        DependencyParseTree tree = getTree(
                "1\tBMW\tBMW\tN\tNE\t_|Nom|Pl\t4\tsubj\t_\t_ \n" +
                "2\tund\tund\tKON\tKON\t_\t1\tkon\t_\t_ \n" +
                "3\tPorsche\tPorsche\tN\tNE\t_|Nom|Pl\t2\tcj\t_\t_ \n" +
                "4\tsind\tsein\tV\tVAFIN\t3|Pl|Pres|Ind\t0\troot\t_\t_ \n" +
                "5\tPartner\tPartner\tN\tNN\tMasc|Nom|_\t4\tpred\t_\t_ \n"
        );

        // Expected roots
        List<Node> path = tree.shortestPath(1, 3);

        List<Integer> pathIds = path.stream().map(Node::getId).collect(Collectors.toList());

        assertTrue(pathIds.contains(2));
    }

    @Test
    public void testShortestPath2() throws Exception {
        DependencyParseTree tree = getTree(
                "1\tBMW\tBMW\tN\tNE\t_|Nom|Sg\t2\tsubj\t_\t_ \n" +
                "2\tist\tsein\tV\tVAFIN\t3|Sg|Pres|Ind\t0\troot\t_\t_ \n" +
                "3\tder\tdie\tART\tART\tDef|Masc|Nom|Sg\t4\tdet\t_\t_ \n" +
                "4\tPartner\tPartner\tN\tNN\tMasc|Nom|Sg\t2\tpred\t_\t_ \n" +
                "5\tvon\tvon\tPREP\tAPPR\t_\t4\tpp\t_\t_ \n" +
                "6\tAudi\tAudi\tN\tNE\t_|_|_\t5\tpn\t_\t_ \n" +
                "7\t;\t;\t$.\t$.\t_\t0\troot\t_\t_ \n" +
                "8\tPorsche\tPorsche\tN\tNE\t_|Nom|Sg\t9\tsubj\t_\t_ \n" +
                "9\tist\tsein\tV\tVAFIN\t3|Sg|Pres|Ind\t0\troot\t_\t_ \n" +
                "10\tkein\tkeine\tART\tPIAT\tMasc|Nom|Sg\t11\tdet\t_\t_ \n" +
                "11\tPartner\tPartner\tN\tNN\tMasc|Nom|Sg\t9\tpred\t_\t_ \n" +
                "12\tvon\tvon\tPREP\tAPPR\t_\t11\tpp\t_\t_ \n" +
                "13\tAudi\tAudi\tN\tNE\t_|_|_\t12\tpn\t_\t_ \n" +
                "14\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n"
        );

        // Expected roots
        List<Node> path = tree.shortestPath(1, 7);

        assertTrue(path.isEmpty());
    }

}
