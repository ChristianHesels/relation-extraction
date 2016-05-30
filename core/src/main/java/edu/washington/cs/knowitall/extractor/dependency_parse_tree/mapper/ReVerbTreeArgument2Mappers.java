package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import java.util.ArrayList;
import java.util.List;

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
        List<String> firstPosTags = new ArrayList<>();

        // First word of second argument
        // can't be a Wh word
        firstPosTags.add("PWS");
        firstPosTags.add("PWAT");
        firstPosTags.add("PWAV");
        // can't be a number
        firstPosTags.add("CARD");
        // can't be pronoun
        firstPosTags.add("PRF");   // sich
        firstPosTags.add("PDS");   // dieser, jener
        firstPosTags.add("PDAT");  // dieser, jener
        firstPosTags.add("PPOSS"); // meins, deiner
        firstPosTags.add("PIS");   // man
        firstPosTags.add("PPER");  // er

        addMapper(new FirstPosTagNotEqualsFilter(firstPosTags));
    }



}
