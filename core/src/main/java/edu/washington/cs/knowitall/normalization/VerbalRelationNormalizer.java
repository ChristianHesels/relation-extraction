package edu.washington.cs.knowitall.normalization;

import com.google.common.base.Joiner;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedExtraction;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedRelationExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;
import edu.washington.cs.knowitall.sequence.SequenceException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that can be used to normalize verbal relation strings. It performs the following
 * normalization procedure on a {@link ChunkedExtraction} object: <ul> <li>Removes inflection in
 * each token using the {@link MateToolLemmatizer} class.</li> <li>Removes auxiliary verbs, determiners,
 * adjectives, and adverbs.</li> </ul>
 *
 * @author afader
 */
public class VerbalRelationNormalizer {

    private boolean stripAdj = false;
    private boolean lemmatize = false;
    private boolean replaceNNandART = false;

    private HashSet<String> ignorePosTags;
    private HashSet<String> auxVerbs;

    private MateToolLemmatizer lemmatizer = null;


    public VerbalRelationNormalizer() {
        ignorePosTags = new HashSet<String>();
        ignorePosTags.add("ART");
        ignorePosTags.add("ADV");
        ignorePosTags.add("PPOSAT");
        ignorePosTags.add("ADJA");
//        ignorePosTags.add("PROAV");

        auxVerbs = new HashSet<String>();
        auxVerbs.add("sein");
        auxVerbs.add("haben");
        auxVerbs.add("werden");
    }

    public VerbalRelationNormalizer(boolean lemmatize, boolean stripAdj, boolean replaceNNandART) {
        this();

        this.lemmatize = lemmatize;
        this.stripAdj = stripAdj;
        this.replaceNNandART = replaceNNandART;
    }


    /**
     * Normalizes the given field.
     */
    public TreeNormalizedField normalizeField(TreeExtraction extraction) {
        List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());
        if (extraction.getLastNodeId() != null) {
            nodes.add(extraction.getRootNode().find(extraction.getLastNodeId()));
        }

        List<String> tokens = nodes.stream().map(Node::getWord).collect(Collectors.toList());
        List<String> postags = nodes.stream().map(Node::getPos).collect(Collectors.toList());

        normalizeModify(tokens, postags);

        return new TreeNormalizedField(Joiner.on(" ").join(tokens));
    }

    /**
     * Normalizes the given field.
     */
    public NormalizedField normalizeField(ChunkedRelationExtraction field) {
        List<String> tokens = field.getTokens();
        List<String> posTags = field.getPosTags();

        List<String> subPosTags = new ArrayList<>();
        List<String> subTokens = new ArrayList<>();
        if (field.hasSubRelation()) {
            subTokens = field.getSubRelation().getTokens();
            subPosTags = field.getSubRelation().getPosTags();
        }

        ArrayList<String> tokensCopy = new ArrayList<String>(tokens.size() + subTokens.size());
        tokensCopy.addAll(tokens);
        tokensCopy.addAll(subTokens);
        ArrayList<String> posTagsCopy = new ArrayList<String>(posTags.size() + subPosTags.size());
        posTagsCopy.addAll(posTags);
        posTagsCopy.addAll(subPosTags);

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
        if (lemmatize && lemmatizer == null) {
            lemmatizer = new MateToolLemmatizer();
        }

        removeIgnoredPosTags(tokens, posTags);
        if (stripAdj) {
            removeAdj(tokens, posTags);
        }

        if (lemmatize) {
            tokens = lemmatizer.lemmatize(tokens);
            removeLeadingBeHave(tokens, posTags);
        }

        if (replaceNNandART) {
            replaceNounsArticels(tokens, posTags);
        }
    }

    private void replaceNounsArticels(List<String> tokens, List<String> tags) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tags.get(i).equals("ART")) {
                tokens.set(i, "ART");
            }
            if (tags.get(i).equals("NN") || tags.get(i).equals("NE")) {
                if (i - 1 >= 0 && tokens.get(i - 1).equals("NOUN")) {
                    tokens.remove(i);
                    tags.remove(i);
                } else {
                    tokens.set(i, "NOUN");
                }
            }
        }
    }

    private void removeIgnoredPosTags(List<String> tokens, List<String> tags) {
        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            String tag = tags.get(i);
            if (ignorePosTags.contains(tag)) {
                tokens.remove(i);
                tags.remove(i);
            } else {
                i++;
            }
        }
    }

    private void removeAdj(List<String> tokens, List<String> tags) {
        int i = 0;
        while (i < tokens.size()) {
            String tag = tags.get(i);
            if (tag.equals("ADJD")) {
                tokens.remove(i);
                tags.remove(i);
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
