package edu.washington.cs.knowitall.nlp.extraction;

import com.google.common.base.Joiner;

import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;

public class TreeBinaryExtraction implements ExtractionConverter {

    private DependencyParseTree tree;
    private List<Node> relationNodes;
    private List<Node> argument1Nodes;
    private List<Node> argument2Nodes;

    public TreeBinaryExtraction() {
    }

    public TreeBinaryExtraction(List<Node> relationNodes, List<Node> argument1Nodes, List<Node> argument2Nodes) {
        this.relationNodes = relationNodes;
        this.argument1Nodes = argument1Nodes;
        this.argument2Nodes = argument2Nodes;
    }

    public String toString() {
        return Joiner.on(" ").join(argument1Nodes) + " # " +
               Joiner.on(" ").join(relationNodes) + " # " +
               Joiner.on(" ").join(argument2Nodes);
    }

    public DependencyParseTree getTree() {
        return tree;
    }

    public void setTree(DependencyParseTree tree) {
        this.tree = tree;
    }

    public List<Node> getRelationNodes() {
        return relationNodes;
    }

    public void setRelationNodes(List<Node> relationNodes) {
        this.relationNodes = relationNodes;
    }

    public List<Node> getArgument1Nodes() {
        return argument1Nodes;
    }

    public void setArgument1Nodes(List<Node> argument1Nodes) {
        this.argument1Nodes = argument1Nodes;
    }

    public List<Node> getArgument2Nodes() {
        return argument2Nodes;
    }

    public void setArgument2Nodes(List<Node> argument2Nodes) {
        this.argument2Nodes = argument2Nodes;
    }

    @Override
    public SimpleBinaryRelation convert() {
        return new SimpleBinaryRelation(relationNodes.toString(), argument1Nodes.toString(), argument2Nodes.toString());
    }
}
