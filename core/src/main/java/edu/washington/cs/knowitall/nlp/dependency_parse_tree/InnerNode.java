package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

import java.util.ArrayList;
import java.util.List;

public class InnerNode extends Node {

    public String feature = "";
    public String label = "";

    public InnerNode(String data, int id) {
        super(id);
        parse(data);
    }

    /**
     * Parse the given data string.
     * @param data the data string.
     */
    @Override
    protected void parse(String data) {
        data = data.trim();

        data = data.replace("(", "");

        // split line into 'feature' and 'label'
        String[] parts = data.split("-");
        assert(parts.length > 0);

        // set variables
        this.feature = parts[0];
        if (parts.length > 1) {
            this.label = parts[1].split("\\\\/")[0];
        }
    }

    @Override
    public boolean matchLabel(List<String> labels) {
        return labels.contains(label);
    }

    @Override
    public boolean matchFeature(List<String> features) {
        return features.contains(feature);
    }

    @Override
    public boolean matchPosTag(List<String> posTags) {
        return false;
    }

    @Override
    public boolean isInnerNode() {
        return true;
    }

    @Override
    public boolean isLeafNode() {
        return false;
    }

    @Override
    public String getDataString() {
        if (!feature.isEmpty() && !label.isEmpty()) {
            return feature + "-" + label;
        } else if (feature.isEmpty() && !label.isEmpty()) {
            return label;
        } else if (!feature.isEmpty()) {
            return feature;
        }
        return "";
    }

    @Override
    public List<Integer> findLeafs(String pattern) {
        // TODO allow combinations

        String[] parts = pattern.split(" ");

        List<String> posTags = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (String p : parts) {
            if (p.endsWith("_pos")) {
                posTags.add(p.substring(0, p.length() - 4));
            } else if (p.endsWith("_lab")) {
                labels.add(p.substring(0, p.length() - 4));
            }
        }

        List<Integer> nodes = new ArrayList<>();
        for (Node n : this.children) {
            if (n.isLeafNode()) {
                LeafNode ln = (LeafNode) n;
                if (posTags.contains(ln.pos) || labels.contains(ln.label)) {
                    nodes.add(ln.id);
                }
            }
        }
        return nodes;
    }

    @Override
    public List<Node> findNodes(String pattern) {
        String[] parts = pattern.split(" ");

        List<String> labels = new ArrayList<>();
        List<String> features = new ArrayList<>();

        for (String p : parts) {
            if (p.endsWith("_lab")) {
                labels.add(p.substring(0, p.length() - 4));
            } else if (p.endsWith("_fea")) {
                features.add(p.substring(0, p.length() - 4));
            }
        }

        List<Node> nodes = new ArrayList<>();
        for (Node n : this.children) {
            if (n.isInnerNode()) {
                InnerNode in = (InnerNode) n;
                if (labels.contains(in.label) || features.contains(in.feature)) {
                    nodes.add(in);
                }
            }
        }
        return nodes;
    }
}
