package edu.washington.cs.knowitall.util;


import com.google.common.collect.Lists;

import opennlp.tools.sentdetect.SentenceDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.nlp.ChunkedDocument;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.TreeTaggerSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class ReVerb {

    private boolean debug;

    public ReVerb() {
        this(false);
    }

    public ReVerb(boolean debug) {
        this.debug = debug;
    }

    public ChunkedDocument process(String content) throws IOException {
        SentenceDetector reader = DefaultObjects.getDefaultSentenceDetector();
        TreeTaggerSentenceChunker chunker = new TreeTaggerSentenceChunker();

        List<ChunkedBinaryExtraction> relations = new ArrayList<>();
        List<ChunkedSentence> sentences = new ArrayList<>();

        if (this.debug) System.out.println("Process sentences ...");
        int n = 0;
        for (String s : reader.sentDetect(content)) {
            ChunkedSentence sent = chunker.chunkSentence(s);

            // Output progress
            if (this.debug && n % 50 == 0) {
                System.out.print(n + " .. ");
            }
            n++;

            ReVerbExtractor reverb = new ReVerbExtractor(0, true, true, false);
            relations.addAll(Lists.newArrayList(reverb.extract(sent)));
            sentences.add(sent);
        }
        if (this.debug) System.out.println("Done.");

        return new ChunkedDocument(sentences, relations);
    }
}
