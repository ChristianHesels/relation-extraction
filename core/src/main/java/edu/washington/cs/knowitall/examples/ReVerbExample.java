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
import edu.washington.cs.knowitall.normalization.BinaryExtractionNormalizer;
import edu.washington.cs.knowitall.normalization.NormalizedBinaryExtraction;
import edu.washington.cs.knowitall.util.DefaultObjects;

/* String -> ChunkedSentence */
/* The class that is responsible for extraction. */
/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */
/* A class for holding a (arg1, rel, arg2) triple. */

public class ReVerbExample {

    public static void main(String[] args) throws Exception {
        String sentence = "Sowohl amerikanische Farmer als auch afrikanische Kleinbauern bauen Baumwolle an .";
        extractFromSentence(sentence);

        String fileName = "/home/tanja/Repositories/reverb/core/text/sentences.txt";
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
        BinaryExtractionNormalizer normalizer = new BinaryExtractionNormalizer();
//        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
        for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {

//            double conf = confFunc.getConf(extr);
            System.out.println();
            System.out.println(extr);
//            System.out.println("Conf=" + conf);

            NormalizedBinaryExtraction normExtr = normalizer.normalize(extr);
            System.out.println();
            System.out.println(normExtr);
        }
    }

    private static void extractFromFile(String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName.replace(".txt", ".output.txt"));
        ChunkedSentenceReader reader = new ChunkedSentenceReader(new FileReader(fileName), DefaultObjects.getDefaultSentenceExtractor());
        BinaryExtractionNormalizer normalizer = new BinaryExtractionNormalizer();

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
                writer.println(extr);
//                System.out.println("Conf=" + conf);

                NormalizedBinaryExtraction normExtr = normalizer.normalize(extr);
                writer.println();
                writer.println(normExtr);
            }
            writer.println();
            writer.println(StringUtils.repeat("-", 100));
            writer.println();
        }
        writer.close();
        System.out.println("Done.");
    }
}
