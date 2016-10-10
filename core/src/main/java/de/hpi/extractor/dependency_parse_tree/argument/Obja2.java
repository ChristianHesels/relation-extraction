package de.hpi.extractor.dependency_parse_tree.argument;

import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;


/**
 * Represents the typed dependency '2nd accusative object'
 */
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

