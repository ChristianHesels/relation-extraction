package edu.washington.cs.knowitall.extractor;

import java.util.ArrayList;
import java.util.Collection;

import edu.washington.cs.knowitall.extractor.mapper.ReVerbArgument1Mappers;
import edu.washington.cs.knowitall.extractor.mapper.ReVerbArgument2Mappers;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;


public class ReVerbIExtractor extends Extractor<ChunkedSentence, ChunkedBinaryExtraction> {

    protected Extractor<ChunkedSentence, ChunkedRelationExtraction> relExtr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg1Extr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg2Extr;

    private boolean allowUnary = false;
    private boolean mergeOverlapRels = true;
    private boolean combineVerbs = false;
    private boolean useMorphologyLexicon = false;

    public ReVerbIExtractor() {
        this.relExtr = new ReVerbRelationExtractor();

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(useMorphologyLexicon));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers());
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     */
    public ReVerbIExtractor(int minFreq, boolean useLexSynConstraints) {
        this.relExtr = new ReVerbRelationExtractor(minFreq, useLexSynConstraints, mergeOverlapRels, combineVerbs);

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(useMorphologyLexicon));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers());
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

    public void setMergeOverlapRels(boolean mergeOverlapRels) {
        this.mergeOverlapRels = mergeOverlapRels;
    }

    public void setCombineVerbs(boolean combineVerbs) {
        this.combineVerbs = combineVerbs;
    }

    public void setUseMorphologyLexicon(boolean useMorphologyLexicon) {
        this.useMorphologyLexicon = useMorphologyLexicon;
    }
}

