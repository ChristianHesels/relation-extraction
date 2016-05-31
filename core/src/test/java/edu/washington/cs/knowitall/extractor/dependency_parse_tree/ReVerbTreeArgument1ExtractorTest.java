package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.ParZuSentenceParser;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import static org.junit.Assert.assertTrue;


public class ReVerbTreeArgument1ExtractorTest {

    ParZuSentenceParser parser = new ParZuSentenceParser();
    ReVerbTreeArgument1Extractor extractor = new ReVerbTreeArgument1Extractor();

    @Test
    public void testExtractCandidates1() throws Exception {
        // Create tree
        List<DependencyParseTree>
            tree = parser.parseSentence("Das Haus und der Zaun sind gelb gestrichen.");
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Create rel extraction
        List<Integer> ids = new ArrayList<>();
        ids.add(6);
        ids.add(8);
        TreeExtraction rel = new TreeExtraction(rootNode,  ids);

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
        List<DependencyParseTree>
            tree = parser.parseSentence("Die Firma, seit 1996 in Japan angesiedelt, verkauft Spielkonsolen.");
        Node rootNode = tree.get(0).getRootElements().get(0);

        // Create rel extraction
        List<Integer> ids = new ArrayList<>();
        ids.add(10);
        TreeExtraction rel = new TreeExtraction(rootNode,  ids);

        // Extract relations
        Iterable<TreeExtraction> extractions = extractor.extractCandidates(rel);

        // Define expected relations
        List<String> expectedExtractions = new ArrayList<>();
        expectedExtractions.add("Die Firma");

        // Check
        extractions.forEach(extr -> assertTrue(expectedExtractions.contains(extr.toString())));
    }
}
