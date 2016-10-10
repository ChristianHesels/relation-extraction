package de.hpi.nlp.chunking;

public interface SentenceChunker {

    public ChunkedSentence chunkSentence(String sent) throws ChunkerException;
}
