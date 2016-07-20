package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

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
