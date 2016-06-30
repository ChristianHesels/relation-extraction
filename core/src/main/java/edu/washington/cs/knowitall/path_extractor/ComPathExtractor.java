package edu.washington.cs.knowitall.path_extractor;


import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;
import java.util.stream.Collectors;

public class ComPathExtractor {

    public TreeExtraction extract(DependencyParseTree source, int comp1, int comp2) {
        List<Node> nodes = source.shortestPath(comp1, comp2);

        return new TreeExtraction(source.getTree(), nodes.stream().map(Node::getId).collect(Collectors.toList()));
    }
}
