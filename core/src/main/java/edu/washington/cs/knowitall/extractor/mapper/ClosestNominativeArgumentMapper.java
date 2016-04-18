package edu.washington.cs.knowitall.extractor.mapper;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;
import edu.washington.cs.knowitall.nlp.morphology.Morphy;
import edu.washington.cs.knowitall.util.DefaultObjects;

/**
 * A mapper object for <code>ChunkedArgumentExtraction</code> objects that returns the object,
 * which is in nominative, and closest to the relation.
 */
public class ClosestNominativeArgumentMapper extends
                                   MaxMapper<Integer, ChunkedArgumentExtraction> {

    private boolean test = false;

    public ClosestNominativeArgumentMapper() {
    }

    public ClosestNominativeArgumentMapper(boolean test) {
        this.test = test;
    }

    @Override
    /**
     * Returns the distance between <code>arg</code> and its relation, in number of words.
     */
    public Integer doValueMap(ChunkedArgumentExtraction arg) {
        Morphy morphy = null;
        try {
            morphy = DefaultObjects.getMorphy(test);
        } catch (IOException e) {
            System.out.println("Could not load Morphy!");
        }

        if (morphy != null) {
            boolean isNominative = true;
            List<String> posTags = arg.getPosTags();
            for (int i = 0; i < posTags.size(); i++) {
                if (posTags.get(i).equals("NN") || posTags.get(i).equals("NE")) {
                    String token = arg.getToken(i);
                    try {
                        if (! morphy.isNominative(token)) {
                            isNominative = false;
                        }
                    } catch (NoSuchElementException e) {
                        // no information
                        // System.out.println("Key not found: " + arg.getToken(i));
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
