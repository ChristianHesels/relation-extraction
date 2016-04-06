package edu.washington.cs.knowitall.extractor.mapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;

/**
 * Given a set of <code>ChunkedExtraction</code>s from the same sentence, combine those
 * extractions, which are separated verbs.
 */
public class SeparatedVerbMapper extends Mapper<ChunkedExtraction> {


    /**
     * Between two separated verbs can only be noun phrases.
     * @param verb1 first verb
     * @param verb2 second verb
     * @return true, if the verbs belong together, false otherwise
     */
    private boolean separatedVerbs(ChunkedExtraction verb1, ChunkedExtraction verb2) {
        ImmutableList<String> chunkTags = verb1.getSentence().getChunkTags(verb1.getStart() + 1, verb2.getStart() - verb1.getStart() - 1);
        boolean separatedVerbs = true;
        for (String chunk : chunkTags) {
            if (!chunk.equals("O") && !chunk.contains("NP")) {
                separatedVerbs = false;
                break;
            }
        }
        return separatedVerbs;
    }

    @Override
    protected Iterable<ChunkedExtraction> doMap(Iterable<ChunkedExtraction> extrs) {
        List<ChunkedExtraction> extrList = new ArrayList<ChunkedExtraction>();
        Iterables.addAll(extrList, extrs);

        for (int i = 0; i < extrList.size() - 1; i++) {
            ChunkedExtraction verb1 = extrList.get(i);
            ChunkedExtraction verb2 = extrList.get(i + 1);

            if (verb1.getStart() < verb2.getStart() && separatedVerbs(verb1, verb2)) {
                verb1.setSubExtraction(verb2);
            }
        }

        return extrList;

    }

}
