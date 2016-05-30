package edu.washington.cs.knowitall.extractor.chunking.mapper;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import edu.washington.cs.knowitall.extractor.MaxMapper;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedExtraction;
import edu.washington.cs.knowitall.nlp.morphology.Morphy;
import edu.washington.cs.knowitall.nlp.morphology.ZmorgeMorphology;
import edu.washington.cs.knowitall.util.DefaultObjects;

/**
 * A mapper object for <code>ChunkedArgumentExtraction</code> objects that returns the object,
 * which is in nominative, and closest to the relation.
 */
public class ClosestNominativeArgumentMapper extends
                                             MaxMapper<Integer, ChunkedArgumentExtraction> {

    private ZmorgeMorphology zmorge;
    private Morphy morphy;

    ClosestNominativeArgumentMapper() {
        this(false);
    }

    ClosestNominativeArgumentMapper(boolean test) {
        morphy = null;
        try {
            morphy = DefaultObjects.getMorphy(test);
        } catch (IOException e) {
            System.out.println("Could not load Morphy!");
        }
        zmorge = new ZmorgeMorphology();
    }

    @Override
    /**
     * Returns the distance between <code>arg</code> and its relation, in number of words.
     */
    public Integer doValueMap(ChunkedArgumentExtraction arg) {
        if (morphy != null) {
            boolean isNominative = true;
            List<String> posTags = arg.getPosTags();
            for (int i = 0; i < posTags.size(); i++) {
                // We are only interested in nouns
                if (posTags.get(i).equals("NN") || posTags.get(i).equals("NE")) {
                    String token = arg.getToken(i);
                    try {
                        // Use Morphy to determine the case of the token
                        if (! morphy.isNominative(token)) {
                            isNominative = false;
                        }
                    } catch (NoSuchElementException e) {
                        // if the Morphy lexicon does not contain the token, use zmorge
                        isNominative = zmorge.isNominative(token);
                    }
                }
            }

            if (!isNominative) {
                return -Integer.MAX_VALUE;
            }
        }

        ChunkedExtraction relation = arg.getRelation();
        int relStart = relation.getStart();
        int argStart = arg.getStart();
        int distance = Math.abs(argStart - relStart);
        return -distance; // return -distance since we want the minimum distance
    }

}
