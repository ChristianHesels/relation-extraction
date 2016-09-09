package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import com.google.common.collect.Iterables;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.ParZuSentenceParser;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class DepConIEArgument2ExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private DepConIEArgument2Extractor extractor = new DepConIEArgument2Extractor();


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

        TreeExtraction rel = getRelation(sent, 4, 11, 7);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("Estland");
        expectedExtractions.add("Lettland");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }


    @Test
    public void testExtractCandidates3() throws Exception {
        // So verkleidete der Schermbecker Unternehmer zum Beispiel die Dortmunder Konzerthalle oder aber auch das Stadion in Mönchengladbach und den Königspalast in Krefeld, um hier nur einige Beispiel zu nennen.
        String sent =   "1\tSo\tso\tADV\tADV\t_\t2\tadv\t_\t_ \n"
                        + "2\tverkleidete\tverkleiden\tV\tVVFIN\t3|Sg|Past|_\t0\troot\t_\t_ \n"
                        + "3\tder\tdie\tART\tART\tDef|Masc|Nom|Sg\t4\tdet\t_\t_ \n"
                        + "4\tSchermbecker\tSchermbecker\tN\tNN\tMasc|Nom|Sg\t2\tsubj\t_\t_ \n"
                        + "5\tUnternehmer\tUnternehmer\tN\tNN\tMasc|Nom|_\t4\tapp\t_\t_ \n"
                        + "6\tzum\tzu\tPREP\tAPPRART\tDat\t2\tpp\t_\t_ \n"
                        + "7\tBeispiel\tBeispiel\tN\tNN\tNeut|Dat|Sg\t6\tpn\t_\t_ \n"
                        + "8\tdie\tdie\tART\tART\tDef|Fem|Acc|Sg\t10\tdet\t_\t_ \n"
                        + "9\tDortmunder\tDortmunder\tADJA\tADJA\tPos|Fem|Acc|Sg|_|\t10\tattr\t_\t_ \n"
                        + "10\tKonzerthalle\tKonzerthalle\tN\tNN\tFem|Acc|Sg\t2\tobja\t_\t_ \n"
                        + "11\toder\toder\tKON\tKON\t_\t10\tkon\t_\t_ \n"
                        + "12\taber\taber\tADV\tADV\t_\t15\tadv\t_\t_ \n"
                        + "13\tauch\tauch\tADV\tADV\t_\t15\tadv\t_\t_ \n"
                        + "14\tdas\tdie\tART\tART\tDef|Neut|Acc|Sg\t15\tdet\t_\t_ \n"
                        + "15\tStadion\tStadion\tN\tNN\tNeut|Acc|Sg\t11\tcj\t_\t_ \n"
                        + "16\tin\tin\tPREP\tAPPR\t_\t15\tpp\t_\t_ \n"
                        + "17\tMönchengladbach\tMönchengladbach\tN\tNN\tMasc|_|Sg\t16\tpn\t_\t_ \n"
                        + "18\tund\tund\tKON\tKON\t_\t15\tkon\t_\t_ \n"
                        + "19\tden\tdie\tART\tART\tDef|Masc|Acc|Sg\t20\tdet\t_\t_ \n"
                        + "20\tKönigspalast\tKönigspalast\tN\tNN\tMasc|Acc|Sg\t18\tcj\t_\t_ \n"
                        + "21\tin\tin\tPREP\tAPPR\t_\t2\tpp\t_\t_ \n"
                        + "22\tKrefeld\tKrefeld\tN\tNE\tNeut|_|Sg\t21\tpn\t_\t_ \n"
                        + "23\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        TreeExtraction rel = getRelation(sent, 2);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Check
        assertTrue(Iterables.isEmpty(extractions));
    }

}
