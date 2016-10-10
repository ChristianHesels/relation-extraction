package de.hpi.extractor.dependency_parse_tree.mapper;

import de.hpi.extractor.FilterMapper;
import de.hpi.nlp.dependency_parse_tree.Node;
import de.hpi.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters out arguments, which do not contain a noun.
 */
public class ContainsNounFilter extends FilterMapper<TreeExtraction> {

    private boolean pronounsAsSubject;

    public ContainsNounFilter() {
        this(false);
    }

    public ContainsNounFilter(boolean pronounsAsSubject) {
        this.pronounsAsSubject = pronounsAsSubject;
    }

    @Override
    public boolean doFilter(TreeExtraction extraction) {
        List<Node> nodes = extraction.getRootNode().find(extraction.getNodeIds());

        List<Node> nounNodes = nodes.stream()
                .filter(x -> x.getPosGroup().equals("N") || x.getPosGroup().equals("FM") || (this.pronounsAsSubject && x.getPos().equals("PPER")))
                .filter(x -> x.toString().matches(".*[A-Za-zäöüßÖÄÜ].*"))
                .collect(Collectors.toList());

        return !nounNodes.isEmpty() ;
    }
}
