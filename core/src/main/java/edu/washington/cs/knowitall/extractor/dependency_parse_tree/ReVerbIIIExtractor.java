package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper.ReVerbTreeArgument1Mappers;
import edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper.ReVerbTreeArgument2Mappers;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;


public class ReVerbIIIExtractor extends Extractor<DependencyParseTree, TreeBinaryExtraction> {

    protected Extractor<DependencyParseTree, Node> arg1Extr;
    protected Extractor<DependencyParseTree, Node> arg2Extr;
    protected Extractor<DependencyParseTree, Node> relExtr;

    public ReVerbIIIExtractor() {
        this.relExtr = new ReVerbTreeRelationExtractor();

        this.arg1Extr = new ReVerbTreeArgumentExtractor("SB_lab");
        arg1Extr.addMapper(new ReVerbTreeArgument1Mappers());

        this.arg2Extr = new ReVerbTreeArgumentExtractor("OA_lab OC_lab OG_lab OP_lab");
        arg2Extr.addMapper(new ReVerbTreeArgument2Mappers());
    }

    @Override
    protected Iterable<TreeBinaryExtraction> extractCandidates(DependencyParseTree dependencyParseTree)
        throws ExtractorException {
        Collection<TreeBinaryExtraction> extrs = new ArrayList<>();

        Iterable<Node> subjectNodes = arg1Extr.extract(dependencyParseTree);
        Iterable<Node> objectNodes = arg2Extr.extract(dependencyParseTree);
        Iterable<Node> relationNodes = relExtr.extract(dependencyParseTree);

        if (!Iterables.isEmpty(subjectNodes) && !Iterables.isEmpty(objectNodes) && !Iterables.isEmpty(relationNodes)) {
            extrs.add(new TreeBinaryExtraction(relationNodes, subjectNodes, objectNodes));
        }

        return extrs;
    }

}

