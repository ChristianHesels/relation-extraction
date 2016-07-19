package edu.washington.cs.knowitall.extractor.dependency_parse_tree;

import edu.washington.cs.knowitall.extractor.Extractor;
import edu.washington.cs.knowitall.extractor.ExtractorException;
import edu.washington.cs.knowitall.extractor.dependency_parse_tree.argument.*;
import edu.washington.cs.knowitall.nlp.dependency_parse_tree.Node;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeExtraction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Extracts all candidates for objects and complements of the verb.
 */
public class DepReVerbArgument2Extractor extends Extractor<TreeExtraction, TreeExtraction> {

    private boolean childArguments;
    private boolean progressiveExtraction;

    public DepReVerbArgument2Extractor() {
        this(false, false);
    }

    /**
     * Creates a argument 2 extractor.
     * @param childArguments extract second argument also from child nodes?
     * @param progressiveExtraction extract all extractions, which can be found (also those with many arguments)
     */
    public DepReVerbArgument2Extractor(boolean childArguments, boolean progressiveExtraction) {
        this.childArguments = childArguments;
        this.progressiveExtraction = progressiveExtraction;
    }

    @Override
    protected Iterable<TreeExtraction> extractCandidates(TreeExtraction rel)
        throws ExtractorException {
        List<TreeExtraction> extrs = new ArrayList<>();

        // Extract all possible candidates
        List<Node> candidates = extractObjectComplementCandidates(rel);

        // There are no possible objects/complements of verb
        if (candidates.isEmpty()) return extrs;

        // Convert candidates into arguments
        List<Argument2> arguments = new ArrayList<>();
        for (Node n : candidates) {
            switch (n.getLabelToParent()) {
                case "objd":
                    arguments.add(new Objd(n, rel));
                    break;
                case "obja":
                    arguments.add(new Obja(n, rel));
                    break;
                case "obja2":
                    arguments.add(new Obja2(n, rel));
                    break;
                case "objg":
                    arguments.add(new Objg(n, rel));
                    break;
                case "objp":
                    arguments.add(new Objp(n, rel));
                    break;
                case "pred":
                    arguments.add(new Pred(n, rel));
                    break;
                case "pp":
                    arguments.add(new Pp(n, rel));
                    break;
                case "kom":
                    arguments.add(new Kom(n, rel));
                    break;
            }
        }

        // If there is only one object, add it to the list of extractions
        if (arguments.size() == 1) {
            Argument2 arg = arguments.get(0);
            if (arg.getRole() != Role.COMPLEMENT && arg.getRole() != Role.NONE) {
                extrs.addAll(arg.createTreeExtractions());
            }
            return extrs;
        }

        List<Argument2> complements = arguments.stream()
                .filter(x -> x.getRole() == Role.COMPLEMENT).collect(Collectors.toList());
        List<Argument2> objects = arguments.stream()
                .filter(x -> x.getRole() == Role.OBJECT).collect(Collectors.toList());
        List<Argument2> both = arguments.stream()
                .filter(x -> x.getRole() == Role.BOTH).collect(Collectors.toList());
        List<Argument2> none = arguments.stream()
                .filter(x -> x.getRole() == Role.NONE && x instanceof Objp).collect(Collectors.toList());

        // There is an argument, which requires more information
        // Extracting any kind of relation, would lead to uninformative and non factual relations
        if (!none.isEmpty()) {
            return extrs;
        }

        // Add 'sich' always as complement. It does not count as argument.
        if (!complements.isEmpty()) {
            Argument2 sich = complements.stream().filter(x -> x.getRootNode().getWord().equals("sich")).findAny().orElse(null);
            if (sich != null) {
                addToRelation(rel, sich);
                arguments.remove(sich);
            }
        }

        // If there are two arguments, create an extraction
        if (arguments.size() <= 2) {
            // Add the complements to the relation
            addToRelation(rel, complements);

            // Handle the arguments, which can be both
            if (objects.isEmpty() && !both.isEmpty()) {
                if (both.size() > 1) {
                    // the argument, which has the maximum distance to relation, is the object
                    Argument2 object = getObject(both);
                    extrs.addAll(object.createTreeExtractions());
                    // the other arguments are complements
                    both.remove(object);
                    addToRelation(rel, both);
                } else {
                    extrs.addAll(both.get(0).createTreeExtractions());
                }
                return extrs;
            }

            // Handle the arguments, which are objects
            if (!objects.isEmpty() && both.isEmpty()) {
                // Add the objects, which are close to the relation to the relation
                // The object with the maximum distance to the relation is the real object
                List<Argument2> c = new ArrayList<>();
                while (objects.size() != 1) {
                    Argument2 complement = getComplement(objects);
                    objects.remove(complement);
                    c.add(complement);
                }
                addToRelation(rel, c);
                extrs.addAll(objects.get(0).createTreeExtractions());
                return extrs;
            }

            // Handle the case of one object and one argument, which can be both
            if (objects.size() == 1 && both.size() == 1) {
                // The argument, which can be both, is the complement
                addToRelation(rel, both);
                // The object becomes the seconds argument
                extrs.addAll(objects.get(0).createTreeExtractions());
                return extrs;
            }
        }

        if (progressiveExtraction) {
            // There exists more than two arguments
            // Because we want coherent extraction, create a extraction for each argument
            arguments.forEach(a -> {
                if (a.getRole() != Role.COMPLEMENT && a.getRole() != Role.NONE) {
                    extrs.addAll(a.createTreeExtractions());
                }
            });
        }
        return extrs;
    }

    /**
     * Gets the argument, which has the longest distance to relation.
     * @param argument2s the arguments
     * @return the argument, which has the longest distance to relation
     */
    private Argument2 getObject(List<Argument2> argument2s) {
        Argument2 object = null;
        int maxDistance = 0;
        for (Argument2 arg : argument2s) {
            int currDistance = arg.distanceToRelation();
            if (currDistance > maxDistance) {
                maxDistance = currDistance;
                object = arg;
            }
        }
        return object;
    }

    /**
     * Gets the argument, which has the longest distance to relation.
     * @param argument2s the arguments
     * @return the argument, which has the longest distance to relation
     */
    private Argument2 getComplement(List<Argument2> argument2s) {
        Argument2 complement = null;
        int minDistance = Integer.MAX_VALUE;
        for (Argument2 arg : argument2s) {
            int currDistance = arg.distanceToRelation();
            if (currDistance < minDistance) {
                minDistance = currDistance;
                complement = arg;
            }
        }
        return complement;
    }

    /**
     * Adds the complement nodes to the given relation.
     * @param rel         the relation
     * @param complements the list of complements
     */
    private void addToRelation(TreeExtraction rel, List<Argument2> complements) {
        if (complements.isEmpty()) {
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (Argument2 arg : complements) {
            ids.addAll(arg.getIds(false));
            if (arg.getPreposition() != null) {
                ids.add(arg.getPreposition().getId());
            }
        }

        ids.addAll((Collection<? extends Integer>) rel.getNodeIds());
        rel.setNodeIds(ids);
    }

    private void addToRelation(TreeExtraction rel, Argument2 complement) {
        List<Argument2> complements = new ArrayList<>();
        complements.add(complement);
        addToRelation(rel, complements);
    }

    /**
     * Extract candidates for objects and verb complements.
     * @param rel relation extraction
     * @return list of root nodes of candidates
     */
    private List<Node> extractObjectComplementCandidates(TreeExtraction rel) {
        List<Node> relNodes = rel.getRootNode().find(rel.getNodeIds());
        // First check if there is an argument directed connected to main verb of the relation
        List<Node> fullVerbs = relNodes.stream().filter(x -> x.getPos().startsWith("VV") || x.getPos().equals("VAFIN")).collect(
            Collectors.toList());
        List<Node> arguments = getArguments(fullVerbs);
        // If not also consider the other verb forms
        if (arguments.isEmpty()) {
            arguments = getArguments(relNodes);
        }

        if (this.childArguments) {
            // Check if there are arguments connected to conjunction child nodes
            if (arguments.isEmpty()) {
                relNodes = rel.getRootNode().find(rel.getKonNodeIds());
                arguments = getArguments(relNodes);
            }
            // Check if the root node of the relation has an argument
            if (arguments.isEmpty()) {
                relNodes = new ArrayList<>();
                relNodes.add(rel.getRootNode());
                arguments = getArguments(relNodes);
            }
        }

        return arguments;
    }

    private List<Node> getArguments(List<Node> relNodes) {
        // objects are directly connected to verbs
        List<Node> arguments = new ArrayList<>();
        for (Node n : relNodes) {
            List<Node> a = n.getChildren().stream()
                .filter(x -> x.getLabelToParent().equals("obja") ||
                        x.getLabelToParent().equals("obja2") ||
                        x.getLabelToParent().equals("objd") ||
                        x.getLabelToParent().equals("objg") ||
                        x.getLabelToParent().equals("objp") ||
                        x.getLabelToParent().equals("pred") ||
                        (x.getLabelToParent().equals("kom") && x.getWord().equals("als")) ||
                        x.getLabelToParent().equals("pp"))
                .collect(Collectors.toList());
            arguments.addAll(a);
        }
        return arguments;
    }


    private void writeArgumentPattern(List<Argument2> arguments, TreeExtraction rel) {
        if (arguments.size() > 0) {
            String sentence = rel.getRootNode().toString();

            StringBuilder patternBuilder = new StringBuilder();
            for (Argument2 arg : arguments) {
                if (arg instanceof Pp) {
                    patternBuilder.append(" ");
                    patternBuilder.append(arg.getPreposition().getLabelToParent());
                    patternBuilder.append(" (");
                    patternBuilder.append(arg.getPreposition().getWord().toLowerCase());
                    patternBuilder.append(")");
                } else {
                    patternBuilder.append(" ");
                    String[] parts = arg.getClass().getName().split("\\.");
                    patternBuilder.append(parts[parts.length - 1]);
                }
            }
            String pattern = patternBuilder.toString();

            try {
                FileWriter fw = new FileWriter("argument2_patterns.txt", true);

                fw.write(sentence + "\t" + pattern);
                fw.write("\n");

                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
