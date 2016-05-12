package edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree;

import com.google.common.collect.Iterables;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;

public class TreeExtraction {

    private Node sentenceRoot;
    private DependencyParseTree tree;
    private Iterable<Integer> nodeIds;

    public TreeExtraction() {
    }

    public TreeExtraction(DependencyParseTree tree, Iterable<Integer> nodeIds) {
        this.tree = tree;
        this.nodeIds = nodeIds;
    }

    public TreeExtraction(DependencyParseTree tree, Iterable<Integer> nodeIds, Node sentenceRoot) {
        this.tree = tree;
        this.nodeIds = nodeIds;
        this.sentenceRoot = sentenceRoot;
    }

    public String toString() {
        return tree.find(nodeIds).toString();
    }

    public int length() {
        return Iterables.size(nodeIds);
    }

    public boolean isEmtpy() {
        return length() == 0;
    }

    public DependencyParseTree getTree() {
        return tree;
    }

    public void setTree(DependencyParseTree tree) {
        this.tree = tree;
    }

    public Node getSentenceRoot() {
        return sentenceRoot;
    }

    public void setSentenceRoot(Node sentenceRoot) {
        this.sentenceRoot = sentenceRoot;
    }

    public Iterable<Integer> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(Iterable<Integer> nodeIds) {
        this.nodeIds = nodeIds;
    }
}
