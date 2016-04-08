package edu.washington.cs.knowitall.extractor.mapper;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

/**
 * A class used to filter out any relations, which do not contain any VP.
 */
public class ContainsVerbFilter extends FilterMapper<ChunkedRelationExtraction> {

    @Override
    public boolean doFilter(ChunkedRelationExtraction extr) {
        boolean containsVP = false;
        for (String chunkTag : extr.getChunkTags()) {
            if (chunkTag.contains("VP")) {
                containsVP = true;
            }
        }
        return containsVP;
    }

}
