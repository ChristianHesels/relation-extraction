package de.hpi.extractor.chunking.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.hpi.extractor.Mapper;
import de.hpi.nlp.extraction.chunking.ChunkedBinaryExtraction;

/**
 * Given a set of <code>ChunkedExtraction</code>s from the same sentence, merges those extractions
 * that are next to each other or overlapping. For example, given the sentence "He wants to go to
 * the store" and the relations "wants to" and "go to", returns "wants to go to".
 *
 * @author afader
 */
public class ChunkedBinaryExtractionMergeOverlappingMapper extends Mapper<ChunkedBinaryExtraction> {

    private List<ChunkedBinaryExtraction> mergeOverlapping(List<ChunkedBinaryExtraction> extractions) {
        extractions.sort(new Comparator<ChunkedBinaryExtraction>() {
            @Override
            public int compare(ChunkedBinaryExtraction o1, ChunkedBinaryExtraction o2) {
                return Integer.compare(o1.getRelation().getRange().getEnd(), o2.getRelation().getRange().getEnd());
            }
        });

        List<ChunkedBinaryExtraction> result = new ArrayList<ChunkedBinaryExtraction>(extractions.size());
        if (extractions.size() > 1) {

            for (int i = 0; i < extractions.size() - 1; i++) {
                ChunkedBinaryExtraction extr1 = extractions.get(i);
                boolean isContained = false;

                for (int j = i + 1; j < extractions.size(); j++) {
                    if (i == j) continue;

                    ChunkedBinaryExtraction extr2 = extractions.get(j);

                    if (isContainedIn(extr1, extr2)) {
                        isContained = true;
                        break;
                    }
                }

                if (!isContained) {
                    result.add(extr1);
                }
            }
            result.add(extractions.get(extractions.size() - 1));
            return result;

        } else {
            return extractions;
        }
    }

    /**
     * @param extr1 extraction 1
     * @param extr2 extraction 2
     * @return true, if extr1 is contained in or equal to extr2
     */
    private boolean isContainedIn(ChunkedBinaryExtraction extr1, ChunkedBinaryExtraction extr2) {
        return extr2.getRelation().getRange().contains(extr1.getRelation().getRange());
    }

    @Override
    protected Iterable<ChunkedBinaryExtraction> doMap(
        Iterable<ChunkedBinaryExtraction> extrs) {
        List<ChunkedBinaryExtraction> extrList = new ArrayList<ChunkedBinaryExtraction>();
        Iterables.addAll(extrList, extrs);

        if (extrList.size() > 1) {
            return mergeOverlapping(extrList);
        } else {
            return extrList;
        }
    }

}
