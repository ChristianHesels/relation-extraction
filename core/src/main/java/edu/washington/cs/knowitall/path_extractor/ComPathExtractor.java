package edu.washington.cs.knowitall.path_extractor;


import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;
import java.util.stream.Collectors;

public class ComPathExtractor {

    public TreeExtraction extract(DependencyParseTree source, int comp1, int comp2) {
//        System.out.println(source.getConllFormat());

        List<Node> nodes = source.shortestPath(comp1, comp2);

        // If there is a 'neb' node on the path, there can not be a valid relation
        List<Node> nebNodes = nodes.stream().filter(n -> n.getLabelToParent().equals("neb")).collect(Collectors.toList());
        if (!nebNodes.isEmpty()) {
            return null;
        }

        return new TreeExtraction(source.getTree(), nodes.stream().map(Node::getId).collect(Collectors.toList()));
    }

}
