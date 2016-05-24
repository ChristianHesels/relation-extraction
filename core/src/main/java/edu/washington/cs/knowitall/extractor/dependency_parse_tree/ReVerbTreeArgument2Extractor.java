package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.InnerNode;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class ReVerbTreeArgument2Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    private static final String HEAD = "HD_lab";
    private static final String OBJECT = "OA_lab OC_lab OG_lab OP_lab PD_lab";

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        // find object nodes
        List<Node> objectNodes = rel.getSentenceRoot().findNodes(OBJECT);

        assert(objectNodes.size() <= 1);

        // check if subject exists
        // sentences with no subject, e.g., "Es ist schönes Wetter."
        if (objectNodes.isEmpty()) {
            return new ArrayList<>();
        }

        // get node ids
        List<Integer> ids = collectIds(objectNodes.get(0));

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

        findRecursive(rootNode, ids);

        return ids;
    }

    /**
     * Find all subject nodes recursively and add the id of the subject nodes to the given list.
     * @param root  the root node (start node)
     * @param ids   the list of subject node ids
     */
    private void findRecursive(Node root, List<Integer> ids) {
        // if we reached a leaf node, we are done
        if (root.isLeafNode()) {
            return;
        }

        // get the subject ids
        ids.addAll(root.findLeafs(HEAD));

        // get all subjects connected to root
        List<Node> nodes = root.findNodes(OBJECT);
        assert(nodes.size() <= 1);

        // if no subjects exists, we can stop
        if (nodes.isEmpty()) {
            return;
        }

        // recursive call
        for (Node n : nodes) {
            findRecursive(n, ids);
        }
    }


}
