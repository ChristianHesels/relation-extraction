package edu.washington.cs.knowitall.extractor;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class used to represent the composition of two <code>Extractor</code> objects.
 *
 * @author afader
 */
public class ExtractorComposition<R, S, T> extends Extractor<R, T> {

    private final Extractor<R, S> rsExtractor;
    private final Extractor<S, T> stExtractor;

    /**
     * Constructs a new extractor that is the composition of the given extractors.
     * @param rsExtractor extractor from type r to type s
     * @param stExtractor extractor from type s to type t
     */
    public ExtractorComposition(Extractor<R, S> rsExtractor, Extractor<S, T> stExtractor) {
        this.rsExtractor = rsExtractor;
        this.stExtractor = stExtractor;
    }

    @Override
    protected Collection<T> extractCandidates(R r) throws ExtractorException {
        Iterable<S> sExtrs = rsExtractor.extract(r);
        ArrayList<T> results = new ArrayList<T>();
        for (S extr : sExtrs) {
            Iterables.addAll(results, stExtractor.extract(extr));
        }
        return results;
    }

}
