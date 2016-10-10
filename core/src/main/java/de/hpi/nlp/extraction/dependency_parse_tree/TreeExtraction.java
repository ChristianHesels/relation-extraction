package de.hpi.nlp.extraction.dependency_parse_tree;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import de.hpi.nlp.dependency_parse_tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeExtraction {

    private Node prepositionNode;
    private Node rootNode;
    private Iterable<Integer> nodeIds;
    private Iterable<Integer> konNodeIds;
    private Integer lastNodeId;

    public TreeExtraction(Node rootNode, Iterable<Integer> nodeIds) {
        this.rootNode = rootNode;
        this.nodeIds = nodeIds;
        this.prepositionNode = null;
    }

    public TreeExtraction(Node rootNode, int nodeId) {
        this.rootNode = rootNode;
        List<Integer> ids = new ArrayList<>();
        ids.add(nodeId);
        this.nodeIds = ids;
        this.prepositionNode = null;
    }

    public TreeExtraction(Node rootNode, Iterable<Integer> nodeIds, Node prepositionNode) {
        this.rootNode = rootNode;
        this.nodeIds = nodeIds;
        this.prepositionNode = prepositionNode;
    }

    public TreeExtraction(Node rootNode, int nodeId, Node prepositionNode) {
        this.rootNode = rootNode;
        List<Integer> ids = new ArrayList<>();
        ids.add(nodeId);
        this.nodeIds = ids;
        this.prepositionNode = prepositionNode;
    }

    public String toString() {
        List<String> words = rootNode.find(nodeIds).stream().map(Node::getWord).collect(Collectors.toList());
        if (lastNodeId != null) {
            words.add(rootNode.find(lastNodeId).getWord());
        }
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

    public Integer getLastNodeId() {
        return lastNodeId;
    }

    public void setLastNodeId(Integer lastNodeId) {
        this.lastNodeId = lastNodeId;
    }

    public Node getPrepositionNode() {
        return prepositionNode;
    }

    public void setPrepositionNode(Node prepositionNode) {
        this.prepositionNode = prepositionNode;
    }
}
