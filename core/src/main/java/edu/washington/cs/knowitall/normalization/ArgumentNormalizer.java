package edu.washington.cs.knowitall.normalization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedExtraction;

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

}
