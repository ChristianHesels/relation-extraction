package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * A list of mappers for ReVerb III extractor second argument.
 */
public class ReVerbTreeArgument2Mappers extends
                                        MapperList<TreeExtraction> {

    public ReVerbTreeArgument2Mappers() {
        init();
    }

    private void init() {
        // Second argument can't be a Wh word
        addFirstPosTagNotEqualsFilter("PWS");
        addFirstPosTagNotEqualsFilter("PWAT");
        addFirstPosTagNotEqualsFilter("PWAV");

        // Second argument can't be a number
        addFirstPosTagNotEqualsFilter("CARD");

        // Can't be pronoun
        addFirstPosTagNotEqualsFilter("PRF");   // sich
        addFirstPosTagNotEqualsFilter("PDS");   // dieser, jener
        addFirstPosTagNotEqualsFilter("PDAT");  // dieser, jener
        addFirstPosTagNotEqualsFilter("PPOSS"); // meins, deiner
        addFirstPosTagNotEqualsFilter("PIS");   // man
        addFirstPosTagNotEqualsFilter("PPER");  // er
    }

    private void addFirstPosTagNotEqualsFilter(final String posTag) {
        final List<String> posTagList = new ArrayList<>();
        posTagList.add(posTag);
        addMapper(new FilterMapper<TreeExtraction>() {
            public boolean doFilter(TreeExtraction node) {
//                if (!node.getLeafNodes().isEmpty()) {
//                    Node n = node.getLeafNodes().get(0);
//                    return !n.matchPosTag(posTagList);
//                }
                return true;
            }
        });
    }


}
