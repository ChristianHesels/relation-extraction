package edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

public class Objp extends Argument2 {

    private Node preopsition;

    public Objp(Node rootNode, TreeExtraction relation) {
        super(rootNode, relation);
        this.preopsition = rootNode;

        // the noun of this object is connected via 'pn' to the preposition, which is the root
        // of an 'objp'
        List<Node> pn = rootNode.getChildrenOfType("pn");
        assert(pn.size() == 1);
        this.rootNode = pn.get(0);
    }

    @Override
    public Role getRole() {
        return Role.BOTH;
    }

    @Override
    public List<TreeExtraction> createTreeExtractions() {
        List<TreeExtraction> extractions = new ArrayList<>();

        // Add the main object
        List<Integer> ids = getIds(this.rootNode);
        ids.add(0, this.preopsition.getId());
        extractions.add(new TreeExtraction(this.relation.getRootNode(), ids));

        // Add a extraction for each object in the conjunction
        extractions.addAll(resolveConjunction().stream()
                               .map(kon -> {
                                        List<Integer> konIds = getIds(kon);
                                        konIds.add(0, this.preopsition.getId());
                                        return new TreeExtraction(this.relation.getRootNode(), konIds);
                               })
                               .collect(Collectors.toList()));

        return extractions;
    }
}
