package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * A list of mappers for ReVerb III extractor first argument.
 */
public class ReVerbTreeArgument1Mappers extends
                                        MapperList<TreeExtraction> {

    public ReVerbTreeArgument1Mappers() {
        init();
    }

    private void init() {
        List<String> firstPosTags = new ArrayList<>();
        // First word of argument
        // can't be a Wh word
        firstPosTags.add("PWS");
        firstPosTags.add("PWAT");
        firstPosTags.add("PWAV");
        // can't be pronoun
        firstPosTags.add("PRF");       // sich
        firstPosTags.add("PDS");       // dieser, jener
        firstPosTags.add("PPOSS");     // meins, deiner
        firstPosTags.add("PRELAT");    // dessen
        firstPosTags.add("PPER");      // ich, er, ihm, mich
        firstPosTags.add("PRELS");     // [der Hund ,] der

        addMapper(new FirstPosTagNotEqualsFilter(firstPosTags));

        addArgumentNotEqualsFilter("ART");      // der die das

        addTokenNotEqualsFilter("es");
        addTokenNotEqualsFilter("Es");
        addTokenNotEqualsFilter("Man");
        addTokenNotEqualsFilter("man");
    }

    private void addArgumentNotEqualsFilter(final String posTag) {
        addMapper(new FilterMapper<TreeExtraction>() {
            public boolean doFilter(TreeExtraction extraction) {
                if (Iterables.size(extraction.getNodeIds()) == 1) {
                    List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());
                    Node n = nodes.get(0);
                    return ! n.getPos().equals(posTag);
                }
                return true;
            }
        });
    }

    private void addTokenNotEqualsFilter(final String token) {
        addMapper(new FilterMapper<TreeExtraction>() {
            public boolean doFilter(TreeExtraction extraction) {
                if (Iterables.size(extraction.getNodeIds()) == 1) {
                    List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());
                    Node n = nodes.get(0);
                    return ! n.getWord().equals(token);
                }
                return true;
            }
        });
    }
}
