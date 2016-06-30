package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper.DepReVerbArgument1Mappers;
import edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper.DepReVerbArgument2Mappers;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.Context;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.ContextType;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Extracts binary relations from a sentence by analysing the dependency parse tree of that sentence.
 */
public class DepReVerbExtractor extends Extractor<DependencyParseTree, TreeBinaryExtraction> {

    // TODO
    // add mapper: classifier, which decides if a relation is a relation or not
    // Add dictionary for abbreviations (?)

    private Extractor<TreeExtraction, TreeExtraction> arg1Extr;
    private Extractor<TreeExtraction, TreeExtraction> arg2Extr;
    private Extractor<Node, TreeExtraction> relExtr;

    /**
     * Default constructor.
     */
    public DepReVerbExtractor() {
        this.relExtr = new DepReVerbRelationExtractor();

        this.arg1Extr = new DepReVerbArgument1Extractor();
        arg1Extr.addMapper(new DepReVerbArgument1Mappers());

        this.arg2Extr = new DepReVerbArgument2Extractor();
        arg2Extr.addMapper(new DepReVerbArgument2Mappers());
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param considerAllArguments consider arguments of child nodes for root nodes and vice versa?
     * @param weSubject            extract we as subject?
     */
    public DepReVerbExtractor(boolean considerAllArguments, boolean weSubject) {
        this.relExtr = new DepReVerbRelationExtractor();

        this.arg1Extr = new DepReVerbArgument1Extractor();
        arg1Extr.addMapper(new DepReVerbArgument1Mappers(weSubject));

        this.arg2Extr = new DepReVerbArgument2Extractor(considerAllArguments);
        arg2Extr.addMapper(new DepReVerbArgument2Mappers());
    }

    @Override
    protected Iterable<TreeBinaryExtraction> extractCandidates(DependencyParseTree dependencyParseTree)
        throws ExtractorException {
        Collection<TreeBinaryExtraction> extrs = new ArrayList<>();

        // 1. remove not needed nodes from tree
        dependencyParseTree.prune();

        // 2. if tree has multiple root nodes, divide the tree in subtrees
        List<Node> rootElements = dependencyParseTree.getRootElements();

        // For each of the root elements:
        for (Node root : rootElements) {
            // 3. get the verbs
            Iterable<TreeExtraction> rels = relExtr.extract(root);

            for (TreeExtraction rel : rels) {
                // 4. extract the subject of the verb
                Iterable<TreeExtraction> arg1s = arg1Extr.extract(rel);

                // 5. extract the objects and complements of verbs
                // add the complements to the verb and create extraction for the objects
                Iterable<TreeExtraction> arg2s = arg2Extr.extract(rel);

                // 6. Create TreeBinaryExtractions
                extrs.addAll(TreeBinaryExtraction.productOfArgs(dependencyParseTree, getContext(root), rel, arg1s, arg2s));
            }
        }

        return extrs;
    }


    /**
     * Determines the context of the extraction.
     * If the extraction comes from the subordinate clause, the conjunction is also determined.
     * @param root the root of the clause
     * @return the context of the clause
     */
    private Context getContext(Node root) {
        Context context = new Context(ContextType.NONE);

        if (root.getLabelToParent().equals("root") || root.getLabelToParent().equals("objc")) {
            context = new Context(ContextType.MAIN_CLAUSE);

        } else if (root.getLabelToParent().equals("neb")) {
            List<Node> konj = root.getChildrenOfType("konj");
            if (!konj.isEmpty()) {
                context = new Context(ContextType.SUBORDINATE_CLAUSE, konj.get(0).getWord());
            } else {
                context = new Context(ContextType.SUBORDINATE_CLAUSE);
            }
        }

        return context;
    }

}

