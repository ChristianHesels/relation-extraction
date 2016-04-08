package edu.washington.cs.knowitall.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.sequence.LayeredTokenMatcher;
import edu.washington.cs.knowitall.sequence.LayeredTokenPattern;
import edu.washington.cs.knowitall.sequence.SequenceException;

/**
 * An extractor that uses a regular expression pattern relations from NP-chunked sentences. This
 * class uses a {@link edu.washington.cs.knowitall.sequence.LayeredTokenPattern} object to represent the regular expression pattern.
 */
public class RegexSentenceExtractor extends
                            Extractor<ChunkedSentence, ChunkedSentence> {

    private String patternString;
    private LayeredTokenPattern pattern;

    /**
     * Constructs a new instance using the given pattern.
     *
     * @param patternString the relation pattern
     * @throws edu.washington.cs.knowitall.sequence.SequenceException if unable to compile pattern
     */
    public RegexSentenceExtractor(String patternString) throws SequenceException {
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
    public Collection<ChunkedSentence> extractCandidates(
        ChunkedSentence sentence) throws ExtractorException {

        try {
            LayeredTokenMatcher m = pattern.matcher(sentence);
            Collection<ChunkedSentence> results = new ArrayList<>();

            while (m.find()) {
                int start = m.start();
                int length = m.end() - start;
                Range range = new Range(start, length);

                List<String> tokens = sentence.getTokens(range);
                List<String> posTags = sentence.getPosTags(range);
                List<String> npChunks = sentence.getChunkTags(range);

                ChunkedSentence sent = new ChunkedSentence(tokens, posTags, npChunks);
                results.add(sent);
            }
            return results;

        } catch (SequenceException e) {
            String msg = String.format("Couldn't extract from sentence '%s'",
                                       sentence);
            throw new ExtractorException(msg, e);
        }
    }

}
