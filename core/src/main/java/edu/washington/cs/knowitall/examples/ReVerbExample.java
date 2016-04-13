package edu.washington.cs.knowitall.examples;

/* For representing a sentence that is annotated with pos tags and np chunks.*/

import org.apache.commons.lang.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ReVerbIExtractor;
import edu.washington.cs.knowitall.extractor.ReVerbIIExtractor;
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
        String sentence = "Altglas ist ein Rohstoff für viele neue Glasprodukte .";
        extractFromSentence(sentence);

        String fileName = "/home/tanja/Repositories/reverb/core/text/sample.txt";
//        extractFromFile(fileName);
    }


    private static void extractFromSentence(String sentStr) throws IOException {
        // Looks on the classpath for the default model files.
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();
        ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentStr);

        // Prints out the (token, tag, chunk-tag) for the sentence
        System.out.println(sentenceAsString(sent));

        // Prints out extractions from the sentence.
        ReVerbIExtractor reverbI = new ReVerbIExtractor(0, true);
        System.out.println("ReVerb I:");
        System.out.print(extractionAsString(reverbI, sent));
        ReVerbIIExtractor reverbII = new ReVerbIIExtractor(0, true);
        System.out.println("ReVerb II:");
        System.out.print(extractionAsString(reverbII, sent));
    }


    private static void extractFromFile(String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName.replace(".txt", ".output.txt"));
        ChunkedSentenceReader reader = new ChunkedSentenceReader(new FileReader(fileName), DefaultObjects.getDefaultSentenceExtractor());
        BinaryExtractionNormalizer normalizer = new BinaryExtractionNormalizer();

        ReVerbIExtractor reverbI = new ReVerbIExtractor(0, true);
        ReVerbIIExtractor reverbII = new ReVerbIIExtractor(0, true);

        System.out.println("Process sentences ...");
        int n = 0;
        for (ChunkedSentence s : reader.getSentences()) {
            // Output progress
            if (n % 10 == 0) {
                System.out.print(n + " .. ");
            }
            n++;

            // Prints out the (token, tag, chunk-tag) for the sentence
            writer.println(sentenceAsString(s));

            // Prints out extractions from the sentence.
            writer.println("ReVerb I:");
            writer.println(extractionAsString(reverbI, s));
            writer.println("ReVerb II:");
            writer.println(extractionAsString(reverbII, s));

            writer.println();
            writer.println(StringUtils.repeat("-", 100));
            writer.println();
        }
        writer.close();
        System.out.println("Done.");
    }


    private static String sentenceAsString(ChunkedSentence sentence) {
        String str = sentence.toString() + "\n";

        for (int i = 0; i < sentence.getLength(); i++) {
            String token = sentence.getToken(i);
            String posTag = sentence.getPosTag(i);
            String chunkTag = sentence.getChunkTag(i);
            str += token + "\t" + posTag + "\t" + chunkTag + "\n";
        }

        return str;
    }

    private static String extractionAsString(Extractor<ChunkedSentence, ChunkedBinaryExtraction> extractor, ChunkedSentence sentence) {
        String str = "";
        BinaryExtractionNormalizer normalizer = new BinaryExtractionNormalizer();
        for (ChunkedBinaryExtraction extr : extractor.extract(sentence)) {
            str += "Extraction: " + extr + "\n";
            NormalizedBinaryExtraction normExtr = normalizer.normalize(extr);
            str += "Normalized extraction: " + normExtr + "\n";
        }
        return str;
    }
}
