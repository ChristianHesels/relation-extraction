package edu.washington.cs.knowitall.extractor.mapper;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;

/**
 * A list of mappers for <code>ReVerbExtractor</code>'s first arguments.
 *
 * @author afader
 */
public class ReVerbArgument2Mappers extends
                                    MapperList<ChunkedArgumentExtraction> {

    public ReVerbArgument2Mappers() {
        init();
    }

    private void init() {
        // Second argument can't be a Wh word
        addFirstPosTagNotEqualsFilter("PWS");
        addFirstPosTagNotEqualsFilter("PWAT");
        addFirstPosTagNotEqualsFilter("PWAV");

        // Second argument can't be a number
        addFirstPosTagNotEqualsFilter("CARD");

        // Can't be pronoun
        addFirstPosTagNotEqualsFilter("PRF");   // sich
        addFirstPosTagNotEqualsFilter("PDS");   // dieser, jener
        addFirstPosTagNotEqualsFilter("PDAT");  // dieser, jener
        addFirstPosTagNotEqualsFilter("PPOSS"); // meins, deiner
        addFirstPosTagNotEqualsFilter("PIS");   // man
        addFirstPosTagNotEqualsFilter("PPER");  // er

        // First argument can't match "REL, ARG2" or "REL and ARG2"
        addMapper(new ConjunctionCommaRightArgumentFilter());

        // Second argument should be closest to relation that passes through
        // filters
        addMapper(new ClosestArgumentMapper());

        // Second argument should be adjacent to the relation
        addMapper(new AdjacentToRelationFilter());

        // If the relation is a separated relation, the argument must be in between
        addMapper(new FilterMapper<ChunkedArgumentExtraction>() {
            public boolean doFilter(ChunkedArgumentExtraction extr) {
                if (extr.getRelation().hasSubExtraction()) {
                    int relEnd = extr.getRelation().getStart() + extr.getRelation().getLength() - 1;
                    int subRelStart = extr.getRelation().getSubExtraction().getStart();
                    int argEnd = extr.getStart() + extr.getLength() - 1;
                    int argStart = extr.getStart();

                    return argStart > relEnd && argEnd < subRelStart;
                }
                return true;
            }
        });
    }

    private void addFirstPosTagNotEqualsFilter(String posTag) {
        final String posTagCopy = posTag;
        addMapper(new FilterMapper<ChunkedArgumentExtraction>() {
            public boolean doFilter(ChunkedArgumentExtraction extr) {
                return !extr.getPosTags().get(0).equals(posTagCopy);
            }
        });
    }

    private void addFirstTokenNotEqualsFilter(String token) {
        final String tokenCopy = token;
        addMapper(new FilterMapper<ChunkedArgumentExtraction>() {
            public boolean doFilter(ChunkedArgumentExtraction extr) {
                return !extr.getPosTags().get(0).equals(tokenCopy);
            }
        });
    }


}
