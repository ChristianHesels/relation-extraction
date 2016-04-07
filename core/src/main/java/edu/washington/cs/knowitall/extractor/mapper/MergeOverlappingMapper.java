package edu.washington.cs.knowitall.extractor.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

/**
 * Given a set of <code>ChunkedExtraction</code>s from the same sentence, merges those extractions
 * that are next to each other or overlapping. For example, given the sentence "He wants to go to
 * the store" and the relations "wants to" and "go to", returns "wants to go to".
 *
 * @author afader
 */
public class MergeOverlappingMapper extends Mapper<ChunkedRelationExtraction> {


    private static ChunkedRelationExtraction join(ChunkedRelationExtraction curr, ChunkedRelationExtraction prev) {
        Range range = curr.getRange().join(prev.getRange());
        ChunkedSentence sentence = curr.getSentence();

        ChunkedRelationExtraction newExtr = new ChunkedRelationExtraction(sentence, range);
        if (curr.hasSubRelation()) {
            newExtr.setSubRelation(curr.getSubRelation());
        } else if (prev.hasSubRelation()) {
            newExtr.setSubRelation(prev.getSubRelation());
        }

        return newExtr;
    }

    private static List<ChunkedRelationExtraction> mergeOverlapping(List<ChunkedRelationExtraction> extractions) {
        extractions.sort(new Comparator<ChunkedExtraction>() {
            @Override
            public int compare(ChunkedExtraction o1, ChunkedExtraction o2) {
                return o1.getRange().compareTo(o2.getRange());
            }
        });

        List<ChunkedRelationExtraction> result = new ArrayList<ChunkedRelationExtraction>(extractions.size());
        if (extractions.size() > 1) {
            result.add(extractions.get(0));
            for (int i = 1; i < extractions.size(); i++) {
                ChunkedRelationExtraction curr = extractions.get(i);
                ChunkedRelationExtraction prev = result.get(result.size() - 1);
                if (prev.isAdjacentOrOverlaps(curr)) {
                    ChunkedRelationExtraction updated = join(curr, prev);
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
    protected Iterable<ChunkedRelationExtraction> doMap(
        Iterable<ChunkedRelationExtraction> extrs) {
        List<ChunkedRelationExtraction> extrList = new ArrayList<ChunkedRelationExtraction>();
        Iterables.addAll(extrList, extrs);

        if (extrList.size() > 1) {
            return mergeOverlapping(extrList);
        } else {
            return extrList;
        }
    }

}
