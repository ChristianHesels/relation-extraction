package de.hpi.extractor.dependency_parse_tree.mapper;


import de.hpi.extractor.Mapper;
import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters out relations containing a pronoun.
 */
public class PronounRelationFilter extends Mapper<TreeBinaryExtraction> {

    private boolean pronounsAllowed;

    public PronounRelationFilter() {
        this(false);
    }

    public PronounRelationFilter(boolean pronounsAllowed) {
        this.pronounsAllowed = pronounsAllowed;
    }

    @Override
    protected Iterable<TreeBinaryExtraction> doMap(Iterable<TreeBinaryExtraction> extractions) {
        if (this.pronounsAllowed) {
            return extractions;
        }

        List<TreeBinaryExtraction> result = new ArrayList<>();

        for (TreeBinaryExtraction extraction: extractions) {
            List<Node> relationNodes = extraction.getTree().find(extraction.getRel().getNodeIds());
            List<Node> pronouns = relationNodes.stream().filter(n -> n.getPos().equals("PPER") && !n.getWord().equals("sich")).collect(Collectors.toList());

            if (pronouns.isEmpty()) {
                result.add(extraction);
            }
        }

        return result;
    }
}
