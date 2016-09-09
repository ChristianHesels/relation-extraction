package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of mappers forDep ConIE second argument extractor.
 */
public class DepConIEArgument2Mappers extends
                                        MapperList<TreeExtraction> {

    public DepConIEArgument2Mappers() {
        init();
    }

    private void init() {
        List<String> firstPosTags = new ArrayList<>();

        // First word of second argument
        // can't be a Wh word
        firstPosTags.add("PWS");
        firstPosTags.add("PWAT");
        firstPosTags.add("PWAV");

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
