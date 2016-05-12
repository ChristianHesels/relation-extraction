package edu.washington.cs.knowitall.normalization;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedExtraction;
import edu.washington.cs.knowitall.sequence.SequenceException;

/**
 * This class represents a field of a {@link edu.washington.cs.knowitall.nlp.extraction.chunking.SpanExtraction} that has been normalized in some way,
 * e.g. morphological normalization. A normalized field is a {@link edu.washington.cs.knowitall.normalization.NormalizedField} with
 * an additional list of attribute tokens and tags.
 */
public class NormalizedArgumentField extends NormalizedField {

    private List<String> attributeTokens;
    private List<String> attributeTags;

    public NormalizedArgumentField(ChunkedExtraction original, String[] tokens,
                                   String[] posTags) throws SequenceException {
        super(original, tokens, posTags);

        this.attributeTags = new ArrayList<>();
        this.attributeTokens = new ArrayList<>();
    }

    public NormalizedArgumentField(ChunkedExtraction original, List<String> tokens,
                                   List<String> posTags) throws SequenceException {
        super(original, tokens, posTags);

        this.attributeTags = new ArrayList<>();
        this.attributeTokens = new ArrayList<>();
    }

    public NormalizedArgumentField(ChunkedExtraction original, String[] tokens,
                                   String[] posTags, List<String> attributeTokens,
                                   List<String> attributeTags) throws SequenceException {
        super(original, tokens, posTags);

        this.attributeTags = attributeTags;
        this.attributeTokens = attributeTokens;
    }

    public NormalizedArgumentField(ChunkedExtraction original, List<String> tokens,
                                   List<String> posTags, List<String> attributeTokens,
                                   List<String> attributeTags) throws SequenceException {
        super(original, tokens, posTags);

        this.attributeTags = attributeTags;
        this.attributeTokens = attributeTokens;
    }

    public List<String> getAttributeTokens() {
        return attributeTokens;
    }

    public void setAttributeTokens(List<String> attributeTokens) {
        this.attributeTokens = attributeTokens;
    }

    public List<String> getAttributeTags() {
        return attributeTags;
    }

    public void setAttributeTags(List<String> attributeTags) {
        this.attributeTags = attributeTags;
    }

    public String toString() {
        return getTokensAsString() + "_(" + Joiner.on(" ").join(getAttributeTokens()) + ")";
    }
}
