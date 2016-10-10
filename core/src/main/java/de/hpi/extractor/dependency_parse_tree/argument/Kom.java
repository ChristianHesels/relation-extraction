package de.hpi.extractor.dependency_parse_tree.argument;

import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;


/**
 * Represents the typed dependency 'comparison'
 */
public class Kom extends Argument2 {

    private Node preopsition;

    public Kom(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation, "KOM");
        this.preopsition = null;

        // the noun of this object is connected via 'cj' to the preposition,
        // which is the root of an 'kom'
        // if there exists a 'cj', it becomes root, because possible conjunctions
        // are connected to it
        // the preposition is stored and added to each extraction in the end
        List<Node> pn = rootNode.getChildrenOfType("cj");
        if (pn.size() == 1) {
            this.rootNode = pn.get(0);
            this.preopsition = rootNode;
        }
    }

    @Override
    public Role getRole() {
        // if there is no preposition, 'kom' is not a valid argument
        if (preopsition == null || !this.containsNoun()) {
            return Role.NONE;
        }
        return Role.BOTH;
    }

    @Override
    public Node getPreposition() {
        return preopsition;
    }

}

