package edu.washington.cs.knowitall.extractor.chunking.conf.featureset;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedExtraction;

/**
 * Parent class for any feature specific to the POS layer
 *
 * @author Rob
 */
public abstract class PosFeature extends ExtractionFeature {

    public static final String[] allVerbPosTags = new String[]{"VB", "VBD",
                                                               "VBG", "VBN", "VBP", "VBZ"};

    private Set<String> posTags;

    public PosFeature(String... posTags) {

        this(Arrays.asList(posTags));
    }

    public PosFeature(Collection<String> posTags) {

        this.posTags = new HashSet<String>();
        this.posTags.addAll(posTags);
    }

    @Override
    protected boolean testAtIndex(Integer index, ChunkedSentence sentence) {

        String posTag = sentence.getPosTag(index);
        return posTags.contains(posTag);
    }

    /**
     * Given a ChunkedBinaryExtraction, I need the implementation to tell me at which indices to
     * test my predicate.
     */
    @Override
    protected abstract Range rangeToExamine(ChunkedBinaryExtraction cbe);

    /**
     * Get a feature that fires if any pos tag from posTags is present within Arg 2.
     */
    public static PosFeature withinArg2(String... posTags) {
        return new PosFeature(posTags) {
            @Override
            protected Range rangeToExamine(ChunkedBinaryExtraction cbe) {
                return cbe.getArgument2().getRange();
            }
        };
    }

    /**
     * Get a feature that fires if any pos tag from posTags is present within Arg 1.
     */
    public static PosFeature withinArg1(String... posTags) {
        return new PosFeature(posTags) {
            @Override
            protected Range rangeToExamine(ChunkedBinaryExtraction cbe) {
                return cbe.getArgument1().getRange();
            }
        };
    }

    /**
     * Get a feature that fires if any pos tag from posTags is present within a given single verb
     * relation (false if multiple-token rel)
     */
    public static PosFeature relSingleVerb() {
        return new PosFeature(PosFeature.allVerbPosTags) {
            @Override
            protected Range rangeToExamine(ChunkedBinaryExtraction cbe) {

                Range range = cbe.getRelation().getRange();
                if (range.getLength() == 1) {
                    return range;
                } else {
                    return Range.EMPTY;
                }
            }
        };
    }

    /**
     * Get a feature that fires if any pos tag from posTags is present within the relation.
     */
    public static PosFeature withinRel(String... posTags) {
        return new PosFeature(posTags) {
            @Override
            protected Range rangeToExamine(ChunkedBinaryExtraction cbe) {
                return cbe.getRelation().getRange();
            }
        };
    }

    /**
     * Get a feature that fires if any pos tag from posTags is present at the index immediately
     * before arg1.
     */
    public static PosFeature rightBeforeArg1(String... posTags) {
        return new PosFeature(posTags) {
            @Override
            protected Range rangeToExamine(ChunkedBinaryExtraction cbe) {
                ChunkedArgumentExtraction arg1 = cbe.getArgument1();
                int index = arg1.getStart() - 1;
                if (index < 0 || index >= arg1.getSentence().getLength()) {
                    return Range.EMPTY;
                } else {
                    return Range.fromInterval(index, index + 1);
                }
            }
        };
    }

    /**
     * Get a feature that fires if any pos tag from posTags is present at the index immediately
     * after arg2.
     */
    public static PosFeature rightAfterArg2(String... posTags) {
        return new PosFeature(posTags) {
            @Override
            protected Range rangeToExamine(ChunkedBinaryExtraction cbe) {
                ChunkedArgumentExtraction arg2 = cbe.getArgument2();
                int index = arg2.getStart() + arg2.getLength();
                if (index < 0 || index >= arg2.getSentence().getLength()) {
                    return Range.EMPTY;
                } else {
                    return Range.fromInterval(index, index + 1);
                }
            }
        };
    }

    /**
     * Get a feature that fires if any pos tag from posTags is present immediately before the
     * relation.
     */
    public static PosFeature rightBeforeRel(String... posTags) {
        return new PosFeature(posTags) {
            @Override
            protected Range rangeToExamine(ChunkedBinaryExtraction cbe) {
                ChunkedExtraction rel = cbe.getRelation();
                int index = rel.getStart() - 1;
                if (index < 0 || index >= rel.getSentence().getLength()) {
                    return Range.EMPTY;
                } else {
                    return Range.fromInterval(index, index + 1);
                }
            }
        };
    }
}
