package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper.ReVerbTreeArgument1Mappers;
import edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper.ReVerbTreeArgument2Mappers;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;


/**
 * Extracts binary relations from a sentence by analysing the dependency parse tree of that sentence.
 */
public class ReVerbIIIExtractor extends Extractor<DependencyParseTree, TreeBinaryExtraction> {

    // TODO add mapper: classifier, which decides if a relation is a relation or not

    private Extractor<TreeExtraction, TreeExtraction> arg1Extr;
    private Extractor<TreeExtraction, TreeExtraction> arg2Extr;
    private Extractor<Node, TreeExtraction> relExtr;

    /**
     * Default constructor.
     */
    public ReVerbIIIExtractor() {
        this.relExtr = new ReVerbTreeRelationExtractor();

        this.arg1Extr = new ReVerbTreeArgument1Extractor();
        arg1Extr.addMapper(new ReVerbTreeArgument1Mappers());

        this.arg2Extr = new ReVerbTreeArgument2Extractor();
        arg2Extr.addMapper(new ReVerbTreeArgument2Mappers());
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param considerAllArguments consider arguments of child nodes for root nodes and vice versa?
     */
    public ReVerbIIIExtractor(boolean considerAllArguments) {
        this.relExtr = new ReVerbTreeRelationExtractor();

        this.arg1Extr = new ReVerbTreeArgument1Extractor();
        arg1Extr.addMapper(new ReVerbTreeArgument1Mappers());

        this.arg2Extr = new ReVerbTreeArgument2Extractor(considerAllArguments);
        arg2Extr.addMapper(new ReVerbTreeArgument2Mappers());
    }

    @Override
    protected Iterable<TreeBinaryExtraction> extractCandidates(DependencyParseTree dependencyParseTree)
        throws ExtractorException {
        Collection<TreeBinaryExtraction> extrs = new ArrayList<>();
    
        // 1. remove not needed nodes from tree
        dependencyParseTree.prune();

        // 2. if tree has multiple root nodes, divide the tree in subtrees
        // TODO: add objc as root ?
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
                extrs.addAll(TreeBinaryExtraction.productOfArgs(dependencyParseTree, rel, arg1s, arg2s));
            }
        }

        return extrs;
    }


}

