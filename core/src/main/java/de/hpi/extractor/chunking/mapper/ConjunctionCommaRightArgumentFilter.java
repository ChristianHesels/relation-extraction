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
public class ConjunctionCommaRightArgumentFilter extends FilterMapper<ChunkedArgumentExtraction> {

    @Override
    public boolean doFilter(ChunkedArgumentExtraction arg) {
        ChunkedExtraction rel = arg.getRelation();
        ChunkedSentence sent = arg.getSentence();
        int relEnd = rel.getStart() + rel.getLength();
        int argStart = arg.getStart();
        int argEnd = argStart + arg.getLength();
        int sentLen = sent.getLength();

        // Can't match "REL, ARG"
        if (argEnd < sentLen && sent.getTokens().get(relEnd).equals(",")
            && argStart == relEnd + 1) {
            return false;
        }

        // Can't match "REL, ARG"
        if (argEnd < sentLen && sent.getTokens().get(relEnd).equals(",")) {
            return false;
        }

        // Can't match "REL and ARG"
        if (argEnd < sentLen && sent.getTokens().get(relEnd).equals("und")
            && argStart == relEnd + 1) {
            return false;
        }

        return true;

    }

}
