package de.hpi.extractor.chunking.mapper;

import de.hpi.extractor.FilterMapper;
import de.hpi.nlp.extraction.chunking.ChunkedRelationExtraction;
import de.hpi.normalization.NormalizedField;
import de.hpi.normalization.VerbalRelationNormalizer;

import java.util.HashSet;

/**
 * A class used to filter out any relations whose normalized form does not appear in the given
 * dictionary. Relation strings are normalized using the VerbalRelationNormalizer class.
 *
 * @author afader
 */
public class NormalizedRelationDictionaryFilter extends
                                                FilterMapper<ChunkedRelationExtraction> {

    private HashSet<String> relations;
    private VerbalRelationNormalizer normalizer;

    /**
     * Constructs a new filter using the String relations in the given set. These relations should
     * be normalized using the VerbalRelationNormalizer class, with a space between each token in
     * the string.
     * @param relations the relations contained in the dictionary
     */
    public NormalizedRelationDictionaryFilter(HashSet<String> relations) {
        this.relations = relations;
        normalizer = new VerbalRelationNormalizer(true, true, false);
    }

    /**
     * Returns true if the tokens in the given extraction appear in the set of relations passed to
     * the constructor.
     */
    public boolean doFilter(ChunkedRelationExtraction extr) {
        NormalizedField normField = normalizer.normalizeField(extr);
        return relations.contains(normField.toString());
    }

}
