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


public class DepConIEArgument1ExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private DepConIEArgument1Extractor extractor = new DepConIEArgument1Extractor();

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

        // Das Haus und der Zaun sind gelb gestrichen.
        String sent =   "1\tDas\tdie\tART\tART\tDef|Neut|Nom|Sg\t2\tdet\t_\t_ \n"
                      + "2\tHaus\tHaus\tN\tNN\tNeut|Nom|Sg\t6\tsubj\t_\t_ \n"
                      + "3\tund\tund\tKON\tKON\t_\t2\tkon\t_\t_ \n"
                      + "4\tder\tdie\tART\tART\tDef|Masc|Nom|Sg\t5\tdet\t_\t_ \n"
                      + "5\tZaun\tZaun\tN\tNN\tMasc|Nom|Sg\t3\tcj\t_\t_ \n"
                      + "6\tsind\tsein\tV\tVAFIN\t_|Pl|Pres|Ind\t0\troot\t_\t_ \n"
                      + "7\tgelb\tgelb\tADV\tADJD\tPos|\t6\tadv\t_\t_ \n"
                      + "8\tgestrichen\tstreichen\tV\tVVPP\t_\t6\taux\t_\t_ \n"
                      + "9\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        TreeExtraction rel = getRelation(sent, 6, 8);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("Das Haus");
        expectedExtractions.add("der Zaun");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates2() throws Exception {
        // Create tree

        // Die Firma, seit 1996 in Japan angesiedelt, verkauft Spielkonsolen.
        String sent =   "1\tDie\tdie\tART\tART\tDef|Fem|Nom|Sg\t2\tdet\t_\t_ \n"
                      + "2\tFirma\tFirma\tN\tNN\tFem|Nom|Sg\t8\tsubj\t_\t_ \n"
                      + "3\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n"
                      + "4\tseit\tseit\tPREP\tAPPR\tDat\t8\tpp\t_\t_ \n"
                      + "5\t1996\t1996\tCARD\tCARD\t_\t4\tpn\t_\t_ \n"
                      + "6\tin\tin\tPREP\tAPPR\t_\t8\tobjp\t_\t_ \n"
                      + "7\tJapan\tJapan\tN\tNE\tNeut|_|Sg\t6\tpn\t_\t_ \n"
                      + "8\tangesiedelt\tansiedeln\tV\tVVPP\t_\t10\tneb\t_\t_ \n"
                      + "9\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n"
                      + "10\tverkauft\tverkaufen\tV\tVVFIN\t_|_|Pres|Ind\t0\troot\t_\t_ \n"
                      + "11\tSpielkonsolen\tSpielkonsole\tN\tNN\tFem|_|Pl\t10\tobja\t_\t_ \n"
                      + "12\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        TreeExtraction rel = getRelation(sent, 10);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("Die Firma");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates3() throws Exception {
        // K&K ist ein Supermarkt
        String sent =   "1\tK\tK\tN\tNN\tNeut|Nom|Sg\t4\tsubj\t_\t_ \n"
                      + "2\t&\t&\tKON\tKON\t_\t1\tkon\t_\t_ \n"
                      + "3\tK\tK\tN\tNN\tNeut|Nom|_\t2\tcj\t_\t_ \n"
                      + "4\tist\tsein\tV\tVAFIN\t3|Sg|Pres|Ind\t0\troot\t_\t_ \n"
                      + "5\tein\teine\tART\tART\tIndef|Masc|Nom|Sg\t6\tdet\t_\t_ \n"
                      + "6\tSupermarkt\tSupermarkt\tN\tNN\tMasc|Nom|Sg\t4\tpred\t_\t_ \n"
                      + "7\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        TreeExtraction rel = getRelation(sent, 4);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("K & K");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates4() throws Exception {
        // Die Raum- und Luftfahrt und die NASA sind beliebt.
        String sent =     "1\tDie\tdie\tART\tART\tDef|_|_|_\t2\tdet\t_\t_ \n"
                        + "2\tRaum-\tRaum-\tTRUNC\tTRUNC\tFem|_|Sg\t8\tsubj\t_\t_ \n"
                        + "3\tund\tund\tKON\tKON\t_\t2\tkon\t_\t_ \n"
                        + "4\tLuftfahrt\tLuftfahrt\tN\tNN\tFem|_|Sg\t3\tcj\t_\t_ \n"
                        + "5\tund\tund\tKON\tKON\t_\t4\tkon\t_\t_ \n"
                        + "6\tdie\tdie\tART\tART\tDef|Fem|_|Sg\t7\tdet\t_\t_ \n"
                        + "7\tNASA\tNASA\tN\tNN\tFem|_|Sg\t5\tcj\t_\t_ \n"
                        + "8\tsind\tsein\tV\tVAFIN\t_|Pl|Pres|Ind\t0\troot\t_\t_ \n"
                        + "9\tbeliebt\tbeliebt\tADV\tADJD\tPos|\t8\tpred\t_\t_ \n"
                        + "10\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        TreeExtraction rel = getRelation(sent, 5);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("Die Raum- und Luftfahrt");
        expectedExtractions.add("die NASA");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }


}
