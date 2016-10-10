package de.hpi.normalization;

import com.google.common.base.Joiner;
import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.chunking.ChunkedArgumentExtraction;
import de.hpi.nlp.extraction.chunking.ChunkedExtraction;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Normalizes {@link ChunkedExtraction} arguments by <ul> <li>Lowercasing</li> <li>Removing
 * punctuation</li> <li>Replacing numbers with a # symbol</li> </ul>
 *
 * @author afader
 */
public class ArgumentNormalizer {

    private HashSet<String> ignorePosTags;
    private HashSet<String> attributePosTags;


    public ArgumentNormalizer() {
        ignorePosTags = new HashSet<String>();
        ignorePosTags.add("KON");
        ignorePosTags.add("$,");
        ignorePosTags.add("ART");

        attributePosTags = new HashSet<String>();
        attributePosTags.add("ADV");
        attributePosTags.add("ADJA");
    }

    /**
     * Remove adjective and adverbs and store separate.
     * @param field argument to normalize
     * @return the normalized argument
     */
    public NormalizedArgumentField normalizeField(ChunkedArgumentExtraction field) {
        ArrayList<String> tokens = new ArrayList<String>(field.getTokens());
        ArrayList<String> tags = new ArrayList<String>(field.getPosTags());

        List<String> attrTokens = new ArrayList<String>();
        List<String> attrTags = new ArrayList<String>();

        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            String tag = tags.get(i);
            if (ignorePosTags.contains(tag)) {
                tokens.remove(i);
                tags.remove(i);
            } else if (attributePosTags.contains(tag)) {
                attrTags.add(tag);
                attrTokens.add(token);
                tokens.remove(i);
                tags.remove(i);
            } else {
                i++;
            }
        }
        return new NormalizedArgumentField(field, tokens, tags, attrTokens, attrTags);
    }

    /**
     * Remove adjective and adverbs and store separate.
     * @param extraction argument to normalize
     * @return the normalized argument
     */
    public TreeNormalizedField normalizeField(TreeExtraction extraction) {
        List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());
        if (extraction.getLastNodeId() != null) {
            nodes.add(extraction.getRootNode().find(extraction.getLastNodeId()));
        }

        List<String> tokens = nodes.stream().map(Node::getWord).collect(Collectors.toList());
        List<String> tags = nodes.stream().map(Node::getPos).collect(Collectors.toList());

        int i = 0;
        while (i < tokens.size()) {
            String tag = tags.get(i);
            if (ignorePosTags.contains(tag)) {
                tokens.remove(i);
                tags.remove(i);
            } else if (attributePosTags.contains(tag)) {
                tokens.remove(i);
                tags.remove(i);
            } else {
                i++;
            }
        }
        return new TreeNormalizedField(Joiner.on(" ").join(tokens));
    }

}
