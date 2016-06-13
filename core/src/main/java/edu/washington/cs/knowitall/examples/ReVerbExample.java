package edu.washington.cs.knowitall.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.util.ReVerbI;
import edu.washington.cs.knowitall.util.ReVerbII;
import edu.washington.cs.knowitall.util.ReVerbIII;


public class ReVerbExample {

    public static void main(String[] args) throws Exception {
        String sentence = "";
        extractFromSentence(sentence);

//        String fileName = "/home/tanja/Repositories/reverb/core/text/wikipedia.txt";
//        List<String> sentences = readSentences(fileName);
//        extractFromSentences(sentences, fileName);
    }

    private static void extractFromSentence(String sentStr) throws IOException {
        System.out.println(sentStr);
        System.out.println("");

        ReVerbI reVerbI = new ReVerbI(false, 20, true);
        Iterable<ChunkedBinaryExtraction> relationsI = reVerbI.extractRelationsFromString(sentStr);
        System.out.println("ReVerb I:");
        System.out.println(chunkRelationsAsString(relationsI));
        System.out.println("");

        ReVerbII reVerbII = new ReVerbII(false, 20, true, true, true);
        Iterable<ChunkedBinaryExtraction> relationsII = reVerbII.extractRelationsFromString(sentStr);
        System.out.println("ReVerb II:");
        System.out.println(chunkRelationsAsString(relationsII));
        System.out.println("");

        ReVerbIII reVerbIII = new ReVerbIII(false, false);
        Iterable<TreeBinaryExtraction> relations = reVerbIII.extractRelationsFromString(sentStr);
        System.out.println("ReVerb III:");
        System.out.println(treeRelationsAsString(relations));
        System.out.println("");
    }

    private static void extractFromSentences(List<String> sentences, String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName.replace(".txt", ".output.txt"));

        ReVerbI reVerbI = new ReVerbI(false, 20, true);
        ReVerbII reVerbII = new ReVerbII(false, 20, true, true, true);
        ReVerbIII reVerbIII = new ReVerbIII(false, false);

        System.out.print("Process sentences ");
        int i = 0;
        for (String sentStr : sentences) {
            if (i % 10 == 0) {
                System.out.print(i + " .. ");
            }
            i++;

            writer.write(sentStr);
            writer.write("\n");

            Iterable<ChunkedBinaryExtraction> relationsI = reVerbI.extractRelationsFromString(sentStr);
            writer.write("ReVerb I:");
            writer.write("\n");
            writer.write(chunkRelationsAsString(relationsI));
            writer.write("\n");

            Iterable<ChunkedBinaryExtraction> relationsII = reVerbII.extractRelationsFromString(sentStr);
            writer.write("ReVerb II:");
            writer.write("\n");
            writer.write(chunkRelationsAsString(relationsII));
            writer.write("\n");

            Iterable<TreeBinaryExtraction> relations = reVerbIII.extractRelationsFromString(sentStr);
            writer.write("ReVerb III:");
            writer.write("\n");
            writer.write(treeRelationsAsString(relations));
            writer.write("\n");
            writer.write("\n");
        }

        writer.close();
        System.out.print("Done.");
    }

    private static List<String> readSentences(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        List<String> sentences = new ArrayList<>();
        String line = br.readLine();
        while (line != null && !line.isEmpty()) {
            sentences.add(line);
            line = br.readLine();
        }
        br.close();

        return sentences;
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
