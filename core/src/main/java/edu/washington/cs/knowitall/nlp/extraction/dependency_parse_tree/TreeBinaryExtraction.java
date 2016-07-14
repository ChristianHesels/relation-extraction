package edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.extraction.ExtractionConverter;
import edu.washington.cs.knowitall.nlp.extraction.SimpleBinaryRelation;

import java.util.ArrayList;
import java.util.Collection;

public class TreeBinaryExtraction implements ExtractionConverter {

    private TreeExtraction rel;
    private TreeExtraction arg1;
    private TreeExtraction arg2;
    private DependencyParseTree tree;
    private Context context;

    public TreeBinaryExtraction() {
    }

    public TreeBinaryExtraction(DependencyParseTree tree, Context context, TreeExtraction rel, TreeExtraction arg1, TreeExtraction arg2) {
        this.tree = tree;
        this.rel = rel;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.context = context;
    }

    public DependencyParseTree getTree() {
        return tree;
    }

    public void setTree(DependencyParseTree tree) {
        this.tree = tree;
    }

    public TreeExtraction getRel() {
        return rel;
    }

    public void setRel(TreeExtraction rel) {
        this.rel = rel;
    }

    public TreeExtraction getArg1() {
        return arg1;
    }

    public void setArg1(TreeExtraction arg1) {
        this.arg1 = arg1;
    }

    public TreeExtraction getArg2() {
        return arg2;
    }

    public void setArg2(TreeExtraction arg2) {
        this.arg2 = arg2;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Given a collection of arg1s, a collection of arg2s, and a relation, returns all (arg1, rel,
     * arg2) extractions, where arg1 and arg2 range over the given collections.
     *
     * @param tree  the dependency parse tree
     * @param rel   the relation
     * @param arg1s list of argument1
     * @param arg2s list of argument2
     * @return all (arg1, rel, arg2) extractions, where arg1 and arg2 range over the given collections.
     */
    public static Collection<TreeBinaryExtraction> productOfArgs(
        DependencyParseTree tree,
        Context context,
        TreeExtraction rel,
        Iterable<TreeExtraction> arg1s,
        Iterable<TreeExtraction> arg2s) {
        Collection<TreeBinaryExtraction> results = new ArrayList<>();

        for (TreeExtraction arg1 : arg1s) {
            for (TreeExtraction arg2 : arg2s) {
                if (!arg1.isEmtpy() && !arg2.isEmtpy()) {

                    rel = new TreeExtraction(rel.getRootNode(), rel.getNodeIds(), arg2.getPrepositionNode());
                    if (arg2.getPrepositionNode() != null) {
                        rel.setLastNodeId(arg2.getPrepositionNode().getId());
                    }

                    TreeBinaryExtraction extr = new TreeBinaryExtraction(tree, context, rel, arg1, arg2);
                    results.add(extr);
                }
            }

        }
        return results;
    }

    @Override
    public String toString() {
        return arg1.toString() + " # " + rel.toString() + " # " + arg2.toString() + " (" +  context.toString() + ")";
    }

    @Override
    public SimpleBinaryRelation convert() {
        return new SimpleBinaryRelation(rel.toString(),
                                        arg1.toString(),
                                        arg2.toString(),
                                        tree.getSentence(),
                                        tree.getConllFormat(),
                                        context.toString());
    }
}
