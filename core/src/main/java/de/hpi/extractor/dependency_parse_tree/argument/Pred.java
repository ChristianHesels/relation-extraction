package de.hpi.extractor.dependency_parse_tree.argument;

import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

/**
 * Represents the typed dependency 'predicative complement'
 */
public class Pred extends Argument2 {

    public Pred(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation, "PRED");
        }

    @Override
    public Role getRole() {
        // If 'pred' is not a noun, it is a adverb/adjective and thus, a complement
        if (this.rootNode.getPosGroup().equals("N"))
            return Role.OBJECT;

        return Role.COMPLEMENT;
    }

    @Override
    public Node getPreposition() {
        return null;
    }

}
