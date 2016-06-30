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

import java.util.Arrays;
import java.util.List;
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
     * Extracts the relation between the two given entities in the parsed string.
     * The parsed string should represent a dependency parse tree in conll format.
     * The ids of the entities should match the nodes in the tree.
     * @param parsedString the parsed string
     * @param entity1        the id of the first entity
     * @param entity2        the id of the second entity
     * @return a tree binary extraction representing the relation
     */
    public TreeBinaryExtraction extractRelations(String parsedString, int entity1, int entity2) {
        // Convert sentence into a dependency parse tree
        ParZuSentenceParser parser = new ParZuSentenceParser();
        List<DependencyParseTree> trees = parser.convert(Arrays.asList(parsedString.split("\n")));

        // Get the tree, which contains both entities
        DependencyParseTree tree = findTree(trees, entity1, entity2);
        if (tree == null) {
            return null;
        }

        // Extract relations
        return new TreeBinaryExtraction(tree,
                new Context(ContextType.NONE),
                extractor.extract(tree, entity1, entity2),
                new TreeExtraction(tree.getTree(), entity1),
                new TreeExtraction(tree.getTree(), entity2));

    }


    /**
     * Extract the relation between the two annotated entities from the given string.
     * The entities should be annotated with <comp>XXX</comp>.
     * Example:
     *  "<comp>Microsoft</comp> kauft <comp>LinkedIn</comp> für 26 Millionen US-Dollar."
     * @param annotatedString the annotated string
     * @return a tree binary extraction representing the relation
     */
    public TreeBinaryExtraction extractRelations(String annotatedString) throws ExtractorException {
        String[] entities = extractEntities(annotatedString);

        // Replace the entities, so that the parser cannot split the entities
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
        if (ids.size() != 2) {
            throw new ExtractorException("Wrong entity mapping!");
        }
        int entity1 = ids.get(0);
        int entity2 = ids.get(1);

        // Extract relations
        TreeExtraction extraction = extractor.extract(tree, entity1, entity2);

        // Replace the tmp word with the entity name
        tree.find(entity1).setWord(entities[0]);
        tree.find(entity2).setWord(entities[1]);

        return new TreeBinaryExtraction(tree,
                new Context(ContextType.NONE),
                extraction,
                new TreeExtraction(tree.getTree(), entity1),
                new TreeExtraction(tree.getTree(), entity2));
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
    private String[] extractEntities(String annotatedString) throws ExtractorException {
        String[] entities = new String[2];

        Pattern pattern = Pattern.compile("<comp>(.*?)</comp>");
        Matcher matcher = pattern.matcher(annotatedString);
        if (matcher.find()) {
            entities[0] = matcher.group(1);
        } else {
            throw new ExtractorException("Sentence is not annotated correctly!");
        }
        if (matcher.find()) {
            entities[1] = matcher.group(1);
        } else {
            throw new ExtractorException("Sentence is not annotated correctly!");
        }
        if (matcher.find()) {
            throw new ExtractorException("Sentence is not annotated correctly!");
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
