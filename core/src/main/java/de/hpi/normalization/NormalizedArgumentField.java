package de.hpi.normalization;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import de.hpi.nlp.extraction.chunking.ChunkedExtraction;
import de.hpi.sequence.SequenceException;

/**
 * This class represents a field of a {@link de.hpi.nlp.extraction.chunking.SpanExtraction} that has been normalized in some way,
 * e.g. morphological normalization. A normalized field is a {@link de.hpi.normalization.NormalizedField} with
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
