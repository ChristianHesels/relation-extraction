package edu.washington.cs.knowitall.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.TreeBinaryExtraction;


public class ReVerbIIIExtractor extends Extractor<DependencyParseTree, TreeBinaryExtraction> {

    @Override
    protected Iterable<TreeBinaryExtraction> extractCandidates(DependencyParseTree dependencyParseTree)
        throws ExtractorException {

        Collection<TreeBinaryExtraction> extrs = new ArrayList<>();

        List<Node> subjectTree = dependencyParseTree.find("SB_lab");
        List<Node> verbTree = dependencyParseTree.find("VVFIN_pos VVIMP_pos VVINF_pos VVIZU_pos VVPP_pos VAFIN_pos VAIMP_pos VAINF_pos VAPP_pos VMFIN_pos VMINF_pos VMPP_pos PTKVZ_pos");
        List<Node> objectTree = dependencyParseTree.find("OA_lab OC_lab OG_lab OP_lab");

        if (subjectTree.size() > 0 && verbTree.size() > 0 && objectTree.size() > 0) {
            extrs.add(new TreeBinaryExtraction(verbTree, subjectTree, objectTree));
        }
        return extrs;
    }

}

