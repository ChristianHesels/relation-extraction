package de.hpi;

import de.hpi.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import de.hpi.util.DepConIE;

public class main {
    public static void main(String[] args) {
        String sentence = "Ich hoffe das funktioniert.";
        DepConIE depConIE = new DepConIE(false, 0, false, false, false);
        Iterable<TreeBinaryExtraction> relations = depConIE.extractRelationsFromString(sentence);

        StringBuilder str = new StringBuilder();
        for (TreeBinaryExtraction extr : relations) {
            str.append("Extraction: ").append(extr).append("\n");
        }
        System.out.println(str);
    }
}
