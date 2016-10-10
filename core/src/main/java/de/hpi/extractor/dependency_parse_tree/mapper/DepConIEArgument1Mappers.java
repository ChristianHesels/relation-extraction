package de.hpi.extractor.dependency_parse_tree.mapper;

import com.google.common.collect.Iterables;
import de.hpi.extractor.FilterMapper;
import de.hpi.extractor.MapperList;
import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of mappers for Dep ConIE first argument.
 */
public class DepConIEArgument1Mappers extends MapperList<TreeExtraction> {

    public DepConIEArgument1Mappers() {
        this(false);
    }

    /**
     * Constructor of Dep ConIE argument 2 mapper
     * @param pronounsAsSubject consider pronouns as subject?
     */
    public DepConIEArgument1Mappers(boolean pronounsAsSubject) {
        init(pronounsAsSubject);
    }

    private void init(boolean pronounsAsSubject) {
        addMapper(new MergeOverlappingMapper());

        List<String> firstPosTags = new ArrayList<>();
        // First word of argument
        // can't be a Wh word
        firstPosTags.add("PWS");
        firstPosTags.add("PWAT");
        firstPosTags.add("PWAV");
        if (!pronounsAsSubject) {
            firstPosTags.add("PPER");      // ich, er, ihm, mich
        }
        firstPosTags.add("PRELS");     // [der Hund ,] der

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
