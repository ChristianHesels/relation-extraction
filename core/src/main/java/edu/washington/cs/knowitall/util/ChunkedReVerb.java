package edu.washington.cs.knowitall.util;


import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.chunking.TreeTaggerSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;


/**
 * Utility class to call ReVerb.
 */
abstract class ChunkedReVerb extends ReVerb<ChunkedSentence, ChunkedBinaryExtraction> {

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public ChunkedReVerb(boolean debug) {
        super(debug);
    }

    /**
     * Extract relations from the given sentence.
     * @param sentStr the sentence as string
     * @return the extracted relations
     * @throws IOException if the tree-tagger model could not be loaded
     */
    public Iterable<ChunkedBinaryExtraction> extractRelationsFromString(String sentStr) throws IOException {
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();
        ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentStr);
        return extract(sent);
    }

    /**
     * Extract relations from the given list of sentences.
     * @param sentences a list of sentences
     * @return the extracted relations
     * @throws IOException if the tree-tagger model could not be loaded
     */
    public Map<String, Iterable<ChunkedBinaryExtraction>> extractRelationsFromStrings(List<String> sentences) throws IOException {
        Map<String, Iterable<ChunkedBinaryExtraction>> sent2relations = new HashMap<>();
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();

        if (this.debug) System.out.println("Process sentences ...");
        int n = 0;
        for (String sentence : sentences) {
            // Output progress
            if (this.debug && n % 50 == 0) {
                System.out.print(n + " .. ");
            }
            n++;
            // Convert sentence and extract relations
            ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentence);
            sent2relations.put(sentence, extract(sent));
        }
        if (this.debug) System.out.println("Done.");

        return sent2relations;
    }

    /**
     * Extract relations from the given sentence.
     * @param sentStr the sentence as string
     * @return the extracted relations
     * @throws IOException if the tree-tagger model could not be loaded
     */
    public Iterable<ChunkedBinaryExtraction> extractRelationsFromParsedString(String sentStr) throws IOException {
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();
        ChunkedSentence sent = taggerSentenceChunker.convert(sentStr);
        return extract(sent);
    }

    /**
     * Extract relations from the given list of sentences.
     * @param sentences a list of sentences
     * @return the extracted relations
     * @throws IOException if the tree-tagger model could not be loaded
     */
    public Map<String, Iterable<ChunkedBinaryExtraction>> extractRelationsFromParsedStrings(List<String> sentences) throws IOException {
        Map<String, Iterable<ChunkedBinaryExtraction>> sent2relations = new HashMap<>();
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();

        if (this.debug) System.out.println("Process sentences ...");
        int n = 0;
        for (String sentence : sentences) {
            // Output progress
            if (this.debug && n % 50 == 0) {
                System.out.print(n + " .. ");
            }
            n++;
            // Convert sentence and extract relations
            ChunkedSentence sent = taggerSentenceChunker.convert(sentence);
            sent2relations.put(sentence, extract(sent));
        }
        if (this.debug) System.out.println("Done.");

        return sent2relations;
    }

    /**
     * Extract relations from the given list of chunked sentences.
     * @param sentences a list of chunked sentences
     * @return the extracted relations
     */
    public List<ChunkedBinaryExtraction> extractRelations(List<ChunkedSentence> sentences) {
        List<ChunkedBinaryExtraction> relations = new ArrayList<>();

        if (this.debug) System.out.println("Process sentences ...");
        int n = 0;
        for (ChunkedSentence sent : sentences) {
            // Output progress
            if (this.debug && n % 50 == 0) {
                System.out.print(n + " .. ");
            }
            n++;
            // Extract relations
            relations.addAll(Lists.newArrayList(extract(sent)));
        }
        if (this.debug) System.out.println("Done.");

        return relations;
    }

    /**
     * Extract relations from the given chunked sentence.
     * @param sentence a chunked sentence
     * @return the extracted relations
     */
    public Iterable<ChunkedBinaryExtraction> extractRelations(ChunkedSentence sentence) {
        return extract(sentence);
    }

    protected abstract Iterable<ChunkedBinaryExtraction> extract(ChunkedSentence sentence);
}
