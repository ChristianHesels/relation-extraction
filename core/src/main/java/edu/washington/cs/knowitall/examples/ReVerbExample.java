package edu.washington.cs.knowitall.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.chunking.TreeTaggerSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.util.ReVerbI;
import edu.washington.cs.knowitall.util.ReVerbII;
import edu.washington.cs.knowitall.util.ReVerbIII;


public class ReVerbExample {

    public static void main(String[] args) throws Exception {
        String sentence = "Bleiben Patienten auch nach dem Rezept dem fle-xx-Zirkel treu?";
        extractFromSentence(sentence);

        String fileName = "/home/tanja/Repositories/reverb/core/text/sample.txt";
//        List<String> sentences = readSentences(fileName);
//        extractFromSentences(sentences, fileName);
    }

    private static void extractFromSentence(String sentStr) throws IOException {
        System.out.println(sentenceAsString(sentStr));
        System.out.println("");

//        ReVerbI reVerbI = new ReVerbI(false, 20, true);
//        Iterable<ChunkedBinaryExtraction> relationsI = reVerbI.extractRelations(sentStr);
//        System.out.println("ReVerb I:");
//        System.out.println(chunkRelationsAsString(relationsI));
//        System.out.println("");
//
//        ReVerbII reVerbII = new ReVerbII(false, 20, true, true, true);
//        Iterable<ChunkedBinaryExtraction> relationsII = reVerbII.extractRelations(sentStr);
//        System.out.println("ReVerb II:");
//        System.out.println(chunkRelationsAsString(relationsII));
//        System.out.println("");

        ReVerbIII reVerbIII = new ReVerbIII();
        Iterable<TreeBinaryExtraction> relations = reVerbIII.extractRelations(sentStr);
        System.out.println("ReVerb III:");
        System.out.println(treeRelationsAsString(relations));
        System.out.println("");
    }

    private static void extractFromSentences(List<String> sentences, String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName.replace(".txt", ".output.txt"));

        ReVerbI reVerbI = new ReVerbI(false, 20, true);
        ReVerbII reVerbII = new ReVerbII(false, 20, true, true, true);
        ReVerbIII reVerbIII = new ReVerbIII();

        System.out.print("Process sentences ");
        int i = 0;
        for (String sentStr : sentences) {
            if (i % 10 == 0) {
                System.out.print(i + " .. ");
            }
            i++;

            writer.write(sentenceAsString(sentStr));
            writer.write("\n");

            Iterable<ChunkedBinaryExtraction> relationsI = reVerbI.extractRelations(sentStr);
            writer.write("ReVerb I:");
            writer.write(chunkRelationsAsString(relationsI));
            writer.write("\n");

            Iterable<ChunkedBinaryExtraction> relationsII = reVerbII.extractRelations(sentStr);
            writer.write("ReVerb II:");
            writer.write(chunkRelationsAsString(relationsII));
            writer.write("\n");

            Iterable<TreeBinaryExtraction> relations = reVerbIII.extractRelations(sentStr);
            writer.write("ReVerb III:");
            writer.write(treeRelationsAsString(relations));
            writer.write("\n");
        }
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

    private static String sentenceAsString(String sentStr) throws IOException {
        TreeTaggerSentenceChunker chunker = new TreeTaggerSentenceChunker();
        ChunkedSentence sentence = chunker.chunkSentence(sentStr);

        String str = sentence.toString() + "\n";

        for (int i = 0; i < sentence.getLength(); i++) {
            String token = sentence.getToken(i);
            String posTag = sentence.getPosTag(i);
            String chunkTag = sentence.getChunkTag(i);
            str += token + "\t" + posTag + "\t" + chunkTag + "\n";
        }

        return str;
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
