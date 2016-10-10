package de.hpi.extractor.chunking.mapper;

import de.hpi.extractor.FilterMapper;
import de.hpi.nlp.extraction.chunking.ChunkedRelationExtraction;

/**
 * A class used to filter out any relations, which do not contain any VP.
 */
public class VerbFilter extends FilterMapper<ChunkedRelationExtraction> {

    @Override
    public boolean doFilter(ChunkedRelationExtraction extr) {
        // a relation should contain a verb phrase chunk
        boolean containsVP = false;
        for (String chunkTag : extr.getChunkTags()) {
            if (chunkTag.contains("VP")) {
                containsVP = true;
            }
        }
        if (!containsVP) {
            return false;
        }

        return true;
    }

}
