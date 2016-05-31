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

    ParZuSentenceParser parser = new ParZuSentenceParser();
    ReVerbTreeRelationExtractor extractor = new ReVerbTreeRelationExtractor();

    @Test
    public void testExtractCandidates1() throws Exception {
        // Create tree
        List<DependencyParseTree>
            tree = parser.parseSentence("Ich bin gelaufen, habe gegessen und bin gefahren.");
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("bin gelaufen");
        expectedExtractions.add("habe gegessen");
        expectedExtractions.add("bin gefahren");

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rootNode);

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates2() throws Exception {
        // Create tree
        List<DependencyParseTree>
            tree = parser.parseSentence("Siemens hat heute Daimler gekauft, gefeiert und wieder verkauft.");
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("hat gekauft");
        expectedExtractions.add("hat gefeiert");
        expectedExtractions.add("hat verkauft");

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rootNode);

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates3() throws Exception {
        // Create tree
        List<DependencyParseTree>
            tree = parser.parseSentence("Ich springe, renne, laufe und hüpfe.");
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("springe");
        expectedExtractions.add("renne");
        expectedExtractions.add("laufe");
        expectedExtractions.add("hüpfe");

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rootNode);

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

    @Test
    public void testExtractCandidates4() throws Exception {
        // Create tree
        List<DependencyParseTree>
            tree = parser.parseSentence("Ich habe geredet und heraus kam dabei nichts.");
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("habe geredet");
        expectedExtractions.add("heraus kam");

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rootNode);

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }

}
