package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

public class Obja extends Argument2 {

    public Obja(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation);
    }

    @Override
    public Role getRole() {
        if (rootNode.getWord().equals("sich"))
            return Role.COMPLEMENT;

        return Role.BOTH;
    }

}
