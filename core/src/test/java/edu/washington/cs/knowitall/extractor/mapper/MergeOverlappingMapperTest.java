package edu.washington.cs.knowitall.extractor.mapper;


import org.junit.Before;
import org.junit.Test;

import edu.washington.cs.knowitall.extractor.ExtractorUnion;
import edu.washington.cs.knowitall.extractor.RegexExtractor;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;

import static org.junit.Assert.assertEquals;

public class MergeOverlappingMapperTest {
	
	ExtractorUnion<ChunkedSentence, ChunkedExtraction> e1;
	ExtractorUnion<ChunkedSentence, ChunkedExtraction> e2;
	ExtractorUnion<ChunkedSentence, ChunkedExtraction> e3;

	@Before
	public void setUp() throws Exception {
		RegexExtractor r1 = new RegexExtractor("ist_tok");
		RegexExtractor r2 = new RegexExtractor("ist_tok Professor_tok für_tok");
		RegexExtractor r3 = new RegexExtractor("ist_tok Professor_tok für_tok Biologie_tok an_tok der_tok");
		
		e1 = new ExtractorUnion<ChunkedSentence, ChunkedExtraction>();
		e1.addExtractor(r1);
		e1.addExtractor(r2);
		e1.addMapper(new MergeOverlappingMapper());
		
		e2 = new ExtractorUnion<ChunkedSentence, ChunkedExtraction>();
		e2.addExtractor(r1);
		e2.addExtractor(r3);
		e2.addMapper(new MergeOverlappingMapper());
		
		e3 = new ExtractorUnion<ChunkedSentence, ChunkedExtraction>();
		e3.addExtractor(r2);
		e3.addExtractor(r3);
		e3.addMapper(new MergeOverlappingMapper());
	}
	
	@Test
	public void testMerge() throws Exception {
		ChunkedSentence sent = new ChunkedSentence(
			new String[] { "Er", "ist", "Professor", "für", "Biologie", "an", "der", "Universität", "." },
            new String[] { "PPER", "VAFIN", "NN", "APPR", "NN", "APPR", "ART", "NN", "." },
            new String[] { "B-NP", "B-VP", "B-NP", "B-PP", "I-PP", "B-PP", "I-PP", "I-PP", "O" }
        );
		
		String result1 = e1.extract(sent).iterator().next().toString();
		assertEquals("ist Professor für", result1);
		
		String result2 = e2.extract(sent).iterator().next().toString();
		assertEquals("ist Professor für Biologie an der", result2);
		
		String result3 = e3.extract(sent).iterator().next().toString();
		assertEquals("ist Professor für Biologie an der", result3);
		
	}

}
