package edu.washington.cs.knowitall.examples;

import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.util.DepConIE;
import edu.washington.cs.knowitall.util.GermanReVerb;
import edu.washington.cs.knowitall.util.ReVerb;

import java.io.IOException;


public class RelationExtractionExample {

    public static void main(String[] args) throws Exception {
        String sent = "Dep ConIE ist ein Relationsextraktionssystem.";
        if (args.length > 0) {
            sent = args[0];
        }

        extractFromSentence(sent);
    }

    private static void extractFromSentence(String sentStr) throws IOException {
        System.out.println("Sentence: " + sentStr);
        System.out.println("");

        ReVerb reVerb = new ReVerb(false, 20, true);
        Iterable<ChunkedBinaryExtraction> relationsI = reVerb.extractRelationsFromString(sentStr);
        System.out.println("ReVerb:");
        System.out.println(chunkRelationsAsString(relationsI));
        System.out.println("");

        GermanReVerb germanReVerb = new GermanReVerb(false, 20, true, true, true, true, true);
        Iterable<ChunkedBinaryExtraction> relationsII = germanReVerb.extractRelationsFromString(sentStr);
        System.out.println("German ReVerb:");
        System.out.println(chunkRelationsAsString(relationsII));
        System.out.println("");

        DepConIE depConIE = new DepConIE(false, 0, false, false, false);
        Iterable<TreeBinaryExtraction> relationsIII = depConIE.extractRelationsFromString(sentStr);
        System.out.println("Dep ConIE:");
        System.out.println(treeRelationsAsString(relationsIII));
        System.out.println("");
    }

    private static String chunkRelationsAsString(Iterable<ChunkedBinaryExtraction> relations) {
        String str = "";
        for (ChunkedBinaryExtraction extr : relations) {
            str += "Extraction: " + extr + "\n";
        }
        return str;
    }

    private static String treeRelationsAsString(Iterable<TreeBinaryExtraction> relations) {
        String str = "";
        for (TreeBinaryExtraction extr : relations) {
            str += "Extraction: " + extr + "\n";
        }
        return str;
    }
}
