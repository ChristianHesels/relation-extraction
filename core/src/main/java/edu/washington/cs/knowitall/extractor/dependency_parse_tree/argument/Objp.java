package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;


import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

public class Objp extends Argument2 {

    public Objp(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation);
    }

    @Override
    public Role getRole() {
        return Role.COMPLEMENT;
    }

    @Override
    protected List<Node> resolveConjunction() {
        List<Node> konNodes = new ArrayList<>();
        Node.getKonNodes(this.rootNode, konNodes);

        List<Node> pn = this.rootNode.getChildrenOfType("pn");
        if (!pn.isEmpty()) Node.getKonNodes(pn.get(0), konNodes);

        return konNodes;
    }
}
