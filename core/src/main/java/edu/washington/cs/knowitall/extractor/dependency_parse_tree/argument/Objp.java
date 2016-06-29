package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;


import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;

public class Objp extends Argument2 {

    private Node preopsition;

    public Objp(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation);
        this.preopsition = null;

        // the noun of this object is connected via 'pn' to the preposition,
        // which is the root of an 'objp'
        // if there exists a 'pn', it becomes root, because possible conjunctions
        // are connected to it
        // the preposition is stored and added to each extraction in the end
        List<Node> pn = rootNode.getChildrenOfType("pn");
        if (pn.size() == 1) {
            this.rootNode = pn.get(0);
            this.preopsition = rootNode;
        }
    }

    @Override
    public Role getRole() {
        // if there is no preposition, 'objp' is most likely some adverb/adjective
        if (preopsition == null) {
            // we do not want a pronominal adverb as complement
            if (rootNode.toList().size() == 1 && rootNode.getPos().equals("PROAV")) {
                return Role.NONE;
            }
            return Role.COMPLEMENT;
        }
        return Role.BOTH;
    }

    @Override
    protected List<Integer> getIds(Node n) {
        // add the preposition node to the list of nodes
        List<Integer> ids = super.getIds(n);
        ids.add(0, this.preopsition.getId());
        return ids;
    }
}
