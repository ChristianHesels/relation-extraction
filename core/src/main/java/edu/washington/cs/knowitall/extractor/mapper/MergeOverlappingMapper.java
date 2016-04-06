package edu.washington.cs.knowitall.extractor.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;

/**
 * Given a set of <code>ChunkedExtraction</code>s from the same sentence, merges those extractions
 * that are next to each other or overlapping. For example, given the sentence "He wants to go to
 * the store" and the relations "wants to" and "go to", returns "wants to go to".
 *
 * @author afader
 */
public class MergeOverlappingMapper extends Mapper<ChunkedExtraction> {


    private static ChunkedExtraction join(ChunkedExtraction curr, ChunkedExtraction prev) {
        Range range = curr.getRange().join(prev.getRange());
        ChunkedSentence sentence = curr.getSentence();

        ChunkedExtraction newExtr = new ChunkedExtraction(sentence, range);
        if (curr.hasSubExtraction()) {
            newExtr.setSubExtraction(curr.getSubExtraction());
        } else if (prev.hasSubExtraction()) {
            newExtr.setSubExtraction(prev.getSubExtraction());
        }

        return newExtr;
    }

    private static List<ChunkedExtraction> mergeOverlapping(List<ChunkedExtraction> extractions) {
        extractions.sort(new Comparator<ChunkedExtraction>() {
            @Override
            public int compare(ChunkedExtraction o1, ChunkedExtraction o2) {
                return o1.getRange().compareTo(o2.getRange());
            }
        });

        List<ChunkedExtraction> result = new ArrayList<ChunkedExtraction>(extractions.size());
        if (extractions.size() > 1) {
            result.add(extractions.get(0));
            for (int i = 1; i < extractions.size(); i++) {
                ChunkedExtraction curr = extractions.get(i);
                ChunkedExtraction prev = result.get(result.size() - 1);
                if (prev.isAdjacentOrOverlaps(curr)) {
                    ChunkedExtraction updated = join(curr, prev);
                    result.set(result.size() - 1, updated);
                } else {
                    result.add(curr);
                }
            }
            return result;

        } else {
            return extractions;
        }
    }

    @Override
    protected Iterable<ChunkedExtraction> doMap(
        Iterable<ChunkedExtraction> extrs) {
        List<ChunkedExtraction> extrList = new ArrayList<ChunkedExtraction>();
        Iterables.addAll(extrList, extrs);

        if (extrList.size() > 1) {
            return mergeOverlapping(extrList);
        } else {
            return extrList;
        }
    }

}
