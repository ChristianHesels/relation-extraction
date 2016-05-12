package edu.washington.cs.knowitall.nlp.chunking;

public interface SentenceChunker {

    public ChunkedSentence chunkSentence(String sent) throws ChunkerException;
}
