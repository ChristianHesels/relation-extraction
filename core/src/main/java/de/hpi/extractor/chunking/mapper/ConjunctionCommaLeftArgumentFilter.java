package de.hpi.extractor.chunking.mapper;

import de.hpi.extractor.FilterMapper;
import de.hpi.nlp.chunking.ChunkedSentence;
import de.hpi.nlp.extraction.chunking.ChunkedArgumentExtraction;
import de.hpi.nlp.extraction.chunking.ChunkedExtraction;

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

        // Can't match "ARG ... , REL"
        if (argEnd < sentLen - 1 && sent.getTokens().get(relStart - 1).equals(",")) {
            return false;
        }

        // Can't match "ARG and REL"
        if (argEnd < sentLen - 1 && sent.getTokens().get(argEnd).equals("und")
            && relStart == argEnd + 1) {
            return false;
        }

        return true;

    }

}
