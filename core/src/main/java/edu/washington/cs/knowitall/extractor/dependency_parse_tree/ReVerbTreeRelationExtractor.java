package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.InnerNode;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * Extracts the relation phrase from the tree.
 */
public class ReVerbTreeRelationExtractor extends Extractor<DependencyParseTree, TreeExtraction> {

    public static final String SENTENCE = "S_fea";
    public static final String VERB_PHRASE = "VP_lab CVP_lab";
    public static final String VERB = "VVFIN_pos VVINF_pos VVIZU_pos VAFIN_pos VAINF_pos VMINF_pos VMFIN_pos PTKVZ_pos VMPP_pos VAPP_pos VVPP_pos";


    @Override
    protected Iterable<TreeExtraction> extractCandidates(DependencyParseTree source)
        throws ExtractorException {
        List<TreeExtraction> rels = new ArrayList<>();

        // Get the sentence nodes
        List<Node> sentences = source.find(SENTENCE);

        for (Node sentenceNode : sentences) {
            List<Integer> rel = new ArrayList<>();

            // The verbs directly connected to the sentence represent the main verbs
            List<Integer> mainVerbs = sentenceNode.findLeafs(VERB);

            // The remaining part of the verbs can be found in verb phrases
            List<Node> verbPhrases = sentenceNode.findNodes(VERB_PHRASE);

            // There should be maximum one verb phrase node connected to a sentence node
            assert(verbPhrases.size() <= 1);

            if (verbPhrases.isEmpty()) {
                // there are only main verbs
                rel.addAll(mainVerbs);
                if (!rel.isEmpty()) {
                    rels.add(new TreeExtraction(source, rel, sentenceNode));
                }
            } else {
                // there are more verbs
                InnerNode verbPhraseNode = (InnerNode) verbPhrases.get(0);

                if (verbPhraseNode.feature.equals("VP")) {
                    // there exists a verb phrase with the other part of the verb
                    rel.addAll(mainVerbs);
                    rel.addAll(verbPhraseNode.findLeafs(VERB));
                    // verb phrases may be recursive
                    findVerbNodes(verbPhraseNode, rel);

                    rels.add(new TreeExtraction(source, rel, sentenceNode));

                } else if (verbPhraseNode.feature.equals("CVP")) {
                    // there exists a conjunction of verb phrases
                    // multiple TreeExtractions have to be created
                    List<Node> subVerPhrases = verbPhraseNode.findNodes(VERB_PHRASE);
                    for (Node subVerbPhraseNode : subVerPhrases) {
                        rel.addAll(mainVerbs);
                        rel.addAll(subVerbPhraseNode.findLeafs(VERB));
                        // verb phrases may be recursive
                        findVerbNodes(verbPhraseNode, rel);
                        rels.add(new TreeExtraction(source, rel, sentenceNode));
                        rel = new ArrayList<>();
                    }
                }
            }
        }

        return rels;
    }

    /**
     * Find all verb nodes recursively and add the id of the verb nodes to the given list.
     * @param root      the root node (start node)
     * @param verbIds   the list of verb node ids
     */
    private void findVerbNodes(Node root, List<Integer> verbIds) {
        // if we reached a leaf node, we are done
        if (root.isLeafNode()) {
            return;
        }

        // get all verb phrases connected to root
        List<Node> verbPhrases = root.findNodes(VERB_PHRASE);
        assert(verbPhrases.size() <= 1);

        // if no verb phrase exists, we can stop
        if (verbPhrases.isEmpty()) {
            return;
        }

        // get the verb node ids from the verb phrase
        InnerNode verbPhraseNode = (InnerNode) verbPhrases.get(0);
        if (verbPhraseNode.feature.equals("VP")) {
            verbIds.addAll(verbPhraseNode.findLeafs(VERB));
        }

        // recursive call
        for (Node n : verbPhraseNode.children) {
            findVerbNodes(n, verbIds);
        }
    }

}

