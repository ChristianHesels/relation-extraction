package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.ParZuSentenceParser;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import static org.junit.Assert.assertTrue;


public class ReVerbTreeArgument2ExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private ReVerbTreeArgument2Extractor extractor = new ReVerbTreeArgument2Extractor();


    private TreeExtraction getRelation(String sentence, Integer... ids) {
        List<DependencyParseTree>
            tree = parser.convert(Arrays.asList(sentence.split("\n")));
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Create rel extraction
        return new TreeExtraction(rootNode, Arrays.asList(ids));
    }


    @Test
    public void testExtractCandidates1() throws Exception {
        // Create tree
        String sent =     "1\tViele\tviele\tART\tPIDAT\t_|Nom|Pl\t2\tdet\t_\t_ \n"
                        + "2\tLänder\tLand\tN\tNN\t_|Nom|Pl\t5\tsubj\t_\t_ \n"
                        + "3\tder\tdie\tART\tART\tDef|Fem|Gen|Sg\t4\tdet\t_\t_ \n"
                        + "4\tEM\tEM\tN\tNN\tFem|Gen|Sg\t2\tgmod\t_\t_ \n"
                        + "5\tlehnen\tlehnen\tV\tVVFIN\t3|Pl|Pres|_\t0\troot\t_\t_ \n"
                        + "6\tEuropa\tEuropa\tN\tNE\t_|_|Sg\t5\tobja\t_\t_ \n"
                        + "7\tab\tab\tPTKVZ\tPTKVZ\t_\t5\tavz\t_\t_ \n"
                        + "8\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        TreeExtraction rel = getRelation(sent, 5, 7);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("Europa");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }
}
