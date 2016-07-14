package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;


public class Kom extends Argument2 {

    private Node preopsition;

    public Kom(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation);
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
        if (preopsition == null) {
            return Role.NONE;
        }
        return Role.BOTH;
    }

    @Override
    public Node getPreposition() {
        return preopsition;
    }

}

