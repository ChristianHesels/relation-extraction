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


public class DepConIEExtractorTest {

    private ParZuSentenceParser parser = new ParZuSentenceParser();
    private DepConIEExtractor extractor = new DepConIEExtractor(20, true, false, false);


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
        extractor = new DepConIEExtractor(20, true, true, false);

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
        TreeExtraction arg2 = getExtraction(tree, 6, 9, 10, 11, 12, 13, 14, 15); // der Grundstein des heute weltweit tagierenden Unternehmens
        TreeBinaryExtraction expected = new TreeBinaryExtraction(tree, new Context(ContextType.MAIN_CLAUSE), rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));

        rel = getExtraction(tree, 6, 6); // war
        arg1 = getExtraction(tree, 6, 1, 2, 3, 4, 5); // der Schritt in die Selbständigkeit
        arg2 = getExtraction(tree, 6, 9, 10, 11, 12, 13, 14, 15); // der Grundstein des heute weltweit tagierenden Unternehmens
        expected = new TreeBinaryExtraction(tree, new Context(ContextType.MAIN_CLAUSE), rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));
    }

    @Test
    public void testExtract3() {
        // Als Koordination Einkauf sorge ich zusammen mit meinen Kollegen dafür, dass die Produktion stets mit den optimalen
        // Materialien und Mengen versorgt wird
        String sentence = "1\tAls\tAls\tKOKOM\tKOKOM\t_\t4\tkom\t_\t_ \n" +
                "2\tKoordination\tKoordination\tN\tNN\tMasc|_|Sg\t1\tcj\t_\t_ \n" +
                "3\tEinkauf\tEinkauf\tN\tNN\tMasc|_|Sg\t2\tapp\t_\t_ \n" +
                "4\tsorge\tsorgen\tV\tVVFIN\t1|Sg|Pres|_\t0\troot\t_\t_ \n" +
                "5\tich\tich\tPRO\tPPER\t1|Sg|_|Nom\t4\tsubj\t_\t_ \n" +
                "6\tzusammen\tzusammen\tADV\tADV\t_\t7\tadv\t_\t_ \n" +
                "7\tmit\tmit\tPREP\tAPPR\tDat\t4\tpp\t_\t_ \n" +
                "8\tmeinen\tmeine\tART\tPPOSAT\tMasc|Dat|_\t9\tdet\t_\t_ \n" +
                "9\tKollegen\tKollege\tN\tNN\tMasc|Dat|_\t7\tpn\t_\t_ \n" +
                "10\tdafür\tdafür\tPROAV\tPROAV\t_\t4\tobjp\t_\t_ \n" +
                "11\t,\t,\t$,\t$,\t_\t0\troot\t_\t_ \n" +
                "12\tdass\tdass\tKOUS\tKOUS\t_\t23\tkonj\t_\t_ \n" +
                "13\tdie\tdie\tART\tART\tDef|Fem|Nom|Sg\t14\tdet\t_\t_ \n" +
                "14\tProduktion\tProduktion\tN\tNN\tFem|Nom|Sg\t23\tsubj\t_\t_ \n" +
                "15\tstets\tstets\tADV\tADV\t_\t23\tadv\t_\t_ \n" +
                "16\tmit\tmit\tPREP\tAPPR\tDat\t23\tpp\t_\t_ \n" +
                "17\tden\tdie\tART\tART\tDef|Neut|Dat|Pl\t19\tdet\t_\t_ \n" +
                "18\toptimalen\toptimal\tADJA\tADJA\tPos|Neut|Dat|Pl|_|\t19\tattr\t_\t_ \n" +
                "19\tMaterialien\tMaterial\tN\tNN\tNeut|Dat|Pl\t16\tpn\t_\t_ \n" +
                "20\tund\tund\tKON\tKON\t_\t19\tkon\t_\t_ \n" +
                "21\tMengen\tMenge\tN\tNN\t_|Dat|_\t20\tcj\t_\t_ \n" +
                "22\tversorgt\tversorgen\tV\tVVPP\t_\t23\taux\t_\t_ \n" +
                "23\twird\twerden\tV\tVAFIN\t3|Sg|Pres|Ind\t4\tobjc\t_\t_ \n" +
                "24\t.\t.\t$.\t$.\t_\t0\troot\t_\t_";
        DependencyParseTree tree = getTree(sentence);

        // Execute functionality
        Iterable<TreeBinaryExtraction> actualExtractions = extractor.extract(tree);

        List<String> stringExtractions = new ArrayList<>();
        for (TreeBinaryExtraction e : actualExtractions) {
            stringExtractions.add(e.toString());
        }

        // Expected extractions
        TreeExtraction rel = getExtraction(tree, 23, 22, 23, 15); // stets versorgt wird mit
        rel.setLastNodeId(16);
        TreeExtraction arg1 = getExtraction(tree, 23, 13, 14); // die Produktion
        TreeExtraction arg2 = getExtraction(tree, 23, 17, 18, 19); // den optimalen Materialien
        TreeBinaryExtraction expected = new TreeBinaryExtraction(tree, new Context(ContextType.THAT_CLAUSE, "Attribute: ich sorge dafür"), rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));

        rel = getExtraction(tree, 23, 22, 23, 15); // stets versorgt wird mit
        rel.setLastNodeId(16);
        arg1 = getExtraction(tree, 23, 13, 14); // die Produktion
        arg2 = getExtraction(tree, 23, 21); // Mengen
        expected = new TreeBinaryExtraction(tree, new Context(ContextType.THAT_CLAUSE, "Attribute: ich sorge dafür"), rel, arg1, arg2);

        assertTrue(stringExtractions.contains(expected.toString()));
    }

    @Test
    public void testExtract4() {
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
