package edu.washington.cs.knowitall.extractor.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

/**
 * Given a set of <code>ChunkedExtraction</code>s from the same sentence, combine those
 * extractions, which are separated verbs.
 */
public class SeparatedVerbMapper extends Mapper<ChunkedRelationExtraction> {

    private boolean containsOnlyNPAndO(List<String> chunkTags) {
        boolean npo = true;
        for (String chunk : chunkTags) {
            if (!chunk.equals("O") && !chunk.contains("NP") && !chunk.contains("PP")) {
                npo = false;
                break;
            }
        }
        return npo;
    }

    private boolean containsOnlyVerbForms(List<String> posTags) {
        boolean verbForms = true;
        for (String pos : posTags) {
            if (!pos.startsWith("V") && !pos.equals("PTKVZ") && !pos.equals("PTKNEG")) {
                verbForms = false;
                break;
            }
        }
        return verbForms;
    }

    private boolean noClause(List<String> posTags) {
        int commaCount = 0;
        int conjunctionCount = 0;
        for (String pos : posTags) {
            if (pos.equals("$,")) {
                commaCount++;
            } else if (pos.equals("KON")) {
                conjunctionCount++;
            }
        }
        return commaCount == 0 && conjunctionCount == 0;
    }

    /**
     * Between two separated verbs can only be noun phrases.
     * @param verb1 first verb
     * @param verb2 second verb
     * @return true, if the verbs belong together, false otherwise
     */
    private boolean separatedVerbs(ChunkedExtraction verb1, ChunkedExtraction verb2) {
        int start = verb1.getStart() + 1;
        int end = verb2.getStart() - verb1.getStart() - 1;
        ChunkedSentence sentence = verb1.getSentence();

        return containsOnlyNPAndO(sentence.getChunkTags(start, end)) &&
               containsOnlyVerbForms(verb2.getPosTags()) &&
               noClause(sentence.getPosTags(start, end));
    }

    @Override
    protected Iterable<ChunkedRelationExtraction> doMap(Iterable<ChunkedRelationExtraction> extrs) {
        List<ChunkedRelationExtraction> extrList = new ArrayList<ChunkedRelationExtraction>();
        Iterables.addAll(extrList, extrs);

        extrList.sort(new Comparator<ChunkedExtraction>() {
            @Override
            public int compare(ChunkedExtraction o1, ChunkedExtraction o2) {
                return o1.getRange().compareTo(o2.getRange());
            }
        });

        for (int i = 0; i < extrList.size() - 1; i++) {
            ChunkedRelationExtraction verb1 = extrList.get(i);
            for (int j = i + 1; j < extrList.size(); j++) {
                ChunkedRelationExtraction verb2 = extrList.get(j);

                if (verb1.getStart() < verb2.getStart()) {
                    boolean s = separatedVerbs(verb1, verb2);
                    if (s) verb1.setSubRelation(verb2);
                }
            }
        }

        return extrList;
    }

}
