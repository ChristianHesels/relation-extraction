package edu.washington.cs.knowitall.extractor.chunking.mapper;

import com.google.common.collect.Iterables;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;

import static org.junit.Assert.assertEquals;

public class SentenceMergeOverlappingMapperTest {

    ChunkedSentence sentence1;
    ChunkedSentence sentence2;
    ChunkedSentence sentence3;

    @Before
    public void setUp() throws Exception {
        sentence1 = new ChunkedSentence(
            new String[]{"Die", "Schale", "des", "Apfels", "ist", "gesund", "."},
            new String[]{"ART", "NN", "ART", "NN", "VAFIN", "ADJD", "$."},
            new String[]{"B-NP", "I-NP", "B-NP", "I-NP", "B-VP", "B-NP", "O"}
        );
        sentence2 = new ChunkedSentence(
            new String[]{"Die", "Schale", "des", "Apfels", "ist", "gesund", "rot", "."},
            new String[]{"ART", "NN", "ART", "NN", "VAFIN", "ADJD", "ADJD", "$."},
            new String[]{"B-NP", "I-NP", "B-NP", "I-NP", "B-VP", "B-NP", "B-NP", "O"}
        );
        sentence3 = new ChunkedSentence(
            new String[]{"Die", "Kerne", "des", "Apfels", "sind", "nicht", "lecker", "."},
            new String[]{"ART", "NN", "ART", "NN", "VAFIN", "NEG", "ADJD", "$."},
            new String[]{"B-NP", "I-NP", "B-NP", "I-NP", "B-VP", "O", "B-NP", "O"}
        );
    }

    @Test
    public void testMerge1() throws Exception {
        SentenceMergeOverlappingMapper mapper = new SentenceMergeOverlappingMapper();

        List<ChunkedSentence> sentences = new ArrayList<>();
        sentences.add(sentence1);
        sentences.add(sentence2);

        Iterable<ChunkedSentence> result = mapper.doMap(sentences);

        assertEquals(1, Iterables.size(result));

        ChunkedSentence actual = (ChunkedSentence) ((ArrayList) result).get(0);
        assertEquals("Die Schale des Apfels ist gesund rot .", actual.toString());
    }

    @Test
    public void testMerge2() throws Exception {
        SentenceMergeOverlappingMapper mapper = new SentenceMergeOverlappingMapper();

        List<ChunkedSentence> sentences = new ArrayList<>();
        sentences.add(sentence1);
        sentences.add(sentence3);

        Iterable<ChunkedSentence> result = mapper.doMap(sentences);

        assertEquals(2, Iterables.size(result));
    }
}
