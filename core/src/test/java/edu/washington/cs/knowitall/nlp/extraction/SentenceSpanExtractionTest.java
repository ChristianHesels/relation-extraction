package edu.washington.cs.knowitall.nlp.extraction;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SentenceSpanExtractionTest {
    
    private ChunkedSentence sent;
    private ChunkedSentence sameSent;
    private ChunkedSentence otherSent;

    @Before
    public void setUp() throws Exception {
        String[] tokens = "John Smith spazierte im März in Detroit .".split(" ");
        String[] pos = "NE NE VVFIN APPRART NN APPR NE $.".split(" ");
        String[] np = "B-NP I-NP B-VP B-PP I-PP B-PP I-PP O".split(" ");
        sent = new ChunkedSentence(tokens, pos, np);
        sameSent = sent.clone();
        tokens[0] = "Joe";
        otherSent = new ChunkedSentence(tokens, pos, np);
    }
    
    private List<String> split(String s) {
        String[] tokens = s.split(" ");
        List<String> ts = new ArrayList<String>(tokens.length);
        for (int i = 0; i < tokens.length; i++) ts.add(tokens[i]);
        return ts;
    }
    
    private List<Range> getRanges(int... args) {
        assert args.length % 2 == 0;
        List<Range> result = new ArrayList<Range>(args.length/2);
        for (int i = 0; i < args.length; i += 2) {
            Range range = new Range(args[i], args[i+1]);
            result.add(range);
        }
        return result;
    }
    
    private List<ChunkedExtraction> getExtractions(ChunkedSentence sent, int... args) {
        List<Range> ranges = getRanges(args);
        List<ChunkedExtraction> extrs = new ArrayList<ChunkedExtraction>();
        for (Range r : ranges) {
            extrs.add(new ChunkedExtraction(sent, r));
        }
        return extrs;
    }
    
    @Test
    public void test1() {
        List<Range> ranges = getRanges(0,2, 2,1, 3,4);
        SpanExtraction extr = 
            new SpanExtraction(sent, ranges);
        
        assertEquals(3, extr.getNumFields());
        assertEquals("John Smith", extr.getField(0).toString());
        assertEquals("John Smith", extr.getField("field0").toString());
        assertEquals("spazierte", extr.getField(1).toString());
        assertEquals("spazierte", extr.getField("field1").toString());
        assertEquals("im März in Detroit", extr.getField(2).toString());
        assertEquals("im März in Detroit", extr.getField("field2").toString());
        
        List<String> fieldNames = new ArrayList<String>(3);
        fieldNames.add("arg1");
        fieldNames.add("rel");
        fieldNames.add("arg2");
        extr = new SpanExtraction(sent, ranges, fieldNames);
        
        assertEquals("John Smith", extr.getField("arg1").toString());
        assertEquals("spazierte", extr.getField("rel").toString());
        assertEquals("im März in Detroit", extr.getField("arg2").toString());
    }
    
    @Test
    public void test4() {
        List<ChunkedExtraction> extrs = getExtractions(sent, 0,2, 2,1);
        extrs.add(new ChunkedExtraction(sameSent, new Range(3,4)));
        new SpanExtraction(extrs);
        assertTrue(true);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void test5() {
        List<ChunkedExtraction> extrs = getExtractions(sent, 0,2, 2,1);
        extrs.add(new ChunkedExtraction(otherSent, new Range(3,4)));
        new SpanExtraction(extrs);
    }
    
    @Test
    public void test6() {
        List<Range> ranges = getRanges(0,2, 2,1, 3,4);
        SpanExtraction extr = new SpanExtraction(sent, ranges);
        List<String> expected = split("B-field0 I-field0 B-field1 B-field2 I-field2 I-field2 I-field2 O");
        assertEquals(expected, extr.toBIOLayer());
        
        extr = new SpanExtraction(sent, ranges, split("arg1 rel arg2"));
        expected = split("B-arg1 I-arg1 B-rel B-arg2 I-arg2 I-arg2 I-arg2 O");
        assertEquals(expected, extr.toBIOLayer());
    }

}
