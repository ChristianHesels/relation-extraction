package edu.washington.cs.knowitall.extractor;

import java.util.ArrayList;
import java.util.Collection;

import edu.washington.cs.knowitall.extractor.mapper.ReVerbArgument1Mappers;
import edu.washington.cs.knowitall.extractor.mapper.ReVerbArgument2Mappers;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;


public class ReVerbIIExtractor extends Extractor<ChunkedSentence, ChunkedBinaryExtraction> {

    protected Extractor<ChunkedSentence, ChunkedSentence> sentExtr;
    protected Extractor<ChunkedSentence, ChunkedRelationExtraction> relExtr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg1Extr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg2Extr;

    private boolean allowUnary;

    public ReVerbIIExtractor() {
        this.sentExtr = new SubsentenceExtractor();

        this.relExtr = new ReVerbRelationExtractor();

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(true));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers());

        allowUnary = false;
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     * @param mergeOverlapRels     - Merge overlapping relations?
     * @param combineVerbs         - Combine separated verbs?
     * @param allowUnary           - Allow relations with one argument to be output.
     */
    public ReVerbIIExtractor(int minFreq, boolean useLexSynConstraints,
                             boolean mergeOverlapRels, boolean combineVerbs, boolean allowUnary) {
        this.sentExtr = new SubsentenceExtractor();

        this.relExtr = new ReVerbRelationExtractor(minFreq, useLexSynConstraints, mergeOverlapRels, combineVerbs);

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(true));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers());

        this.allowUnary = allowUnary;
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> extractCandidates(ChunkedSentence source)
        throws ExtractorException {
        Collection<ChunkedBinaryExtraction> extrs = new ArrayList<ChunkedBinaryExtraction>();

        Iterable<? extends ChunkedSentence> sentences = sentExtr.extract(source);

        for (ChunkedSentence sentence : sentences) {
            Iterable<? extends ChunkedRelationExtraction> rels = relExtr.extract(sentence);
            for (ChunkedRelationExtraction rel : rels) {
                Iterable<? extends ChunkedArgumentExtraction> arg1s =
                    arg1Extr.extract(rel);
                Iterable<? extends ChunkedArgumentExtraction> arg2s =
                    arg2Extr.extract(rel);

                extrs.addAll(
                    ChunkedBinaryExtraction.productOfArgs(rel, arg1s, arg2s, allowUnary));
            }
        }

        return extrs;
    }
}

