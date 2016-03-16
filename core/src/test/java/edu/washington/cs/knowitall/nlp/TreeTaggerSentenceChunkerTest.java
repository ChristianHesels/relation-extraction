package edu.washington.cs.knowitall.nlp;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TreeTaggerSentenceChunkerTest {

    @Test
    public void testChunkSentence() throws IOException {
        String sent = "Die Commerzbank ist eine führende, international agierende Geschäftsbank mit Standorten in mehr als 50 Ländern.";

        ChunkedSentence expected = new ChunkedSentence(
            new String[] { "Die", "Commerzbank", "ist", "eine", "führende", ",", "international", "agierende", "Geschäftsbank", "mit", "Standorten", "in", "mehr", "als", "50", "Ländern", "." },
            new String[] { "ART", "NE", "VAFIN", "ART", "ADJA", "$,", "ADJD", "ADJA", "NN", "APPR", "NN", "APPR", "PIAT", "KOKOM", "CARD", "NN", "$." },
            new String[] { "B-NP", "I-NP", "B-VP", "B-NP", "I-NP", "O", "O", "B-NP", "I-NP", "B-PP", "I-PP", "B-PP", "I-PP", "I-PP", "I-PP", "I-PP", "O" }
        );

        TreeTaggerSentenceChunker chunker = new TreeTaggerSentenceChunker();
        ChunkedSentence actual = chunker.chunkSentence(sent);

        assertEquals(expected, actual);
        assertEquals(expected, actual);
    }

}
