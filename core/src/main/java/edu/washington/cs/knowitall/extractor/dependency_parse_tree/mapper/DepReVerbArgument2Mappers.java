package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of mappers for ReVerb III extractor second argument.
 */
public class DepReVerbArgument2Mappers extends
                                        MapperList<TreeExtraction> {

    public DepReVerbArgument2Mappers() {
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
//        firstPosTags.add("PRF");   // sich
//        firstPosTags.add("PDS");   // dieser, jener
//        firstPosTags.add("PDAT");  // dieser, jener
//        firstPosTags.add("PPOSS"); // meins, deiner
//        firstPosTags.add("PRELAT");    // dessen
//        firstPosTags.add("PIS");   // man
//        firstPosTags.add("PPER");  // er

        List<String> firstTokens = new ArrayList<>();
//        firstTokens.add("solche");
//        firstTokens.add("diese");

        addMapper(new FirstTokenNotEqualsFilter(firstTokens));

        List<String> tokens = new ArrayList<>();
        // special quote characters, which may not be parsed correctly
        tokens.add("\u2018");
        tokens.add("\u2019");
        tokens.add("\u201A");
        tokens.add("\u201B");
        tokens.add("\u201C");
        tokens.add("\u201D");
        tokens.add("\u201E");
        tokens.add("\u201F");

        addMapper(new TokenNotEqualsFilter(tokens));

        addMapper(new FirstPosTagNotEqualsFilter(firstPosTags));

        addMapper(new ContainsNounFilter());
    }

}
