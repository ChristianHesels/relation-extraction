package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.extractor.IndependentMapper;
import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.LeafNode;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * A list of mappers for ReVerb III extractor second argument.
 */
public class ReVerbTreeArgument2Mappers extends
                                        MapperList<TreeExtraction> {

    private static final String VERB = "VVFIN VVINF VVIZU VAFIN VAINF VMINF VMFIN PTKVZ VMPP VAPP VVPP PKTZU";

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

        addMapper(new IndependentMapper<TreeExtraction>() {
            @Override
            public TreeExtraction doMap(TreeExtraction extraction) {
                List<Integer> ids = new ArrayList<>();
                for (int i : extraction.getNodeIds()) {
                    if (!extraction.getTree().find(i).matchPosTag(Arrays.asList(VERB.split(" ")))) {
                        ids.add(i);
                    }
                }
                extraction.setNodeIds(ids);
                return extraction;
            }
        });
    }


    private void addFirstPosTagNotEqualsFilter(final String posTag) {
        final List<String> posTagList = new ArrayList<>();
        posTagList.add(posTag);
        addMapper(new FilterMapper<TreeExtraction>() {
            public boolean doFilter(TreeExtraction extraction) {
                if (Iterables.size(extraction.getNodeIds()) > 0) {
                    List<Node> l = extraction.getTree().find(extraction.getNodeIds());
                    LeafNode n = (LeafNode) l.get(0);
                    return !n.matchPosTag(posTagList);
                }
                return true;
            }
        });
    }


}
