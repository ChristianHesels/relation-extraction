package edu.washington.cs.knowitall.examples;

/* For representing a sentence that is annotated with pos tags and np chunks.*/

import org.apache.commons.lang.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.ChunkedSentenceReader;
import edu.washington.cs.knowitall.nlp.TreeTaggerSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.util.DefaultObjects;

/* String -> ChunkedSentence */
/* The class that is responsible for extraction. */
/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */
/* A class for holding a (arg1, rel, arg2) triple. */

public class ReVerbExample {

    public static void main(String[] args) throws Exception {
        String sentence = "Das generierte Captcha wird anschließend auf unserem Server gespeichert , damit Sie die Möglichkeit haben es herunterzuladen oder den Link auf eine         andere Seite zu kopieren . ";
        extractFromSentence(sentence);

        String fileName = "/home/tanja/Repositories/reverb/core/text/wikipedia.txt";
//        extractFromFile(fileName);
    }


    private static void extractFromSentence(String sentStr) throws IOException {
        // Looks on the classpath for the default model files.
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();
        ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentStr);

        // Prints out the (token, tag, chunk-tag) for the sentence
        System.out.println(sentStr);
        System.out.println();
        for (int i = 0; i < sent.getLength(); i++) {
            String token = sent.getToken(i);
            String posTag = sent.getPosTag(i);
            String chunkTag = sent.getChunkTag(i);
            System.out.println(token + " " + posTag + " " + chunkTag);
        }

        // Prints out extractions from the sentence.
        ReVerbExtractor reverb = new ReVerbExtractor(0, true, true, false);
//        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
        for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {

//            double conf = confFunc.getConf(extr);
            System.out.println();
            System.out.println("Arg1=" + extr.getArgument1());
            if (extr.getRelation().hasSubExtraction()) {
                System.out.println("Rel=" + extr.getRelation() + "; " + extr.getRelation().getSubExtraction());
            } else {
                System.out.println("Rel=" + extr.getRelation());
            }
            System.out.println("Arg2=" + extr.getArgument2());
//            System.out.println("Conf=" + conf);
        }
    }

    private static void extractFromFile(String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName.replace(".txt", ".output.txt"));
        ChunkedSentenceReader reader = new ChunkedSentenceReader(new FileReader(fileName), DefaultObjects.getDefaultSentenceExtractor());

        System.out.println("Process sentences ...");
        int n = 0;
        for (ChunkedSentence s : reader.getSentences()) {
            // Output progress
            if (n % 10 == 0) {
                System.out.print(n + " .. ");
            }
            n++;

            // Prints out the (token, tag, chunk-tag) for the sentence
            writer.println(s);
            writer.println();
            for (int i = 0; i < s.getLength(); i++) {
                String token = s.getToken(i);
                String posTag = s.getPosTag(i);
                String chunkTag = s.getChunkTag(i);
                writer.println(token + "\t" + posTag + "\t" + chunkTag);
            }

            // Prints out extractions from the sentence.
            ReVerbExtractor reverb = new ReVerbExtractor(0, true, true, false);
//            ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
            for (ChunkedBinaryExtraction extr : reverb.extract(s)) {
//                double conf = confFunc.getConf(extr);
                writer.println();
                writer.println("Arg1=" + extr.getArgument1());
                if (extr.getRelation().hasSubExtraction()) {
                    writer.println("Rel=" + extr.getRelation() + "; " + extr.getRelation().getSubExtraction());
                } else {
                    writer.println("Rel=" + extr.getRelation());
                }
                writer.println("Arg2=" + extr.getArgument2());
//                System.out.println("Conf=" + conf);
            }
            writer.println();
            writer.println(StringUtils.repeat("-", 100));
            writer.println();
        }
        writer.close();
        System.out.println("Done.");
    }
}
