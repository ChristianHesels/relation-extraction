package edu.washington.cs.knowitall.extractor.dependency_parse_tree;


import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.Context;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.ContextType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContextExtractor {

    private List<String> subordinateConj;

    public ContextExtractor() {
        this.subordinateConj = null;
    }

    /**
     * Determines the context of the extraction.
     * If the extraction comes from the subordinate clause, the conjunction is also determined.
     * @param root the root of the clause
     * @return the context of the clause
     */
    public Context extract(Node root) {
        Context context = new Context(ContextType.NONE);

        if (root.getLabelToParent().equals("root")) {
            context = new Context(ContextType.MAIN_CLAUSE);

            // check if there exists a subordinate clause
            List<Node> nebNodes = root.getChildrenOfType("neb").stream()
                    .filter(n -> n.getChildrenOfType("konj").stream().filter(x -> getSubordinateConj().contains(x.getWord().toLowerCase())).collect(Collectors.toList()).size() > 0)
                    .collect(Collectors.toList());
            if (nebNodes.size() == 1) {
                context.setContextStr("Modifier: " + nebNodes.get(0).toString());
            }


        } else if (root.getLabelToParent().equals("objc")) {
            // check if objc is a that-clause
            List<Node> konj = root.getChildrenOfType("konj");
            if (konj.size() == 1 && konj.get(0).getWord().equals("dass")) {
                context = new Context(ContextType.THAT_CLAUSE);

                // extract
                Node verb = root.getParent();
                Node subj = null;
                if (verb != null) {
                    List<Node> subjs = verb.getChildrenOfType("subj");
                    if (subjs.size() == 1) {
                        subj = subjs.get(0);
                    }
                }

                if (verb != null && subj != null) {
                    context.setContextStr("Attribute: " + subj.toString() + " " + verb.getWord());
                }
            } else {
                context = new Context(ContextType.MAIN_CLAUSE);
            }


        } else if (root.getLabelToParent().equals("neb")) {
            context = new Context(ContextType.SUBORDINATE_CLAUSE);

            List<Node> konj = root.getChildrenOfType("konj");
            if (konj.size() == 1) {
                context.setContextStr("Conjunction: " + konj.get(0).getWord());
            }
        }


        return context;
    }


    private List<String> getSubordinateConj() {
        if (this.subordinateConj == null) {
            this.subordinateConj = new ArrayList<>();
            this.subordinateConj.add("als");
            this.subordinateConj.add("bevor");
            this.subordinateConj.add("bis");
            this.subordinateConj.add("damit");
            this.subordinateConj.add("ob");
            this.subordinateConj.add("obwohl");
            this.subordinateConj.add("seit");
            this.subordinateConj.add("sobald");
            this.subordinateConj.add("sofern");
            this.subordinateConj.add("soweit");
            this.subordinateConj.add("sowie");
            this.subordinateConj.add("während");
            this.subordinateConj.add("weil");
            this.subordinateConj.add("wenn");
            this.subordinateConj.add("wie");
            this.subordinateConj.add("wo");
        }

        return this.subordinateConj;
    }



}
