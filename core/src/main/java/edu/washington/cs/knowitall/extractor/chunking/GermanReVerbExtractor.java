package edu.washington.cs.knowitall.extractor.chunking;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.extractor.chunking.mapper.ChunkedBinaryExtractionMergeOverlappingMapper;
import edu.washington.cs.knowitall.extractor.chunking.mapper.ReVerbArgument1Mappers;
import edu.washington.cs.knowitall.extractor.chunking.mapper.ReVerbArgument2Mappers;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedRelationExtraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GermanReVerbExtractor extends Extractor<ChunkedSentence, ChunkedBinaryExtraction> {

    protected Extractor<ChunkedSentence, ChunkedSentence> sentExtr;
    protected Extractor<ChunkedSentence, ChunkedRelationExtraction> relExtr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg1Extr;
    protected Extractor<ChunkedRelationExtraction, ChunkedArgumentExtraction> arg2Extr;

    private static final boolean allowUnary = false;
    private static final boolean mergeOverlapRels = false;
    private static final boolean combineVerbs = true;
    private static final boolean useMorphologyLexicon = true;
    private boolean extractSubsentences = true;

    public GermanReVerbExtractor() {
        this.sentExtr = new SubsentenceExtractor();

        this.relExtr = new ReVerbRelationExtractor();

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(useMorphologyLexicon));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers());

        this.addMapper(new ChunkedBinaryExtractionMergeOverlappingMapper());
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     */
    public GermanReVerbExtractor(int minFreq, boolean useLexSynConstraints) {
        this.sentExtr = new SubsentenceExtractor();

        this.relExtr = new ReVerbRelationExtractor(minFreq, useLexSynConstraints, mergeOverlapRels, combineVerbs);

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(useMorphologyLexicon));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers());

        this.addMapper(new ChunkedBinaryExtractionMergeOverlappingMapper());
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param minFreq              - The minimum distinct arguments to be observed in a large
     *                             collection for the relation to be deemed valid.
     * @param useLexSynConstraints - Use syntactic and lexical constraints that are part of Reverb?
     * @param combineVerbs         - Combine separated verbs?
     * @param useMorphologyLexicon - Use a morphology lexicon?
     * @param extractSubsentences  - Divide the sentence into subsentence before extracting relations?
     */
    public GermanReVerbExtractor(int minFreq, boolean useLexSynConstraints, boolean combineVerbs,
                                 boolean useMorphologyLexicon, boolean extractSubsentences) {
        this.sentExtr = new SubsentenceExtractor();
        this.extractSubsentences = extractSubsentences;

        this.relExtr = new ReVerbRelationExtractor(minFreq, useLexSynConstraints, mergeOverlapRels, combineVerbs);

        this.arg1Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.LEFT);
        arg1Extr.addMapper(new ReVerbArgument1Mappers(useMorphologyLexicon));

        this.arg2Extr = new ChunkedArgumentExtractor(ChunkedArgumentExtractor.Mode.RIGHT);
        arg2Extr.addMapper(new ReVerbArgument2Mappers());

        this.addMapper(new ChunkedBinaryExtractionMergeOverlappingMapper());
    }


    @Override
    protected Iterable<ChunkedBinaryExtraction> extractCandidates(ChunkedSentence source)
            throws ExtractorException {
        Collection<ChunkedBinaryExtraction> extrs = new ArrayList<ChunkedBinaryExtraction>();

        Iterable<? extends ChunkedSentence> sentences;
        if (extractSubsentences) {
            sentences = sentExtr.extract(source);
        } else {
            List<ChunkedSentence> tmpList = new ArrayList<>();
            tmpList.add(source);
            sentences = tmpList;
        }

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

