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
        // Viele Länder der EM lehnen Europa ab.
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

    @Test
    public void testExtractCandidates2() throws Exception {
        // Im nächsten Schritt wird die RELEX-Nutzung auf Estland und Lettland ausgedehnt.
        String sent =   "1\tIm\tin\tPREP\tAPPRART\tDat\t4\tpp\t_\t_ \n"
                      + "2\tnächsten\tnah\tADJA\tADJA\t_|Masc|Dat|Sg|Wk|\t3\tattr\t_\t_ \n"
                      + "3\tSchritt\tSchritt\tN\tNN\tMasc|Dat|Sg\t1\tpn\t_\t_ \n"
                      + "4\twird\twerden\tV\tVAFIN\t3|Sg|Pres|Ind\t0\troot\t_\t_ \n"
                      + "5\tdie\tdie\tART\tART\tDef|Fem|Nom|Sg\t6\tdet\t_\t_ \n"
                      + "6\tRELEX-Nutzung\tRELEX-Nutzung\tN\tNN\tFem|Nom|Sg\t4\tsubj\t_\t_ \n"
                      + "7\tauf\tauf\tPREP\tAPPR\t_\t4\tobjp\t_\t_ \n"
                      + "8\tEstland\tEstland\tN\tNE\tNeut|_|Sg\t7\tpn\t_\t_ \n"
                      + "9\tund\tund\tKON\tKON\t_\t8\tkon\t_\t_ \n"
                      + "10\tLettland\tLettland\tN\tNE\tNeut|_|Sg\t9\tcj\t_\t_ \n"
                      + "11\tausgedehnt\tausdehnen\tV\tVVPP\t_\t4\taux\t_\t_ \n"
                      + "12\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        TreeExtraction rel = getRelation(sent, 4, 11);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("auf Estland");
        expectedExtractions.add("auf Lettland");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }
}
