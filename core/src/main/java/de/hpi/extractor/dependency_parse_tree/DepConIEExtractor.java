package de.hpi.extractor.dependency_parse_tree;

import de.hpi.extractor.Extractor;
import de.hpi.extractor.ExtractorException;
import de.hpi.extractor.dependency_parse_tree.mapper.DepConIEArgument1Mappers;
import de.hpi.extractor.dependency_parse_tree.mapper.DepConIEArgument2Mappers;
import de.hpi.extractor.dependency_parse_tree.mapper.DepRelationDictionaryFilter;
import de.hpi.extractor.dependency_parse_tree.mapper.PronounRelationFilter;
import de.hpi.nlp.dependency_parse_tree.DependencyParseTree;
import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.Context;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Extracts binary relations from a sentence by analysing the dependency parse tree of that sentence.
 */
public class DepConIEExtractor extends Extractor<DependencyParseTree, TreeBinaryExtraction> {

    // TODO
    // add mapper: classifier, which decides if a relation is a relation or not
    // improve 2nd argument extraction

    private Extractor<TreeExtraction, TreeExtraction> arg1Extr;
    private Extractor<TreeExtraction, TreeExtraction> arg2Extr;
    private Extractor<Node, TreeExtraction> relExtr;
    private ContextExtractor contextExtr;

    /**
     * Default constructor.
     */
    public DepConIEExtractor() {
        this.relExtr = new DepConIERelationExtractor();

        this.arg1Extr = new DepConIEArgument1Extractor();
        arg1Extr.addMapper(new DepConIEArgument1Mappers());

        this.arg2Extr = new DepConIEArgument2Extractor();
        arg2Extr.addMapper(new DepConIEArgument2Mappers());

        this.contextExtr = new ContextExtractor();

        this.addMapper(new DepRelationDictionaryFilter());
        this.addMapper(new PronounRelationFilter());
    }

    /**
     * Explicit constructor to invoke the corresponding super's constructor with arguments.
     *
     * @param minFreq the minimum number of distinct arg2s a relation must have to be included.
     * @param childArguments    extract second argument also from child nodes?
     * @param pronounsAsSubject consider pronouns as subject?
     * @param progressiveExtraction extract all extractions, which can be found (also those with many arguments)
     */
    public DepConIEExtractor(int minFreq, boolean childArguments, boolean pronounsAsSubject, boolean progressiveExtraction) {
        this.relExtr = new DepConIERelationExtractor();

        this.arg1Extr = new DepConIEArgument1Extractor();
        arg1Extr.addMapper(new DepConIEArgument1Mappers(pronounsAsSubject));

        this.arg2Extr = new DepConIEArgument2Extractor(childArguments, progressiveExtraction);
        arg2Extr.addMapper(new DepConIEArgument2Mappers());

        this.contextExtr = new ContextExtractor();

        if (minFreq > 0) {
            this.addMapper(new DepRelationDictionaryFilter(minFreq));
        }
        this.addMapper(new PronounRelationFilter(pronounsAsSubject));
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
                // add the complements to the verb phrase and create extractions for the objects
                Iterable<TreeExtraction> arg2s = arg2Extr.extract(rel);

                // Examine the context of the extraction
                Context context = contextExtr.extract(root);

                // 6. Create TreeBinaryExtractions
                extrs.addAll(TreeBinaryExtraction.productOfArgs(dependencyParseTree, context, rel, arg1s, arg2s));
            }
        }

        return extrs;
    }


}

