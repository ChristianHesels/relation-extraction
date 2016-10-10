package de.hpi.extractor.dependency_parse_tree.argument;

import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * Represents the typed dependency 'accusative object'
 */
public class Obja extends Argument2 {

    public Obja(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation, "OBJA");
    }

    @Override
    public Role getRole() {
        // 'sich' is a complement
        if (rootNode.getPos().equals("PRF") || !this.containsNoun())
            return Role.COMPLEMENT;

        return Role.BOTH;
    }

    @Override
    public Node getPreposition() {
        return null;
    }

}
