package edu.washington.cs.knowitall.normalization;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

/**
 * Represents a {@link ChunkedBinaryExtraction} that has normalized versions of arg1, rel, arg2.
 *
 * @author afader
 */
public class NormalizedBinaryExtraction extends ChunkedBinaryExtraction {

    private NormalizedArgumentField arg1Norm;
    private NormalizedField relNorm;
    private NormalizedArgumentField arg2Norm;

    /**
     * Constructs a new normalized extraction from the given source extraction and its normalized
     * fields.
     */
    public NormalizedBinaryExtraction(ChunkedBinaryExtraction extr,
                                      NormalizedArgumentField arg1Norm,
                                      NormalizedField relNorm,
                                      NormalizedArgumentField arg2Norm) {
        super(extr.getRelation(), extr.getArgument1(), extr.getArgument2());
        this.arg1Norm = arg1Norm;
        this.relNorm = relNorm;
        this.arg2Norm = arg2Norm;
    }

    /**
     * @return normalized argument1
     */
    public NormalizedArgumentField getArgument1Norm() {
        return arg1Norm;
    }

    /**
     * @return normalized relation
     */
    public NormalizedField getRelationNorm() {
        return relNorm;
    }

    /**
     * @return normalized argument2
     */
    public NormalizedArgumentField getArgument2Norm() {
        return arg2Norm;
    }

    @Override
    public String toString() {
        String arg1Str = getArgument1Norm().toString();
        String relStr = getRelationNorm().toString();
        String arg2Str = getArgument2Norm().toString();
        return String.format("(%s, %s, %s)", arg1Str, relStr, arg2Str);
    }

}
