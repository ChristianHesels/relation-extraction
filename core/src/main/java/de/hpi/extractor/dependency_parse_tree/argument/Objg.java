package de.hpi.extractor.dependency_parse_tree.argument;

import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;


/**
 * Represents the typed dependency 'genitive object'
 */
public class Objg extends Argument2 {

    public Objg(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation, "OBJG");
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

