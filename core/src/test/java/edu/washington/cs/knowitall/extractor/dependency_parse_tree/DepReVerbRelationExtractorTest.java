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

public class DepReVerbRelationExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private DepReVerbRelationExtractor extractor = new DepReVerbRelationExtractor();

    private void test(String sentence, List<String> expectedExtractions) {
        // Create tree
        List<DependencyParseTree> tree = parser.convert(Arrays.asList(sentence.split("\n")));
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rootNode);

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates1() throws Exception {
        String sent =   "1\tIch\tich\tPRO\tPPER\t1|Sg|_|Nom\t2\tsubj\t_\t_ \n"
                      + "2\tbin\tsein\tV\tVAFIN\t1|Sg|Pres|Ind\t0\troot\t_\t_ \n"
                      + "3\tgelaufen\tlaufen\tV\tVVPP\t_\t2\taux\t_\t_ \n"
                      + "4\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n"
                      + "5\thabe\thaben\tV\tVAFIN\t_|Sg|Pres|_\t2\tkon\t_\t_ \n"
                      + "6\tgegessen\tessen\tV\tVVPP\t_\t5\taux\t_\t_ \n"
                      + "7\tund\tund\tKON\tKON\t_\t5\tkon\t_\t_ \n"
                      + "8\tbin\tsein\tV\tVAFIN\t1|Sg|Pres|Ind\t7\tcj\t_\t_ \n"
                      + "9\tgefahren\tfahren\tV\tVVPP\t_\t8\taux\t_\t_ \n"
                      + "10\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("bin gelaufen");
        expectedExtractions.add("habe gegessen");
        expectedExtractions.add("bin gefahren");

        // Check
        test(sent, expectedExtractions);
    }

    @Test
    public void testExtractCandidates2() throws Exception {
        String sent =   "1\tSiemens\tSiemens\tN\tNE\t_|Nom|Sg\t2\tsubj\t_\t_ \n"
                      + "2\that\thaben\tV\tVAFIN\t3|Sg|Pres|Ind\t0\troot\t_\t_ \n"
                      + "3\theute\theute\tADV\tADV\t_\t2\tadv\t_\t_ \n"
                      + "4\tDaimler\tDaimler\tN\tNE\t_\t2\tobja\t_\t_ \n"
                      + "5\tgekauft\tkaufen\tV\tVVPP\t_\t2\taux\t_\t_ \n"
                      + "6\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n"
                      + "7\tgefeiert\tfeiern\tV\tVVPP\t_\t5\tkon\t_\t_ \n"
                      + "8\tund\tund\tKON\tKON\t_\t7\tkon\t_\t_ \n"
                      + "9\twieder\twieder\tADV\tADV\t_\t10\tadv\t_\t_ \n"
                      + "10\tverkauft\tverkaufen\tV\tVVPP\t_\t8\tcj\t_\t_ \n"
                      + "11\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("hat gekauft");
        expectedExtractions.add("hat gefeiert");
        expectedExtractions.add("hat verkauft");

        // Check
        test(sent, expectedExtractions);
    }

    @Test
    public void testExtractCandidates3() throws Exception {
        String sent =   "1\tIch\tich\tPRO\tPPER\t1|Sg|_|Nom\t2\tsubj\t_\t_ \n"
                      + "2\tspringe\tspringen\tV\tVVFIN\t1|Sg|Pres|_\t0\troot\t_\t_ \n"
                      + "3\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n"
                      + "4\trenne\trennen\tV\tVVFIN\t_|Sg|Pres|_\t2\tkon\t_\t_ \n"
                      + "5\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n"
                      + "6\tlaufe\tlaufen\tV\tVVFIN\t_|Sg|Pres|_\t4\tkon\t_\t_ \n"
                      + "7\tund\tund\tKON\tKON\t_\t6\tkon\t_\t_ \n"
                      + "8\thüpfe\thüpfen\tV\tVVFIN\t_|Sg|Pres|_\t7\tcj\t_\t_ \n"
                      + "9\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("springe");
        expectedExtractions.add("renne");
        expectedExtractions.add("laufe");
        expectedExtractions.add("hüpfe");

        // Check
        test(sent, expectedExtractions);
    }

    @Test
    public void testExtractCandidates4() throws Exception {
        String sent =   "1\tIch\tich\tPRO\tPPER\t1|Sg|_|Nom\t2\tsubj\t_\t_ \n"
                      + "2\thabe\thaben\tV\tVAFIN\t1|Sg|Pres|_\t0\troot\t_\t_ \n"
                      + "3\tgeredet\treden\tV\tVVPP\t_\t2\taux\t_\t_ \n"
                      + "4\tund\tund\tKON\tKON\t_\t2\tkon\t_\t_ \n"
                      + "5\theraus\theraus\tPTKVZ\tPTKVZ\t_\t6\tavz\t_\t_ \n"
                      + "6\tkam\tkommen\tV\tVVFIN\t3|Sg|Past|Ind\t4\tcj\t_\t_ \n"
                      + "7\tdabei\tdabei\tPROAV\tPROAV\t_\t6\tpp\t_\t_ \n"
                      + "8\tnichts\tnichts\tPRO\tPIS\t_|Nom|Sg\t6\tsubj\t_\t_ \n"
                      + "9\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("habe geredet");
        expectedExtractions.add("heraus kam");

        // Check
        test(sent, expectedExtractions);
    }

    @Test
    public void testExtractCandidates5() throws Exception {
        String sent =   "1\tIch\tich\tPRO\tPPER\t1|Sg|_|Nom\t2\tsubj\t_\t_ \n"
                      + "2\thabe\thaben\tV\tVAFIN\t1|Sg|Pres|_\t0\troot\t_\t_ \n"
                      + "3\tnicht\tnicht\tPTKNEG\tPTKNEG\t_\t2\tadv\t_\t_ \n"
                      + "4\tgeredet\treden\tV\tVVPP\t_\t2\taux\t_\t_ \n"
                      + "5\tund\tund\tKON\tKON\t_\t2\tkon\t_\t_ \n"
                      + "6\talle\talle\tPRO\tPIS\t_|Nom|Pl\t7\tsubj\t_\t_ \n"
                      + "7\twollten\twollen\tV\tVMFIN\t3|Pl|Past|_\t5\tcj\t_\t_ \n"
                      + "8\taber\taber\tADV\tADV\t_\t7\tadv\t_\t_ \n"
                      + "9\twas\twas\tPRO\tPIS\t_\t7\tobja\t_\t_ \n"
                      + "10\thören\thören\tV\tVVINF\t_\t7\taux\t_\t_ \n"
                      + "11\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("habe nicht geredet");
        expectedExtractions.add("wollten hören");

        // Check
        test(sent, expectedExtractions);
    }

    @Test
    public void testExtractCandidates6() throws Exception {
        String sent =   "1\tIch\tich\tPRO\tPPER\t1|Sg|_|Nom\t2\tsubj\t_\t_ \n"
                      + "2\thabe\thaben\tV\tVAFIN\t1|Sg|Pres|_\t0\troot\t_\t_ \n"
                      + "3\tes\tes\tPRO\tPPER\t3|Sg|Neut|_\t2\tobja\t_\t_ \n"
                      + "4\tnicht\tnicht\tPTKNEG\tPTKNEG\t_\t2\tadv\t_\t_ \n"
                      + "5\tverhindern\tverhindern\tV\tVVINF\t_\t2\taux\t_\t_ \n"
                      + "6\tkönnen\tkönnen\tV\tVMINF\t_\t2\taux\t_\t_ \n"
                      + "7\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("habe nicht verhindern können");

        // Check
        test(sent, expectedExtractions);
    }



}
