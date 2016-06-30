package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import com.google.common.collect.Iterables;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.ParZuSentenceParser;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.Context;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.ContextType;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class DepReVerbExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private DepReVerbExtractor extractor = new DepReVerbExtractor(true, false);


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
        extractor = new DepReVerbExtractor(true, true);

        String sentence = "1\tWir\twir\tPRO\tPPER\t1|Pl|_|Nom\t2\tsubj\t_\t_ \n" +
                            "2\thaben\thaben\tV\tVAFIN\t1|Pl|Pres|_\t0\troot\t_\t_ \n" +
                            "3\tein\teine\tART\tART\tIndef|Neut|_|Sg\t7\tdet\t_\t_ \n" +
                            "4\tinnovatives\tinnovativ\tADJA\tADJA\tPos|Neut|_|Sg|St|\t7\tattr\t_\t_ \n" +
                            "5\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n" +
                            "6\tneues\tneu\tADJA\tADJA\tPos|Neut|_|Sg|St|\t4\tkon\t_\t_ \n" +
                            "7\tSystem\tSystem\tN\tNN\tNeut|_|Sg\t2\tobja\t_\t_ \n" +
                            "8\tentwickelt\tentwickeln\tV\tVVPP\t_\t2\taux\t_\t_ \n" +
                            "9\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";
        DependencyParseTree tree = getTree(sentence);

        // Execute functionality
        Iterable<TreeBinaryExtraction> actualExtractions = extractor.extract(tree);

        List<String> stringExtractions = new ArrayList<>();
        for (TreeBinaryExtraction e : actualExtractions) {
            stringExtractions.add(e.toString());
        }

        // Expected extractions
        TreeExtraction rel = getExtraction(tree, 2, 2, 8); // haben entwickelt
        TreeExtraction arg1 = getExtraction(tree, 2, 1); // wir
        TreeExtraction arg2 = getExtraction(tree, 2, 3, 4, 6, 7); // ein innovatives, neues System
        TreeBinaryExtraction expected = new TreeBinaryExtraction(tree, new Context(ContextType.MAIN_CLAUSE), rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));
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
        TreeBinaryExtraction expected = new TreeBinaryExtraction(tree, new Context(ContextType.MAIN_CLAUSE), rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));

        rel = getExtraction(tree, 6, 6); // war
        arg1 = getExtraction(tree, 6, 1, 2, 3, 4, 5); // der Schritt in die Selbständigkeit
        arg2 = getExtraction(tree, 6, 9, 10, 11, 13, 14, 15); // der Grundstein des heute weltweit tagierenden Unternehmens
        expected = new TreeBinaryExtraction(tree, new Context(ContextType.MAIN_CLAUSE), rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));
    }

    @Test
    public void testExtract3() {
        // Meine Handschrift ist nicht die beste, aber Gunnars kann ich überhaupt nicht lesen.
        String sentence = "1\tMeine\tmeine\tART\tPPOSAT\tFem|Nom|Sg\t2\tdet\t_\t_ \n" +
                            "2\tHandschrift\tHandschrift\tN\tNN\tFem|Nom|Sg\t3\tsubj\t_\t_ \n" +
                            "3\tist\tsein\tV\tVAFIN\t3|Sg|Pres|Ind\t0\troot\t_\t_ \n" +
                            "4\tnicht\tnicht\tPTKNEG\tPTKNEG\t_\t3\tadv\t_\t_ \n" +
                            "5\tdie\tdie\tART\tART\tDef|_|_|_\t6\tdet\t_\t_ \n" +
                            "6\tbeste\tgut\tADJA\tADJA\t_|_|_\t3\tpred\t_\t_ \n" +
                            "7\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n" +
                            "8\taber\taber\tKON\tKON\t_\t3\tkon\t_\t_ \n" +
                            "9\tGunnars\tGunnars\tN\tNE\t_\t10\tobja\t_\t_ \n" +
                            "10\tkann\tkönnen\tV\tVMFIN\t1|Sg|Pres|Ind\t8\tcj\t_\t_ \n" +
                            "11\tich\tich\tPRO\tPPER\t1|Sg|_|Nom\t10\tsubj\t_\t_ \n" +
                            "12\tüberhaupt\tüberhaupt\tADV\tADV\t_\t13\tadv\t_\t_ \n" +
                            "13\tnicht\tnicht\tPTKNEG\tPTKNEG\t_\t10\tadv\t_\t_ \n" +
                            "14\tlesen\tlesen\tV\tVVINF\t_\t10\taux\t_\t_ \n" +
                            "15\t.\t.\t$.\t$.\t_\t0\troot\t_\t_ \n";
        DependencyParseTree tree = getTree(sentence);

        // Execute functionality
        Iterable<TreeBinaryExtraction> actualExtractions = extractor.extract(tree);

        assertTrue(Iterables.isEmpty(actualExtractions));
    }


}
