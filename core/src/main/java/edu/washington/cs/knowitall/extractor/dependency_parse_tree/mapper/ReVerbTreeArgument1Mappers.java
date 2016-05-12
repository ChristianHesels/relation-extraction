package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;

/**
 * A list of mappers for ReVerb III extractor first argument.
 */
public class ReVerbTreeArgument1Mappers extends
                                        MapperList<Node> {

    public ReVerbTreeArgument1Mappers() {
        init();
    }

    private void init() {
        // First argument can't be a Wh word
        addFirstPosTagNotEqualsFilter("PWS");
        addFirstPosTagNotEqualsFilter("PWAT");
        addFirstPosTagNotEqualsFilter("PWAV");

        // First argument can't be a preposition
        addFirstPosTagNotEqualsFilter("APPR");
        addFirstPosTagNotEqualsFilter("APPRART");

        // Can't be pronoun
        addFirstPosTagNotEqualsFilter("PRF");       // sich
        addFirstPosTagNotEqualsFilter("PDS");       // dieser, jener
        addFirstPosTagNotEqualsFilter("PPOSS");     // meins, deiner
        addFirstPosTagNotEqualsFilter("PDAT");      // diese
        addFirstPosTagNotEqualsFilter("PRELAT");    // dessen
        addFirstPosTagNotEqualsFilter("PPER");      // ich, er, ihm, mich

        addArgumentNotEqualsFilter("ART");      // der die das
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

    private void addArgumentNotEqualsFilter(final String posTag) {
        final List<String> posTagList = new ArrayList<>();
        posTagList.add(posTag);
        addMapper(new FilterMapper<Node>() {
            public boolean doFilter(Node node) {
                if (node.getLeafNodes().size() == 1) {
                    Node n = node.getLeafNodes().get(0);
                    return !n.matchPosTag(posTagList);
                }
                return true;
            }
        });
    }
}
