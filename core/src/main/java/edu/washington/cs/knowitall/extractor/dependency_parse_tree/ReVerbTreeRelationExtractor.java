package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;


public class ReVerbTreeRelationExtractor extends Extractor<DependencyParseTree, Node> {

    /**
     * Definition of the "verb" of the relation pattern.
     */
    public static final String VERB =
        // Modal or other verbs
        "VVFIN_pos VVIMP_pos VVINF_pos VVIZU_pos VVPP_pos VAFIN_pos VAIMP_pos VAINF_pos VAPP_pos VMFIN_pos VMINF_pos VMPP_pos PTKVZ_pos";


    @Override
    protected Iterable<Node> extractCandidates(DependencyParseTree source)
        throws ExtractorException {
        return source.find(VERB);
    }

}

