package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.ParZuSentenceParser;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import static org.junit.Assert.assertTrue;

public class ReVerbTreeRelationExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private ReVerbTreeRelationExtractor extractor = new ReVerbTreeRelationExtractor();

    private void test(String sentence, List<String> expectedExtractions) {
        // Create tree
        List<DependencyParseTree> tree = parser.parseSentence(sentence);
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rootNode);

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates1() throws Exception {
        String sent = "Ich bin gelaufen, habe gegessen und bin gefahren.";

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
        String sent = "Siemens hat heute Daimler gekauft, gefeiert und wieder verkauft.";

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
        String sent = "Ich springe, renne, laufe und hüpfe.";

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
        String sent = "Ich habe geredet und heraus kam dabei nichts.";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("habe geredet");
        expectedExtractions.add("heraus kam");

        // Check
        test(sent, expectedExtractions);
    }

    @Test
    public void testExtractCandidates5() throws Exception {
        String sent = "Ich habe nicht geredet und alle wollten aber was hören.";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("habe nicht geredet");
        expectedExtractions.add("wollten hören");

        // Check
        test(sent, expectedExtractions);
    }

    @Test
    public void testExtractCandidates6() throws Exception {
        String sent = "Ich habe es nicht verhindern können.";

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("habe nicht verhindern können");

        // Check
        test(sent, expectedExtractions);
    }



}
