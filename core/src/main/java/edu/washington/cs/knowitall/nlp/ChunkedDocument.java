package edu.washington.cs.knowitall.nlp;

import com.google.common.collect.ImmutableList;

import java.util.List;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

/**
 * Represents a collection of {@link ChunkedSentence} objects and the extracted relations.
 */
public class ChunkedDocument {

    private List<ChunkedSentence> sentences;
    private List<ChunkedBinaryExtraction> relations;

    /**
     * Constructs a new ChunkedDocument with the given sentences and relations.
     *
     * @param sentences the sentences
     * @param relations the relations
     */
    public ChunkedDocument(Iterable<ChunkedSentence> sentences, Iterable<ChunkedBinaryExtraction> relations) {
        this.sentences = ImmutableList.copyOf(sentences);
        this.relations = ImmutableList.copyOf(relations);
    }

    /**
     * @return an immutable view of the sentences in this document
     */
    public List<ChunkedSentence> getSentences() {
        return this.sentences;
    }

    /**
     * @return an immutable view of the relations in this document
     */
    public List<ChunkedBinaryExtraction> getRelations() {
        return this.relations;
    }

}
