package edu.washington.cs.knowitall.nlp.dependency_parse_tree;


import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a dependency parse tree.
 */
public class DependencyParseTree {

    private Node tree;
    private String sentence;
    private String conllFormat;

    public DependencyParseTree() {}

    public DependencyParseTree(Node tree) {
        this.tree = tree;
    }


    /**
     * Returns the tree as a list of node objects. The elements of the
     * list are generated from a pre-order traversal of the tree.
     * @return a list of nodes
     */
    public List<Node> toList() {
        return tree.toList();
    }

    /**
     * Returns a string representation of the tree. The elements are generated
     * from a pre-order traversal of the tree.
     * @return the string representation of the tree.
     */
    public String toString() {
        return tree.toString();
    }

    /**
     * Finds the nodes to the given ids.
     * @param ids a list of ids
     * @return a list of nodes
     */
    public List<Node> find(Iterable<Integer> ids) {
        return tree.find(ids);
    }

    /**
     * Find the node to the given id.
     * @param id id
     * @return a node or null
     */
    public Node find(int id) {
        return tree.find(id);
    }

    /**
     * Get the top level nodes of the tree.
     * There can be multiple words in the top level, if the dependency parser has split the
     * sentence.
     * @return a list of root nodes
     */
    public List<Node> getRootElements() {
        List<Node> subRoots = tree.find("s", "neb", "objc");
        List<Node> directRoots = this.tree.getChildren().stream()
            .filter(x -> !x.getPosGroup().equals("$.") && !x.getPosGroup().equals("$,") && !x.getPosGroup().equals("$("))
            .collect(Collectors.toList());

        subRoots.addAll(directRoots);

        return subRoots;
    }

    /**
     * Prunes nodes from this tree, which are not relevant.
     * Such nodes are:
     * rel, vok, par, empty labels, expl, kom
     */
    public void prune() {
        this.getTree().prune();
    }


    /**
     * Returns the shortest path between the start and end node.
     * @param startId start node id
     * @param endId   end node id
     * @return a list of nodes representing the shortest path
     */
    public List<Node> shortestPath(int startId, int endId) {
        Node start = find(startId);
        Node end = find(endId);

        if (start == null || end == null) {
            return new LinkedList<>();
        }

        // BFS
        Map<Node, Boolean> visited = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Queue<Node> q = new LinkedList<>();

        Node current = start;
        q.add(current);
        visited.put(current, true);

        while (!q.isEmpty()) {
            current = q.remove();
            // there is no path over the root
            if (current.getId() == 0) {
                continue;
            }
            // the end is reached
            if (current.equals(end)) {
                break;
            } else {
                // visited all unvisited neighbours
                for (Node node : current.getNeighbours()) {
                    if (!visited.containsKey(node)){
                        q.add(node);
                        visited.put(node, true);
                        previous.put(node, current);
                    }
                }
            }
        }

        List<Node> directions = new LinkedList<>();
        if (!current.equals(end)) {
            // There is no path between the two nodes
            return directions;
        }

        // Collect all nodes on the way
        for(Node node = end; node != null; node = previous.get(node)) {
            directions.add(node);
        }
        return Lists.reverse(directions);
    }

    /**
     * GETTER AND SETTER
     */

    public Node getTree() {
        return this.tree;
    }

    public void setTree(Node tree) {
        this.tree = tree;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getConllFormat() {
        return conllFormat;
    }

    public void setConllFormat(String conllFormat) {
        this.conllFormat = conllFormat;
    }

}
