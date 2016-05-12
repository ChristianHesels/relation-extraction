package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.InnerNode;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class ReVerbTreeArgument1Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    private static final String HEAD = "HD_lab PNC_lab";
    private static final String SUBJECT = "SB_lab";

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        // find subject nodes
        List<Node> subjectNodes = rel.getSentenceRoot().findNodes(SUBJECT);

        assert(subjectNodes.size() <= 1);

        // check if subject exists
        // sentences with no subject, e.g., "Es ist schönes Wetter."
        if (subjectNodes.isEmpty()) {
            return new ArrayList<>();
        }

        // get node ids
        List<Integer> ids = collectIds(subjectNodes.get(0));

        List<TreeExtraction> extrs = new ArrayList<>();
        if (!ids.isEmpty()) {
            extrs.add(new TreeExtraction(rel.getTree(), ids));
        }
        return extrs;
    }

    /**
     * Collect ids from nodes, which are subjects.
     * @param root the root
     * @return a list of ids
     */
    private List<Integer> collectIds(Node root) {
        InnerNode rootNode = (InnerNode) root;
        List<Integer> ids = new ArrayList<>();

        if (rootNode.feature.equals("CNP")) {
            // the subject is a coordinated noun phrase
            // get the sub phrases
            List<Node> subSubjects = rootNode.findNodes(SUBJECT);
            for (Node n : subSubjects) {
                findRecursive(n, ids);
            }
        } else {
            findRecursive(rootNode, ids);
        }

        return ids;
    }

    /**
     * Find all verb nodes recursively and add the id of the verb nodes to the given list.
     * @param root  the root node (start node)
     * @param ids   the list of verb node ids
     */
    private void findRecursive(Node root, List<Integer> ids) {
        // if we reached a leaf node, we are done
        if (root.isLeafNode()) {
            return;
        }

        // get the subject ids
        ids.addAll(root.findLeafs(HEAD));

        // get all subjects connected to root
        List<Node> nodes = root.findNodes(SUBJECT);
        assert(nodes.size() <= 1);

        // if no subjects exists, we can stop
        if (nodes.isEmpty()) {
            return;
        }

        // recursive call
        for (Node n : nodes.get(0).children) {
            findRecursive(n, ids);
        }
    }

}
