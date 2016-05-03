package edu.washington.cs.knowitall.extractor.mapper;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;

/**
 * A list of mappers for ReVerb III extractor second argument.
 */
public class ReVerbTreeArgument2Mappers extends
                                        MapperList<Node> {

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
        addMapper(new FilterMapper<Node>() {
            public boolean doFilter(Node node) {
                if (!node.getLeafNodes().isEmpty()) {
                    Node n = node.getLeafNodes().get(0);
                    return !n.matchPosTag(posTagList);
                }
                return true;
            }
        });
    }


}
