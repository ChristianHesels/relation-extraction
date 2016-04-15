package edu.washington.cs.knowitall.util;


import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ReVerbIExtractor;
import edu.washington.cs.knowitall.extractor.ReVerbIIExtractor;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.TreeTaggerSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;


/**
 * Utility class to call ReVerb.
 */
public class ReVerb {

    public enum ReVerbSystem {
        ReVerbI, ReVerbII
    }

    private boolean debug = false;
    private Extractor<ChunkedSentence, ChunkedBinaryExtraction> reverbExtractor;

    /**
     * Constructor of ReVerb
     * @param system the system you want to use
     */
    public ReVerb(ReVerbSystem system) {
        if (system == ReVerbSystem.ReVerbI) {
            this.reverbExtractor = new ReVerbIExtractor();
        } else if (system == ReVerbSystem.ReVerbII) {
            this.reverbExtractor = new ReVerbIIExtractor();
        } else {
            throw new IllegalArgumentException("The given system is not supported!");
        }
    }

    /**
     * Constructor of ReVerb
     * @param system the system you want to use
     * @param debug  enable debug mode?
     */
    public ReVerb(ReVerbSystem system, boolean debug) {
        if (system == ReVerbSystem.ReVerbI) {
            this.reverbExtractor = new ReVerbIExtractor();
        } else if (system == ReVerbSystem.ReVerbII) {
            this.reverbExtractor = new ReVerbIIExtractor();
        } else {
            throw new IllegalArgumentException("The given system is not supported!");
        }
        this.debug = debug;
    }

    /**
     * Constructor of ReVerb
     * @param system                the system you want to use
     * @param debug                 enable debug mode?
     * @param minFreq               the minimum distinct arguments to be observed in a large
     *                              collection for the relation to be deemed valid.
     * @param useLexSynConstraints  use syntactic and lexical constraints that are part of Reverb?
     * @param allowUnary            allow unary relations in the output?
     * @param mergeOverlapRels      merge overlapping relation?
     * @param combineVerbs          combine separated verbs?
     * @param useMorphologyLexicon  use a morphology lexicon?
     */
    public ReVerb(ReVerbSystem system,
                  boolean debug,
                  int minFreq,
                  boolean useLexSynConstraints,
                  boolean allowUnary,
                  boolean mergeOverlapRels,
                  boolean combineVerbs,
                  boolean useMorphologyLexicon) {
        if (system == ReVerbSystem.ReVerbI) {
            ReVerbIExtractor reverbExtractor = new ReVerbIExtractor(minFreq, useLexSynConstraints);
            reverbExtractor.setAllowUnary(allowUnary);
            reverbExtractor.setMergeOverlapRels(mergeOverlapRels);
            this.reverbExtractor = reverbExtractor;
        } else if (system == ReVerbSystem.ReVerbII) {
            ReVerbIIExtractor reverbExtractor = new ReVerbIIExtractor(minFreq, useLexSynConstraints);
            reverbExtractor.setAllowUnary(allowUnary);
            reverbExtractor.setCombineVerbs(combineVerbs);
            reverbExtractor.setUseMorphologyLexicon(useMorphologyLexicon);
            this.reverbExtractor = reverbExtractor;
        } else {
            throw new IllegalArgumentException("The given system is not supported!");
        }
        this.debug = debug;
    }

    /**
     * Extract relations from the given sentence.
     * @param sentStr the sentence as string
     * @return the extracted relations
     * @throws IOException if the tree-tagger model could not be loaded
     */
    public Iterable<ChunkedBinaryExtraction> extractRelations(String sentStr) throws IOException {
        TreeTaggerSentenceChunker taggerSentenceChunker = new TreeTaggerSentenceChunker();
        ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentStr);

        return this.reverbExtractor.extract(sent);
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
            ChunkedSentence sent = taggerSentenceChunker.chunkSentence(sentence);

            // Output progress
            if (this.debug && n % 50 == 0) {
                System.out.print(n + " .. ");
            }
            n++;

            sent2relations.put(sentence, this.reverbExtractor.extract(sent));
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

            relations.addAll(Lists.newArrayList(this.reverbExtractor.extract(sent)));
        }

        if (this.debug) System.out.println("Done.");

        return relations;
    }
}
