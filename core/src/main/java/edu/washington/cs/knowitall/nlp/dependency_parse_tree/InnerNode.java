package edu.washington.cs.knowitall.nlp.dependency_parse_tree;

public class InnerNode extends Node {

    public String feature = "";
    public String label = "";

    public InnerNode(String data) {
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
    public boolean isInnerNode() {
        return true;
    }

    @Override
    public boolean isLeafNode() {
        return false;
    }
}
