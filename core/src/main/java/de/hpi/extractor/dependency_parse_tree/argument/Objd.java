package de.hpi.extractor.dependency_parse_tree.argument;

import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;


/**
 * Represents the typed dependency 'dative object'
 */
public class Objd extends Argument2 {

    public Objd(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation, "OBJD");
    }

    @Override
    public Role getRole() {
        // if the argument is a pronoun, it belongs to the complement group
        if (rootNode.getPosGroup().equals("PRO") || !this.containsNoun()) {
            return Role.COMPLEMENT;
        }
        return Role.BOTH;
    }

    @Override
    public Node getPreposition() {
        return null;
    }

}
