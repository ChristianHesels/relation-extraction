package edu.washington.cs.knowitall.nlp.extraction.chunking;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;

/**
 * A class representing a relation phrase of a relation.
 */
public class ChunkedRelationExtraction extends ChunkedExtraction {

    private ChunkedExtraction subRelation = null;
    private double confidence = .5;

    /**
     * Constructs a new <code>ChunkedRelationExtraction</code> from <code>sent</code>
     *
     * @param sent     the source sentence.
     * @param range    the range of the argument in <code>sent</code>
     */
    public ChunkedRelationExtraction(ChunkedSentence sent, Range range) {
        super(sent, range);
    }

    public ChunkedRelationExtraction(ChunkedSentence sent, Range range, double confidence) {
        super(sent, range);
        this.confidence = confidence;
    }

    public ChunkedRelationExtraction(ChunkedSentence sent, Range range, String string) {
        super(sent, range, string);
    }

    public boolean hasSubRelation() {
        return subRelation != null;
    }

    /**
     * @return the sub relation of this relation
     */
    public ChunkedExtraction getSubRelation() {
        return subRelation;
    }

    public void setSubRelation(ChunkedExtraction subRelation) {
        this.subRelation = subRelation;
    }

    public double getConfidence() {
        return confidence;
    }

    public String toString() {
        String relStr = this.getText();
        if (hasSubRelation()) {
            relStr += " ; " + this.getSubRelation().toString();
        }
        return relStr;
    }
}
