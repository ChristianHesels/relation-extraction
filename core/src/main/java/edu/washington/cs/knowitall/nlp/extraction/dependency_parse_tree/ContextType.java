package edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree;

public enum ContextType {

    NONE("none"),
    MAIN_CLAUSE("main clause"),
    THAT_CLAUSE("that clause"),
    SUBORDINATE_CLAUSE("subordinate clause");

    private final String str;
    ContextType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

}
