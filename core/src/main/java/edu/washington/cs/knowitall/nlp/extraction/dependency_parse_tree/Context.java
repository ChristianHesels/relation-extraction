package edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree;

public class Context {

    private ContextType type;
    private String conjunction;


    public Context(ContextType type) {
        this.type = type;
        this.conjunction = null;
    }

    public Context(ContextType type, String conjunction) {
        this.type = type;
        this.conjunction = conjunction;
    }

    @Override
    public String toString() {
        if (conjunction != null) {
            return type.toString() + " - " + conjunction;
        }
        return type.toString();
    }


}
