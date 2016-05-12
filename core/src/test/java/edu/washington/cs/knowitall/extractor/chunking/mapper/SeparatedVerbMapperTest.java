package edu.washington.cs.knowitall.extractor.chunking.mapper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeparatedVerbMapperTest {

    ChunkedRelationExtraction relation1;
    ChunkedRelationExtraction relation2;
    ChunkedRelationExtraction relation3;
    ChunkedRelationExtraction relation4;

    @Before
    public void setUp() throws Exception {
        ChunkedSentence sentence1 = new ChunkedSentence(
            new String[]{ "Das", "HPI", "ist", "in", "Potsdam", "gewesen", "."},
            new String[]{"ART", "NN", "VAFIN", "ART", "NN", "VVPP", "$."},
            new String[]{"B-NP", "I-NP", "B-VP", "B-PP", "I-PP", "B-VP", "O"}
        );
        ChunkedSentence sentence2 = new ChunkedSentence(
            new String[]{ "Das", "HPI", "ist", "in", "Potsdam", ",", "Deutschland", ",", "gewesen", "."},
            new String[]{"ART", "NN", "VAFIN", "ART", "NN", "$,", "NN", "$,", "VVPP", "$."},
            new String[]{"B-NP", "I-NP", "B-VP", "B-PP", "I-PP", "O", "B-NP", "O", "B-VP", "O"}
        );
        relation1 = new ChunkedRelationExtraction(sentence1, new Range(2, 1));
        relation2 = new ChunkedRelationExtraction(sentence1, new Range(5, 1));
        relation3 = new ChunkedRelationExtraction(sentence2, new Range(2, 1));
        relation4 = new ChunkedRelationExtraction(sentence2, new Range(8, 1));
    }

    @Test
    public void test1() throws Exception {
        SeparatedVerbMapper mapper = new SeparatedVerbMapper();

        List<ChunkedRelationExtraction> extractions = new ArrayList<>();
        extractions.add(relation1);
        extractions.add(relation2);

        Iterable<ChunkedRelationExtraction> mappedExtractions = mapper.doMap(extractions);

        ChunkedRelationExtraction extraction1 =
            (ChunkedRelationExtraction) ((ArrayList) mappedExtractions).get(0);
        assertTrue(extraction1.hasSubRelation());
        ChunkedRelationExtraction extraction2 =
            (ChunkedRelationExtraction) ((ArrayList) mappedExtractions).get(1);
        assertFalse(extraction2.hasSubRelation());
    }

    @Test
    public void test2() throws Exception {
        SeparatedVerbMapper mapper = new SeparatedVerbMapper();

        List<ChunkedRelationExtraction> extractions = new ArrayList<>();
        extractions.add(relation3);
        extractions.add(relation4);

        Iterable<ChunkedRelationExtraction> mappedExtractions = mapper.doMap(extractions);

        ChunkedRelationExtraction extraction1 =
            (ChunkedRelationExtraction) ((ArrayList) mappedExtractions).get(0);
        assertFalse(extraction1.hasSubRelation());
        ChunkedRelationExtraction extraction2 =
            (ChunkedRelationExtraction) ((ArrayList) mappedExtractions).get(1);
        assertFalse(extraction2.hasSubRelation());
    }
}
