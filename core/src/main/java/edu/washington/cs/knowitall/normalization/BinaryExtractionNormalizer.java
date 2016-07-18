package edu.washington.cs.knowitall.normalization;

import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedRelationExtraction;

/**
 * A class for normalizing {@link ChunkedBinaryExtraction} objects. This class uses {@link
 * ArgumentNormalizer} to normalize arg1 and arg2, and {@link VerbalRelationNormalizer} to normalize
 * rel.
 *
 * @author afader
 */
public class BinaryExtractionNormalizer {

    private ArgumentNormalizer argNormalizer;
    private VerbalRelationNormalizer relNormalizer;

    /**
     * Constructs a new normalizer object.
     */
    public BinaryExtractionNormalizer() {
        this.argNormalizer = new ArgumentNormalizer();
        this.relNormalizer = new VerbalRelationNormalizer(false, false, false);
    }

    /**
     * Normalizes the given argument
     *
     * @return the normalized argument
     */
    public NormalizedArgumentField normalizeArgument(ChunkedArgumentExtraction arg) {
        return argNormalizer.normalizeField(arg);
    }

    /**
     * Normalizes the given relation phrase
     *
     * @return the normalized phrase
     */
    public NormalizedField normalizeRelation(ChunkedRelationExtraction rel) {
        return relNormalizer.normalizeField(rel);
    }

    /**
     * Normalizes the given extraction
     *
     * @return the normalized extraction
     */
    public NormalizedBinaryExtraction normalize(ChunkedBinaryExtraction extr) {
        NormalizedArgumentField arg1Norm = normalizeArgument(extr.getArgument1());
        NormalizedArgumentField arg2Norm = normalizeArgument(extr.getArgument2());
        NormalizedField relNorm = normalizeRelation(extr.getRelation());
        return new NormalizedBinaryExtraction(extr, arg1Norm, relNorm, arg2Norm);
    }

}
