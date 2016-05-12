package edu.washington.cs.knowitall.extractor.chunking.mapper;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedArgumentExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedExtraction;

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

        // Can't match "ARG ... ,/and ... REL"
        int commaCount = 0;
        int conjunctionCount = 0;
        for (int i = argEnd; i < relStart; i++) {
            if (sent.getPosTag(i).equals("$,")) {
                commaCount++;
            } else if (sent.getPosTag(i).equals("KON")) {
                conjunctionCount++;
            }
        }
        if (commaCount > 0 || conjunctionCount > 0) {
            return false;
        }

        return true;

    }

}
