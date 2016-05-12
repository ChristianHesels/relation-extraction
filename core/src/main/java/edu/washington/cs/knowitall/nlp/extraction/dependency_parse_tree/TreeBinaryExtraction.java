package edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree;

import com.google.common.base.Joiner;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.ExtractionConverter;
import edu.washington.cs.knowitall.nlp.extraction.SimpleBinaryRelation;

public class TreeBinaryExtraction implements ExtractionConverter {

    private DependencyParseTree tree;
    private Iterable<Node> relationNodes;
    private Iterable<Node> argument1Nodes;
    private Iterable<Node> argument2Nodes;

    public TreeBinaryExtraction() {
    }

    public TreeBinaryExtraction(Iterable<Node> relationNodes, Iterable<Node> argument1Nodes, Iterable<Node> argument2Nodes) {
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

    public Iterable<Node> getRelationNodes() {
        return relationNodes;
    }

    public void setRelationNodes(Iterable<Node> relationNodes) {
        this.relationNodes = relationNodes;
    }

    public Iterable<Node> getArgument1Nodes() {
        return argument1Nodes;
    }

    public void setArgument1Nodes(Iterable<Node> argument1Nodes) {
        this.argument1Nodes = argument1Nodes;
    }

    public Iterable<Node> getArgument2Nodes() {
        return argument2Nodes;
    }

    public void setArgument2Nodes(Iterable<Node> argument2Nodes) {
        this.argument2Nodes = argument2Nodes;
    }

    @Override
    public SimpleBinaryRelation convert() {
        return new SimpleBinaryRelation(relationNodes.toString(),
                                        argument1Nodes.toString(),
                                        argument2Nodes.toString(),
                                        tree.toString(),
                                        tree.printTree());
    }
}
