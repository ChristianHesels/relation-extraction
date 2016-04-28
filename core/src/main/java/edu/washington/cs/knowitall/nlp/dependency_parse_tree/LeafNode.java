package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import java.util.List;

/**
 * Represents a leaf node of a dependency parse tree.
 * A leaf node contains information about a word.
 */
public class LeafNode extends Node {

    public String word = "";
    public String morphology = "";
    public String label = "";
    public String pos = "";

    public LeafNode(String data) {
        parse(data);
    }

    /**
     * Parse the given data string.
     * @param data the data string.
     */
    @Override
    protected void parse(String data) {
        data = data.trim();
        // handle parentheses
        if (data.contains("\\)")) {
            data = data.replace("\\)", "#CP");
        } else if (data.contains("\\(")) {
            data = data.replace("\\(", "#OP");
        }
        // replace parentheses belonging to the tree structure
        data = data.replace("(", "").replace(")", "");

        // split line into 'information' and 'word'
        String[] parts = data.split(" ");
        assert(parts.length == 2);

        // split 'information' into 'pos', 'label', 'morphology'
        String[] subparts = parts[0].split("-");
        assert(subparts.length > 0);

        // set variables
        this.word = parts[1];
        // handle parentheses
        if (this.word.equals("#CP")) {
            this.word = ")";
        } else if (this.word.equals("#OP")) {
            this.word = "(";
        }

        this.pos = subparts[0].replace("\\", "");
        if (subparts.length > 1) {
            this.label = subparts[1];
        }
        if (subparts.length > 2) {
            this.morphology = subparts[2].split("\\\\/")[0];
        }
    }

    @Override
    public boolean matchLabel(List<String> labels) {
        return labels.contains(label);
    }

    @Override
    public boolean matchFeature(List<String> features) {
        return false;
    }

    @Override
    public boolean matchPosTag(List<String> posTags) {
        return posTags.contains(pos);
    }

    @Override
    public boolean isInnerNode() {
        return false;
    }

    @Override
    public boolean isLeafNode() {
        return true;
    }

    @Override
    public String getDataString() {
        String str = pos;

        if (!label.isEmpty()) {
            if (!str.isEmpty()) {
                str += "-" + label;
            } else {
                str = label;
            }
        }

        if (!morphology.isEmpty()) {
            if (!str.isEmpty()) {
                str += "-" + morphology;
            } else {
                str = morphology;
            }
        }

        str += " " + word;

        return str;
    }
}
