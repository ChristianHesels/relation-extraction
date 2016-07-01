package edu.washington.cs.knowitall.util;


import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.DependencyParseTree;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.ParZuSentenceParser;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.Context;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.ContextType;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;
import edu.washington.cs.knowitall.path_extractor.ComPathExtractor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for ComPath.
 * ComPath extracts the relation between two given entities using the shortest path between the entities in
 * a dependency parse tree.
 */
public class ComPath {

    private ComPathExtractor extractor;

    /**
     * Constructor of ComPath
     */
    public ComPath() {
        this.extractor = new ComPathExtractor();
    }

    /**
     * Extracts the relation between the given entities in the parsed string.
     * The parsed string should represent a dependency parse tree in conll format.
     * The ids of the entities should match the nodes in the tree.
     * @param parsedString the parsed string
     * @param entities     the ids of the entities
     * @return a list of tree binary extractions representing the relations
     */
    public List<TreeBinaryExtraction> extractRelations(String parsedString, int... entities) {
        // Convert sentence into a dependency parse tree
        ParZuSentenceParser parser = new ParZuSentenceParser();
        List<DependencyParseTree> trees = parser.convert(Arrays.asList(parsedString.split("\n")));

        List<TreeBinaryExtraction> extractions = new ArrayList<>();

        // for each entity combination
        for (int i = 0; i < entities.length; i++) {
            for (int j = i + 1; j < entities.length; j++) {
                int entity1 = entities[i];
                int entity2 = entities[j];

                // Get the tree, which contains both entities
                DependencyParseTree tree = findTree(trees, entity1, entity2);
                if (tree == null) {
                    return null;
                }

                TreeExtraction extraction = extractor.extract(tree, entity1, entity2);
                if (extraction == null) {
                    continue;
                }

                // Extract relations
                extractions.add(new TreeBinaryExtraction(tree,
                        new Context(ContextType.NONE),
                        extraction,
                        new TreeExtraction(tree.getTree(), entity1),
                        new TreeExtraction(tree.getTree(), entity2)));
            }
        }

        return extractions;
    }


    /**
     * Extract the relation between the annotated entities from the given string.
     * The entities should be annotated with <comp>XXX</comp>.
     * Example:
     *  "<comp>Microsoft</comp> kauft <comp>LinkedIn</comp> für 26 Millionen US-Dollar."
     * @param annotatedString the annotated string
     * @return a list of tree binary extractions representing the relations
     */
    public List<TreeBinaryExtraction> extractRelations(String annotatedString) throws ExtractorException {
        List<String> entities = extractEntities(annotatedString);

        // Sentence is not annotated properly
        if (entities.isEmpty()) {
            throw new ExtractorException("Sentence is not annotated correctly or not enough distinct entities exists.");
        }

        // Replace the entities, so that the parser cannot split the entities
        // The entities are replaced with words, which have the same form for all cases
        String replaceWord = "";
        if (!annotatedString.contains("Frau")) {
            annotatedString = annotatedString.replaceAll("<comp>(.*?)</comp>", "Frau");
            replaceWord = "Frau";
        } else if (!annotatedString.contains("Kuh")) {
            annotatedString = annotatedString.replaceAll("<comp>(.*?)</comp>", "Kuh");
            replaceWord = "Kuh";
        } else {
            annotatedString = annotatedString.replaceAll("<comp>(.*?)</comp>", "Bucht");
            replaceWord = "Bucht";
        }
        final String word = replaceWord;

        // Convert sentence into a dependency parse tree
        ParZuSentenceParser parser = new ParZuSentenceParser();
        List<DependencyParseTree> trees = parser.parseSentence(annotatedString);

        // Find the tree containing the word
        DependencyParseTree tree = findTree(trees, word);
        if (tree == null) {
            return null;
        }
        // Get the ids of the two entities
        List<Integer> ids = tree.toList().stream()
                .filter(n -> n.getWord().equals(word))
                .map(Node::getId)
                .collect(Collectors.toList());

        if (ids.size() != entities.size()) {
            throw new ExtractorException("Parsing error");
        }

        List<TreeBinaryExtraction> extractions = new ArrayList<>();

        // for each entity combination
        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                int entity1 = ids.get(i);
                int entity2 = ids.get(j);

                // Extract relations
                TreeExtraction extraction = extractor.extract(tree, entity1, entity2);
                if (extraction == null) {
                    continue;
                }

                // Replace the tmp word with the entity name
                tree.find(entity1).setWord(entities.get(i));
                tree.find(entity2).setWord(entities.get(j));

                extractions.add(new TreeBinaryExtraction(tree,
                        new Context(ContextType.NONE),
                        extraction,
                        new TreeExtraction(tree.getTree(), entity1),
                        new TreeExtraction(tree.getTree(), entity2)));
            }
        }

        return extractions;
    }


    /**
     * @param trees   a list of trees
     * @param entity1 id of entity 1
     * @param entity2 id of entity 2
     * @return the tree, which contains both entity ids
     */
    private DependencyParseTree findTree(List<DependencyParseTree> trees, int entity1, int entity2) {
        return trees.stream().filter(t -> t.find(entity1) != null && t.find(entity2) != null).findFirst().orElseGet(null);
    }

    /**
     * Finds and extracts the entities in the annotated sentence.
     * @param annotatedString the annotated sentence
     * @return the entities
     * @throws ExtractorException if the sentence is not annotated correctly
     */
    private List<String> extractEntities(String annotatedString) throws ExtractorException {
        List<String> entities = new ArrayList<>();

        Pattern pattern = Pattern.compile("<comp>(.*?)</comp>");
        Matcher matcher = pattern.matcher(annotatedString);
        while (matcher.find()) {
            entities.add(matcher.group(1));
        }

        // There are no entities at all, or all entities represent the same entity
        Set<String> uniqueEntities = new HashSet<>(entities);
        if (uniqueEntities.size() <= 1) {
            return new ArrayList<>();
        }

        return entities;
    }

    /**
     * @param trees the trees
     * @param word  the word
     * @return the tree, which contains the given word exactly two times.
     */
    private DependencyParseTree findTree(List<DependencyParseTree> trees, String word) {
        if (trees.size() == 1) {
            return trees.get(0);
        }
        return trees.stream().filter(t -> t.toList().stream().filter(n -> n.getWord().equals(word)).collect(Collectors.toList()).size() == 2).findFirst().orElseGet(null);
    }
}
