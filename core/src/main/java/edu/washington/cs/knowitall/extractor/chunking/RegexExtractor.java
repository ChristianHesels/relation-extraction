package edu.washington.cs.knowitall.extractor.chunking;

import java.util.ArrayList;
import java.util.Collection;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedRelationExtraction;
import edu.washington.cs.knowitall.sequence.LayeredTokenMatcher;
import edu.washington.cs.knowitall.sequence.LayeredTokenPattern;
import edu.washington.cs.knowitall.sequence.SequenceException;

/**
 * An extractor that uses a regular expression pattern relations from NP-chunked sentences. This
 * class uses a {@link LayeredTokenPattern} object to represent the regular expression pattern.
 *
 * @author afader
 */
public class RegexExtractor extends
                            Extractor<ChunkedSentence, ChunkedRelationExtraction> {

    private String patternString;
    private LayeredTokenPattern pattern;

    /**
     * Constructs a new instance using the given pattern.
     *
     * @param patternString the relation pattern
     * @throws SequenceException if unable to compile pattern
     */
    public RegexExtractor(String patternString) throws SequenceException {
        this.patternString = patternString;
        this.pattern = new LayeredTokenPattern(patternString);
    }

    /**
     * @return the String relation pattern.
     */
    public String getPatternString() {
        return patternString;
    }

    /**
     * @return the <code>LayeredTokenPattern</code> used to extract relations.
     */
    public LayeredTokenPattern getPattern() {
        return pattern;
    }

    @Override
    /**
     * Extracts relations matching the regular expression.
     */
    public Collection<ChunkedRelationExtraction> extractCandidates(
        ChunkedSentence sentence) throws ExtractorException {

        try {

            LayeredTokenMatcher m = pattern.matcher(sentence);
            Collection<ChunkedRelationExtraction> results = new ArrayList<ChunkedRelationExtraction>();

            while (m.find()) {
                int start = m.start();
                int length = m.end() - start;
                Range r = new Range(start, length);
                ChunkedRelationExtraction extr = new ChunkedRelationExtraction(sentence, r);
                results.add(extr);
            }
            return results;

        } catch (SequenceException e) {
            String msg = String.format("Couldn't extract from sentence '%s'",
                                       sentence);
            throw new ExtractorException(msg, e);
        }
    }

}
