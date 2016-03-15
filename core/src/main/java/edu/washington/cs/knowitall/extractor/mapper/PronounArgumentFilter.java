package edu.washington.cs.knowitall.extractor.mapper;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;

/***
 * Used to filter out arguments that are pronouns.
 *
 * @author afader
 *
 */
public class PronounArgumentFilter extends
        FilterMapper<ChunkedArgumentExtraction> {

    public boolean doFilter(ChunkedArgumentExtraction arg) {
        for (String tag : arg.getPosTags()) {
            if (tag.equals("PDS") || tag.equals("PPER")) {
                return false;
            }
        }
        return true;
    }
}
