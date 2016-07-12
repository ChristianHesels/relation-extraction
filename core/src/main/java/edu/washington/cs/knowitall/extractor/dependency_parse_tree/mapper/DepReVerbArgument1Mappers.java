package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;
import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of mappers for ReVerb III extractor first argument.
 */
public class DepReVerbArgument1Mappers extends
        MapperList<TreeExtraction> {

    public DepReVerbArgument1Mappers() {
        this(false);
    }

    // TODO
    // Filter subject, which do not contain a character
    // Filter times (00 Uhr)
    // Filter root node == 'davon'

    /**
     * Constructor of DepReVerb argument 2 mapper
     * @param pronounsAsSubject consider pronouns as subject?
     */
    public DepReVerbArgument1Mappers(boolean pronounsAsSubject) {
        init(pronounsAsSubject);
    }

    private void init(boolean pronounsAsSubject) {
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
        if (!pronounsAsSubject) {
            firstPosTags.add("PPER");      // ich, er, ihm, mich
        }
        firstPosTags.add("PRELS");     // [der Hund ,] der

        addMapper(new MergeOverlappingMapper());
        addMapper(new FirstPosTagNotEqualsFilter(firstPosTags));

        addArgumentNotEqualsFilter("ART");      // der die das
        addArgumentNotEqualsFilter("PIS");      // alle, wenige, keiner

        List<String> tokens = new ArrayList<>();
        tokens.add("es");
        tokens.add("Es");
        tokens.add("Man");
        tokens.add("man");
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

        addMapper(new ContainsNounFilter(pronounsAsSubject));
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
}
