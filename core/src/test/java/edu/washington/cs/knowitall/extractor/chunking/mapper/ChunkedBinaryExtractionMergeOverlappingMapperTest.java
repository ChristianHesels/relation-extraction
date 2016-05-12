package edu.washington.cs.knowitall.extractor.chunking.mapper;

import com.google.common.collect.Iterables;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testMerge3() throws Exception {
        ChunkedBinaryExtractionMergeOverlappingMapper mapper = new ChunkedBinaryExtractionMergeOverlappingMapper();

        List<ChunkedBinaryExtraction> l = new ArrayList<>();
        l.add(TestExtractions.extractions.get(0));
        l.add(TestExtractions.extractions.get(0));

        Iterable<ChunkedBinaryExtraction> extractionList = mapper.doMap(l);

        assertEquals(1, Iterables.size(extractionList));
    }
}
