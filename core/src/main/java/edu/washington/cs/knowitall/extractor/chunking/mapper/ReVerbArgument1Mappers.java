package edu.washington.cs.knowitall.extractor.chunking.mapper;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.extractor.MapperList;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;

/**
 * A list of mappers for <code>ReVerbExtractor</code>'s second arguments.
 *
 * @author afader
 */
public class ReVerbArgument1Mappers extends
                                    MapperList<ChunkedArgumentExtraction> {

    boolean useMorphologyLexicon;

    public ReVerbArgument1Mappers() {
        this.useMorphologyLexicon = false;
        init();
    }
    public ReVerbArgument1Mappers(boolean useMorphologyLexicon) {
        this.useMorphologyLexicon = useMorphologyLexicon;
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

        // First argument can't be a single number
        addArgumentNotEqualsFilter("CARD");

        // First argument can't be a punctuation character
        addArgumentNotEqualsFilter("$(");
        addArgumentNotEqualsFilter("$.");
        addArgumentNotEqualsFilter("$,");

        // First argument can't be a single pronoun
        addArgumentNotEqualsFilter("PPOSAT");

        // First argument can't be a single article
        addArgumentNotEqualsFilter("ART");

        // First argument can't match "ARG1, REL" "ARG1 and REL" or
        // "ARG1, and REL"
        addMapper(new ConjunctionCommaLeftArgumentFilter());

        // First argument should be closest to relation that passes through
        // filters
        if (useMorphologyLexicon) {
            addMapper(new ClosestNominativeArgumentMapper());
        } else {
            addMapper(new ClosestArgumentMapper());
        }


        /*
         * The argument shouldn't just be a single non word character.
         */
        addMapper(new FilterMapper<ChunkedArgumentExtraction>() {
            public boolean doFilter(ChunkedArgumentExtraction arg) {
                if (arg.getLength() == 1 && arg.getToken(0).length() == 1) {
                    return arg.getToken(0).matches("[a-zA-ZöäüßÖÄÜ]");
                } else {
                    return true;
                }
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
