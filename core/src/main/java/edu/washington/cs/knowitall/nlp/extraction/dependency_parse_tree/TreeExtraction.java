package edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;

public class TreeExtraction {

    private Node rootNode;
    private Iterable<Integer> nodeIds;
    private Iterable<Integer> konNodeIds;

    public TreeExtraction(Node rootNode, Iterable<Integer> nodeIds) {
        this.rootNode = rootNode;
        this.nodeIds = nodeIds;
    }

    public String toString() {
        List<String> words = rootNode.find(nodeIds).stream().map(Node::getWord).collect(Collectors.toList());
        return Joiner.on(" ").join(words);
    }

    public int length() {
        return Iterables.size(nodeIds);
    }

    public boolean isEmtpy() {
        return length() == 0;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public Iterable<Integer> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(Iterable<Integer> nodeIds) {
        this.nodeIds = nodeIds;
    }

    public Iterable<Integer> getKonNodeIds() {
        return konNodeIds;
    }

    public void setKonNodeIds(Iterable<Integer> konNodeIds) {
        this.konNodeIds = konNodeIds;
    }
}
