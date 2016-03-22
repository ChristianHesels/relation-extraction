package edu.washington.cs.knowitall.normalization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;
import edu.washington.cs.knowitall.sequence.SequenceException;

/**
 * A class that can be used to normalize verbal relation strings. It performs the following
 * normalization procedure on a {@link ChunkedExtraction} object: <ul> <li>Removes inflection in
 * each token using the {@link MateToolLemmatizer} class.</li> <li>Removes auxiliary verbs, determiners,
 * adjectives, and adverbs.</li> </ul>
 *
 * @author afader
 */
public class VerbalRelationNormalizer implements FieldNormalizer {

    private boolean stripBeAdj = false;
    private HashSet<String> ignorePosTags;
    private HashSet<String> auxVerbs;
    private MateToolLemmatizer lemmatizer = new MateToolLemmatizer();

    /**
     * Constructs a new instance.
     */
    public VerbalRelationNormalizer() {
        // TODO
        ignorePosTags = new HashSet<String>();
        ignorePosTags.add("VMFIN"); // dürfen
        ignorePosTags.add("VMINF"); // wollen
        ignorePosTags.add("PTKNEG"); // nicht
        ignorePosTags.add("ART"); // der, die, das
        ignorePosTags.add("PDS"); // dieser, jener
        ignorePosTags.add("ADJA"); // adjectives
        ignorePosTags.add("ADV"); // adverbs
        ignorePosTags.add("PPOSAT"); // mein, deine

        auxVerbs = new HashSet<String>();
        auxVerbs.add("sein");
        auxVerbs.add("haben");
        auxVerbs.add("werden");
    }

    /**
     * If set to true, then will not remove adjectives in phrases like "is happy about".
     */
    public void stripBeAdj(boolean value) {
        stripBeAdj = value;
    }

    /**
     * Normalizes the given field.
     */
    public NormalizedField normalizeField(ChunkedExtraction field) {

        List<String> tokens = field.getTokens();
        List<String> posTags = field.getPosTags();

        ArrayList<String> tokensCopy = new ArrayList<String>(tokens.size());
        tokensCopy.addAll(tokens);
        ArrayList<String> posTagsCopy = new ArrayList<String>(posTags.size());
        posTagsCopy.addAll(posTags);

        normalizeModify(tokensCopy, posTagsCopy);

        try {
            return new NormalizedField(field, tokensCopy, posTagsCopy);
        } catch (SequenceException e) {
            String msg = String.format(
                "tokens and posTags are not the same length for field %s",
                field);
            throw new IllegalStateException(msg, e);
        }
    }

    private void normalizeModify(List<String> tokens, List<String> posTags) {
        tokens = lemmatizer.lemmatize(tokens);
        removeIgnoredPosTags(tokens, posTags);
        removeLeadingBeHave(tokens, posTags);
    }

    private void removeIgnoredPosTags(List<String> tokens, List<String> posTags) {
        boolean noNoun = true;
        for (int j = 0; j < posTags.size(); j++) {
            if (posTags.get(j).startsWith("N")) {
                noNoun = false;
                break;
            }
        }

        int i = 0;
        while (i < posTags.size()) {
            String tag = posTags.get(i);
            boolean isAdj = tag.startsWith("ADJ");

            /*
             * This is checking for a special case where the relation phrase
             * contains an adjective, but no noun. This covers cases like
             * "is high in" or "looks perfect for" where the adjective carries
             * most of the semantics of the relation phrase. In these cases, we
             * don't want to strip out the adjectives.
             */
            boolean keepAdj = isAdj && noNoun;
            if (ignorePosTags.contains(tag) && (!keepAdj || stripBeAdj)) {
                tokens.remove(i);
                posTags.remove(i);
            } else {
                i++;
            }
        }
    }

    private void removeLeadingBeHave(List<String> tokens, List<String> posTags) {
        int lastVerbIndex = -1;
        int n = tokens.size();
        for (int i = 0; i < n; i++) {
            String tag = posTags.get(n - i - 1);
            if (tag.startsWith("V")) {
                lastVerbIndex = n - i - 1;
                break;
            }
        }
        if (lastVerbIndex < 0) {
            return;
        }

        // remove auxiliary verbs before other verbs
        int i = 0;
        while (i < lastVerbIndex) {
            String tok = tokens.get(i);
            if (i + 1 < posTags.size() && !posTags.get(i + 1).startsWith("V")) {
                break;
            }
            if (auxVerbs.contains(tok)) {
                tokens.remove(i);
                posTags.remove(i);
                lastVerbIndex--;
            } else {
                i++;
            }
        }

        // remove auxiliary verbs after other verbs
        i = lastVerbIndex;
        while (i > 0 && i < tokens.size()) {
            String tok = tokens.get(i);
            if (i - 1 >= 0 && !posTags.get(i - 1).startsWith("V")) {
                break;
            }
            if (auxVerbs.contains(tok)) {
                tokens.remove(i);
                posTags.remove(i);
                lastVerbIndex--;
            } else {
                i--;
            }
        }
    }
}
