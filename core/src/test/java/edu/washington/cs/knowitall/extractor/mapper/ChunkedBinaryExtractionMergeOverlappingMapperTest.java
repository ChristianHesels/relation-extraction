package edu.washington.cs.knowitall.extractor.mapper;

import com.google.common.collect.Iterables;

import org.junit.Test;

import edu.washington.cs.knowitall.extractor.TestExtractions;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

import static org.junit.Assert.assertEquals;

public class ChunkedBinaryExtractionMergeOverlappingMapperTest {

    @Test
    public void testMerge1() throws Exception {
        ChunkedBinaryExtractionMergeOverlappingMapper mapper = new ChunkedBinaryExtractionMergeOverlappingMapper();

        Iterable<ChunkedBinaryExtraction> extractionList = mapper.doMap(TestExtractions.extractions.subList(0, 2));

        assertEquals(1, Iterables.size(extractionList));
    }

    @Test
    public void testMerge2() throws Exception {
        ChunkedBinaryExtractionMergeOverlappingMapper mapper = new ChunkedBinaryExtractionMergeOverlappingMapper();

        Iterable<ChunkedBinaryExtraction> extractionList = mapper.doMap(TestExtractions.extractions.subList(1, 3));

        assertEquals(2, Iterables.size(extractionList));
    }
}
