package edu.washington.cs.knowitall.util;


import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.cs.knowitall.extractor.chunking.ReVerbIIExtractor;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.chunking.TreeTaggerSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;


/**
 * Utility class to call ReVerb.
 */
public class ReVerbII extends ReVerb<ChunkedSentence, ChunkedBinaryExtraction> {

    private ReVerbIIExtractor extractor;

    /**
     * Constructor of ReVerb
     */
    public ReVerbII() {
        this(false);
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public ReVerbII(boolean debug) {
        super(debug);
        this.extractor = new ReVerbIIExtractor();
    }

    /**
     * Constructor of ReVerb
     * @param debug enable debug mode?
     * @param minFreq the minimum distinct arguments to be observed in a large collection for the relation to be deemed valid.
     * @param useLexSynConstraints use syntactic and lexical constraints that are part of Reverb?
     * @param combineVerbs combine separated verbs?
     * @param useMorphologyLexicon use a morphology lexicon?
     */
    public ReVerbII(boolean debug, int minFreq, boolean useLexSynConstraints,
                    boolean combineVerbs, boolean useMorphologyLexicon) {
        this.debug = debug;
        this.extractor = new ReVerbIIExtractor(minFreq, useLexSynConstraints);
        this.extractor.setCombineVerbs(combineVerbs);
        this.extractor.setUseMorphologyLexicon(useMorphologyLexicon);
    }

    /**
     * Extract relations from the given sentence.
     * @param sentStr the sentence as string
     * @return the extracted relations
     * @throws java.io.IOException if the tree-tagger model could not be loaded
     */
    public Iterable<ChunkedBinaryExtraction> extractRelations(String sentStr) throws IOException {
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();
        ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentStr);
        return this.extractor.extract(sent);
    }

    /**
     * Extract relations from the given list of sentences.
     * @param sentences a list of sentences
     * @return the extracted relations
     * @throws java.io.IOException if the tree-tagger model could not be loaded
     */
    public Map<String, Iterable<ChunkedBinaryExtraction>> extractRelations(List<String> sentences) throws IOException {
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
            // Parse sentence and extract relations
            ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentence);
            sent2relations.put(sentence, this.extractor.extract(sent));
        }
        if (this.debug) System.out.println("Done.");

        return sent2relations;
    }

    /**
     * Extract relations from the given list of chunked sentences.
     * @param sentences a list of chunked sentences
     * @return the extracted relations
     */
    public List<ChunkedBinaryExtraction> extractRelationsFrom(List<ChunkedSentence> sentences) {
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
            relations.addAll(Lists.newArrayList(this.extractor.extract(sent)));
        }
        if (this.debug) System.out.println("Done.");

        return relations;
    }

    /**
     * Extract relations from a chunked sentence.
     * @param sentence a chunked sentence
     * @return the extracted relations
     */
    public Iterable<ChunkedBinaryExtraction> extractRelationsFrom(ChunkedSentence sentence) {
        return this.extractor.extract(sentence);
    }
}
