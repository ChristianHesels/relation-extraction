package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.ParZuSentenceParser;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import static org.junit.Assert.assertTrue;


public class ReVerbIIIExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private ReVerbIIIExtractor extractor = new ReVerbIIIExtractor(true);


    private DependencyParseTree getTree(String sentence) {
        List<DependencyParseTree> tree = parser.convert(Arrays.asList(sentence.split("\n")));
        return tree.get(0);
    }

    private TreeExtraction getExtraction(DependencyParseTree tree, Integer rootId, Integer... ids) {
        List<Integer> rootIds = new ArrayList<>();
        rootIds.add(rootId);
        Node rootNode = tree.find(rootIds).get(0);

        // Create rel extraction
        return new TreeExtraction(rootNode, Arrays.asList(ids));
    }

    @Test
    public void testExtract1() {
        // "meine Handschrift ist nicht die beste , aber Gunnars kann ich überhaupt nicht lesen"

    }

    @Test
    public void testExtract2() {
        // Der Schritt in die Selbständigkeit war und ist der Grundstein des heute weltweit tagierenden Unternehmens.
        String sentence = "1\tDer\tdie\tART\tART\tDef|Masc|Nom|Sg\t2\tdet\t_\t_ \n"
                          + "2\tSchritt\tSchritt\tN\tNN\tMasc|Nom|Sg\t6\tsubj\t_\t_ \n"
                          + "3\tin\tin\tPREP\tAPPR\tAcc\t2\tpp\t_\t_ \n"
                          + "4\tdie\tdie\tART\tART\tDef|Fem|Acc|Sg\t5\tdet\t_\t_ \n"
                          + "5\tSelbstständigkeit\tSelbstständigkeit\tN\tNN\tFem|Acc|Sg\t3\tpn\t_\t_ \n"
                          + "6\twar\tsein\tV\tVAFIN\t3|Sg|Past|Ind\t0\troot\t_\t_ \n"
                          + "7\tund\tund\tKON\tKON\t_\t6\tkon\t_\t_ \n"
                          + "8\tist\tsein\tV\tVAFIN\t3|Sg|Pres|Ind\t7\tcj\t_\t_ \n"
                          + "9\tder\tdie\tART\tART\tDef|Masc|Nom|Sg\t10\tdet\t_\t_ \n"
                          + "10\tGrundstein\tGrundstein\tN\tNN\tMasc|Nom|Sg\t8\tpred\t_\t_ \n"
                          + "11\tdes\tdie\tART\tART\tDef|Neut|Gen|Sg\t15\tdet\t_\t_ \n"
                          + "12\theute\theute\tADV\tADV\t_\t14\tadv\t_\t_ \n"
                          + "13\tweltweit\tweltweit\tADV\tADJD\tPos|\t14\tadv\t_\t_ \n"
                          + "14\tagierenden\tagierend\tADJA\tADJA\tPos|Neut|Gen|Sg|_|<PPRES\t15\tattr\t_\t_ \n"
                          + "15\tUnternehmens\tUnternehmen\tN\tNN\tNeut|Gen|Sg\t10\tgmod\t_\t_ \n"
                          + "16\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";
        DependencyParseTree tree = getTree(sentence);

        // Execute functionality
        Iterable<TreeBinaryExtraction> actualExtractions = extractor.extract(tree);

        List<String> stringExtractions = new ArrayList<>();
        for (TreeBinaryExtraction e : actualExtractions) {
            stringExtractions.add(e.toString());
        }

        // Expected extractions
        TreeExtraction rel = getExtraction(tree, 6, 8); // ist
        TreeExtraction arg1 = getExtraction(tree, 6, 1, 2, 3, 4, 5); // der Schritt in die Selbständigkeit
        TreeExtraction arg2 = getExtraction(tree, 6, 9, 10, 11, 13, 14, 15); // der Grundstein des heute weltweit tagierenden Unternehmens
        TreeBinaryExtraction expected = new TreeBinaryExtraction(tree, rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));

        rel = getExtraction(tree, 6, 6); // war
        arg1 = getExtraction(tree, 6, 1, 2, 3, 4, 5); // der Schritt in die Selbständigkeit
        arg2 = getExtraction(tree, 6, 9, 10, 11, 13, 14, 15); // der Grundstein des heute weltweit tagierenden Unternehmens
        expected = new TreeBinaryExtraction(tree, rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));
    }
}
