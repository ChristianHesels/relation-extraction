package edu.washington.cs.knowitall.extractor.mapper;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;

/**
 * An argument filter that filters out any arguments matching the following patterns: <ul>
 * <li><code>ARG , REL</code></li> <li><code>ARG and REL</code></li> <li><code>ARG , and
 * REL</code></li> </ul>
 *
 * @author afader
 */
public class ConjunctionCommaLeftArgumentFilter extends FilterMapper<ChunkedArgumentExtraction> {

    @Override
    public boolean doFilter(ChunkedArgumentExtraction arg) {
        ChunkedExtraction rel = arg.getRelation();
        ChunkedSentence sent = arg.getSentence();
        int relStart = rel.getStart();
        int argEnd = arg.getStart() + arg.getLength();
        int sentLen = sent.getLength();

        // Can't match "ARG , REL"
        if (argEnd < sentLen - 1 && sent.getTokens().get(argEnd).equals(",")
            && relStart == argEnd + 1) {
            return false;
        }

        // Can't match "ARG and REL"
        if (argEnd < sentLen - 1 && sent.getTokens().get(argEnd).equals("und")
            && relStart == argEnd + 1) {
            return false;
        }

        // Can't match "ARG, and REL"
        if (argEnd < sentLen - 2 && sent.getTokens().get(argEnd).equals(",") && sent.getTokens()
            .get(argEnd + 1).equals("und") && relStart == argEnd + 2) {
            return false;
        }

        // Can't match "ARG ... , REL"
        int count = 0;
        for (int i = argEnd; i < relStart; i++) {
            if (sent.getToken(i).equals(",")) {
                count++;
            }
        }
        if (argEnd < sentLen - 2 && sent.getTokens().get(relStart - 1).equals(",") && count % 2 != 0) {
            return false;
        }

        return true;

    }

}
