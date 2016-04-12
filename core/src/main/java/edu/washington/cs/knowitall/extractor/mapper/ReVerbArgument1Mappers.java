package edu.washington.cs.knowitall.extractor.mapper;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;

/**
 * A list of mappers for <code>ReVerbExtractor</code>'s second arguments.
 *
 * @author afader
 */
public class ReVerbArgument1Mappers extends
                                    MapperList<ChunkedArgumentExtraction> {

    boolean nominativeFilter;

    public ReVerbArgument1Mappers() {
        this.nominativeFilter = false;
        init();
    }
    public ReVerbArgument1Mappers(boolean nominativeFilter) {
        this.nominativeFilter = nominativeFilter;
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

        // Second argument can't be a number
        addFirstPosTagNotEqualsFilter("CARD");

        // Can't be
        addFirstTokenNotEqualsFilter("dass");
        addFirstTokenNotEqualsFilter("es");

        // Can't be pronoun
        addFirstPosTagNotEqualsFilter("PRF");       // sich
        addFirstPosTagNotEqualsFilter("PDS");       // dieser, jener
        addFirstPosTagNotEqualsFilter("PPOSS");     // meins, deiner
        addFirstPosTagNotEqualsFilter("PDAT");      // diese
        addFirstPosTagNotEqualsFilter("PRELAT");    // dessen
        addFirstPosTagNotEqualsFilter("PPER");      // ich, er, ihm, mich

        // First argument can't be a single article or pronoun
        addArgumentNotEqualsFilter("ART");      // der, die, das
        addArgumentNotEqualsFilter("PRELS");    // ..., der
        addArgumentNotEqualsFilter("PIS");      // keiner viele man niemand

        // First argument can't match "ARG1, REL" "ARG1 and REL" or
        // "ARG1, and REL"
        addMapper(new ConjunctionCommaLeftArgumentFilter());

        // First argument should be closest to relation that passes through
        // filters
        if (nominativeFilter) {
            addMapper(new ClosestNominativeArgumentMapper());
        } else {
            addMapper(new ClosestArgumentMapper());
        }
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
                return !extr.getTokens().get(0).toLowerCase().equals(tokenCopy);
            }
        });
    }

    private void addArgumentNotEqualsFilter(String posTag) {
        final String posTagCopy = posTag;
        addMapper(new FilterMapper<ChunkedArgumentExtraction>() {
            public boolean doFilter(ChunkedArgumentExtraction extr) {
                return !(extr.getLength() == 1 && extr.getPosTag(0).equals(posTagCopy));
            }
        });
    }

}
