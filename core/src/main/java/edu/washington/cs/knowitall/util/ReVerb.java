package edu.washington.cs.knowitall.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class ReVerb<R, T> {

    protected boolean debug;

    /**
     * Constructor of ReVerb
     */
    public ReVerb() {
        this(false);
    }

    /**
     * Constructor of ReVerb
     * @param debug enable debug mode?
     */
    public ReVerb(boolean debug) {
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
