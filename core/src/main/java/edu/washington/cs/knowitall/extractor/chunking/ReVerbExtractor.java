package edu.washington.cs.knowitall.extractor.chunking;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.extractor.chunking.mapper.ReVerbArgument1Mappers;
import edu.washington.cs.knowitall.extractor.chunking.mapper.ReVerbArgument2Mappers;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedRelationExtraction;

import java.util.ArrayList;
import java.util.Collection;


public class ReVerbExtractor extends Extractor<ChunkedSentence, ChunkedBinaryExtraction> {

    protected Extractor<ChunkedSentence, ChunkedRelationExtraction> relExtr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg1Extr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg2Extr;

    private boolean allowUnary = false;
    private static final boolean mergeOverlapRels = true;
    private static final boolean combineVerbs = false;
    private static final boolean useMorphologyLexicon = false;
    private static final boolean reflexiveVerbs = false;

    public ReVerbExtractor() {
        this.relExtr = new ReVerbRelationExtractor();

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(useMorphologyLexicon, reflexiveVerbs));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers(reflexiveVerbs));
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     */
    public ReVerbExtractor(int minFreq, boolean useLexSynConstraints) {
        this.relExtr = new ReVerbRelationExtractor(minFreq, useLexSynConstraints, mergeOverlapRels, combineVerbs, reflexiveVerbs);

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(useMorphologyLexicon, reflexiveVerbs));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers(reflexiveVerbs));
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extractCandidates(ChunkedSentence source)
        throws ExtractorException {

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

    public void setAllowUnary(boolean allowUnary) {
        this.allowUnary = allowUnary;
    }

}

