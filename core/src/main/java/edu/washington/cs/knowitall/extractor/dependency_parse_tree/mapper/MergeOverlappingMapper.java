package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.Mapper;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class MergeOverlappingMapper extends Mapper<TreeExtraction> {

    private static List<TreeExtraction> mergeOverlapping(List<TreeExtraction> extractions) {
        List<TreeExtraction> result = new ArrayList<>(extractions.size());
        result.add(extractions.get(0));
        for (int i = 1; i < extractions.size(); i++) {
            TreeExtraction curr = extractions.get(i);
            TreeExtraction prev = result.get(result.size() - 1);

            for (Integer x : curr.getNodeIds()) {
                if (!Iterables.contains(prev.getNodeIds(), x)) {
                    result.add(curr);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    protected Iterable<TreeExtraction> doMap(Iterable<TreeExtraction> extrs) {
        List<TreeExtraction> extrList = new ArrayList<>();
        Iterables.addAll(extrList, extrs);

        if (extrList.size() > 1) {
            return mergeOverlapping(extrList);
        } else {
            return extrList;
        }
    }

}
