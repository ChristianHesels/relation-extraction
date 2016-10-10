package de.hpi.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class Extractor<R, T> {

    protected boolean debug;

    /**
     * Constructor of Extractor
     */
    public Extractor() {
        this(false);
    }

    /**
     * Constructor of Extractor
     * @param debug enable debug mode?
     */
    public Extractor(boolean debug) {
        this.debug = debug;
    }

    public abstract Iterable<T> extractRelationsFromString(String sentStr) throws IOException;
    public abstract Map<String, Iterable<T>> extractRelationsFromStrings(List<String> sentences)
        throws IOException;

    public abstract Iterable<T> extractRelationsFromParsedString(String sentStr) throws IOException;
    public abstract Map<String, Iterable<T>> extractRelationsFromParsedStrings(List<String> sentences)
        throws IOException;

    public abstract List<T> extractRelations(List<R> sentences);
    public abstract Iterable<T> extractRelations(R sentences);

}
