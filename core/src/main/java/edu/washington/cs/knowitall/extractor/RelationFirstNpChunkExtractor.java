package edu.washington.cs.knowitall.extractor;

import java.util.ArrayList;
import java.util.Collection;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

/**
 * <p> Extracts {@link ChunkedBinaryExtraction} objects by first extracting relations, and then for
 * each relation, extracting a pair of arguments. A {@code RelationFirstNpChunkExtractor} must have:
 * </p> <ul> <li>A relation extractor of type {@code Extractor<NpChunkedSentence,
 * NpChunkedExtraction>}</li> <li>An argument1 extractor of type {@code
 * Extractor<NpChunkedExtraction, NpChunkArgumentExtraction>}</li> <li>An argument2 extractor of
 * type {@code Extractor<NpChunkedExtraction, NpChunkArgumentExtraction>}</li> </ul> <p> Subclasses
 * extending {@code RelationFirstNpChunkExtractor} are responsible for setting the extractors via
 * the {@link RelationFirstNpChunkExtractor#setRelationExtractor(Extractor)}, {@link
 * RelationFirstNpChunkExtractor#setArgument1Extractor(Extractor)}, and {@link
 * RelationFirstNpChunkExtractor#setArgument2Extractor(Extractor)} methods. </p>
 *
 * @author afader
 */
public abstract class RelationFirstNpChunkExtractor
    extends Extractor<ChunkedSentence, ChunkedBinaryExtraction> {

    // Allow unary relations to be extracted.
    protected boolean allowUnary = false;

    protected Extractor<ChunkedSentence, ChunkedRelationExtraction> relExtr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg1Extr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg2Extr;

    /**
     * @return the extractor used to extract relations.
     */
    public Extractor<ChunkedSentence, ChunkedRelationExtraction>
    getRelationExtractor() {
        return relExtr;
    }

    /**
     * @return the extractor used to extract argument1.
     */
    public Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction>
    getArgument1Extractor() {
        return arg1Extr;
    }

    /**
     * @return the extractor used to extract argument2.
     */
    public Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction>
    getArgument2Extractor() {
        return arg2Extr;
    }

    /**
     * Sets the relation extractor.
     */
    public void setRelationExtractor(
        Extractor<ChunkedSentence, ChunkedRelationExtraction> relExtr) {
        this.relExtr = relExtr;
    }

    /**
     * Sets the argument1 extractor.
     */
    public void setArgument1Extractor(
        Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg1Extr) {
        this.arg1Extr = arg1Extr;
    }

    /**
     * Sets the argument2 extractor.
     */
    public void setArgument2Extractor(
        Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg2Extr) {
        this.arg2Extr = arg2Extr;
    }

    public void setAllowUnary(boolean allowUnary) {
        this.allowUnary = allowUnary;
    }


    @Override
    /**
     * Extracts the candidate {@link ChunkedBinaryExtraction} objects from the
     * given {@link ChunkedSentence} source. It extracts relations, and for
     * each relation extracts a pair of candidate arguments. If either argument
     * extractor returns no extractions, then no binary relation is extracted.
     * @throws ExtractorException if unable to extract
     */
    protected Collection<ChunkedBinaryExtraction>
    extractCandidates(ChunkedSentence source) throws ExtractorException {

        Extractor<ChunkedSentence, ChunkedRelationExtraction> relExtr =
            getRelationExtractor();
        Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg1Extr =
            getArgument1Extractor();
        Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg2Extr =
            getArgument2Extractor();

        Iterable<? extends ChunkedRelationExtraction> rels = relExtr.extract(source);
        Collection<ChunkedBinaryExtraction> extrs =
            new ArrayList<ChunkedBinaryExtraction>();
        for (ChunkedRelationExtraction rel : rels) {
            Iterable<? extends ChunkedArgumentExtraction> arg1s =
                arg1Extr.extract(rel);
            Iterable<? extends ChunkedArgumentExtraction> arg2s =
                arg2Extr.extract(rel);

            extrs.addAll(
                ChunkedBinaryExtraction.productOfArgs(rel, arg1s, arg2s, allowUnary));

        }

        return extrs;
    }
}
