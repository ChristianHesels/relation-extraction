package edu.washington.cs.knowitall.util;


import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.washington.cs.knowitall.extractor.ReVerbIIIExtractor;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.BitParSentenceParser;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.extraction.TreeBinaryExtraction;


/**
 * Utility class to call ReVerb III.
 * ReVerb III uses dependency parse trees to extract relations from strings.
 */
public class ReVerbIII extends ReVerb<DependencyParseTree, TreeBinaryExtraction> {

    private ReVerbIIIExtractor extractor;

    /**
     * Constructor of ReVerb
     */
    public ReVerbIII() {
        this(false);
    }

    /**
     * Constructor of ReVerb
     * @param debug  enable debug mode?
     */
    public ReVerbIII(boolean debug) {
        super(debug);
        this.extractor = new ReVerbIIIExtractor();
    }

    /**
     * Extract relations from the given sentence.
     * @param sentStr the sentence as string
     * @return the extracted relations
     */
    public Iterable<TreeBinaryExtraction> extractRelations(String sentStr) {
        // Convert sentence into a dependency parse tree
        BitParSentenceParser parser = new BitParSentenceParser();
        List<DependencyParseTree> trees = parser.parseSentence(sentStr);

        // Extract relations
        List<TreeBinaryExtraction> extractions = new ArrayList<>();
        for (DependencyParseTree tree : trees) {
            extractions.addAll(Lists.newArrayList(extractor.extract(tree)));
        }
        return extractions;
    }

    /**
     * Extract relations from the given list of sentences.
     * @param sentences a list of sentences
     * @return the extracted relations
     */
    public Map<String, Iterable<TreeBinaryExtraction>> extractRelations(List<String> sentences) {
        Map<String, Iterable<TreeBinaryExtraction>> sent2relations = new HashMap<>();
        BitParSentenceParser parser = new BitParSentenceParser();
        ReVerbIIIExtractor extractor = new ReVerbIIIExtractor();

        if (this.debug) System.out.println("Process sentences ...");
        int n = 0;
        for (String sentence : sentences) {
            // Output progress
            if (this.debug && n % 50 == 0) {
                System.out.print(n + " .. ");
            }
            n++;
            // parse sentence and extract relations
            List<DependencyParseTree> trees = parser.parseSentence(sentence);
            List<TreeBinaryExtraction> extractions = new ArrayList<>();
            for (DependencyParseTree tree : trees) {
                extractions.addAll(Lists.newArrayList(extractor.extract(tree)));
            }
            sent2relations.put(sentence, extractions);
        }
        if (this.debug) System.out.println("Done.");

        return sent2relations;
    }

    /**
     * Extract relations from the given list of chunked sentences.
     * @param sentences a list of chunked sentences
     * @return the extracted relations
     */
    public List<TreeBinaryExtraction> extractRelationsFrom(List<DependencyParseTree> sentences) {
        List<TreeBinaryExtraction> relations = new ArrayList<>();
        ReVerbIIIExtractor extractor = new ReVerbIIIExtractor();

        if (this.debug) System.out.println("Process sentences ...");
        int n = 0;
        for (DependencyParseTree sent : sentences) {
            // Output progress
            if (this.debug && n % 50 == 0) {
                System.out.print(n + " .. ");
            }
            n++;
            // Extract relations
            relations.addAll(Lists.newArrayList(extractor.extract(sent)));
        }
        if (this.debug) System.out.println("Done.");

        return relations;
    }

    /**
     * Extract relations from the dependency parse tree.
     * @param sentences a dependency parse tree
     * @return the extracted relations
     */
    public Iterable<TreeBinaryExtraction> extractRelationsFrom(DependencyParseTree sentences) {
        ReVerbIIIExtractor extractor = new ReVerbIIIExtractor();
        return extractor.extract(sentences);
    }
}
