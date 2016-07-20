package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


public class Obja2 extends Argument2 {

    public Obja2(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation, "OBJA2");
    }

    @Override
    public Role getRole() {
        return Role.OBJECT;
    }

    @Override
    public Node getPreposition() {
        return null;
    }


}

