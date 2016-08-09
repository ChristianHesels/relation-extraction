package edu.washington.cs.knowitall.extractor.chunking.mapper;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedRelationExtraction;

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
