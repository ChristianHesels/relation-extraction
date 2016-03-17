package edu.washington.cs.knowitall.extractor.mapper;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;

/**
 * A list of mappers for <code>ReVerbExtractor</code>'s second arguments.
 *
 * @author afader
 */
public class ReVerbArgument1Mappers extends
                                    MapperList<ChunkedArgumentExtraction> {

    public ReVerbArgument1Mappers() {
        init();
    }

    private void init() {

        // First argument can't be a Wh word
        addFirstPosTagNotEqualsFilter("PWS");
        addFirstPosTagNotEqualsFilter("PWAT");
        addFirstPosTagNotEqualsFilter("PWAV");

        // First argument can't be a preposition
        addFirstPosTagNotEqualsFilter("APPR");
        addFirstPosTagNotEqualsFilter("APPRART");

        // Can't be "that"
        addFirstTokenNotEqualsFilter("dass");
        addFirstTokenNotEqualsFilter("wessen");

        // Can't be reflexive pronoun
        addFirstPosTagNotEqualsFilter("PRF");   // sich
        addFirstPosTagNotEqualsFilter("PDS");   // dieser, jener
        addFirstPosTagNotEqualsFilter("PPOSS"); // meins, deiner
        addFirstPosTagNotEqualsFilter("PDAT");  // diese


        // First argument can't be a single article
        addMapper(new FilterMapper<ChunkedArgumentExtraction>() {
            public boolean doFilter(ChunkedArgumentExtraction extr) {
                return !(extr.getLength() == 1 && extr.getPosTag(0).equals("ART"));
            }
        });

        // First argument can't match "ARG1, REL" "ARG1 and REL" or
        // "ARG1, and REL"
        addMapper(new ConjunctionCommaArgumentFilter());

        // First argument should be closest to relation that passes through
        // filters
        addMapper(new ClosestArgumentMapper());
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
                return !extr.getTokens().get(0).equals(tokenCopy);
            }
        });
    }

}
