package de.hpi.nlp.extraction.dependency_parse_tree;

public class Context {

    private ContextType extractionFrom;
    private String contextStr;


    public Context(ContextType extractionFrom) {
        this.extractionFrom = extractionFrom;
        this.contextStr = null;
    }

    public Context(ContextType extractionFrom, String modifierStr) {
        this.extractionFrom = extractionFrom;
        this.contextStr = modifierStr;
    }

    @Override
    public String toString() {
        if (contextStr != null) {
            return extractionFrom.toString() + " - " + contextStr;
        }
        return extractionFrom.toString();
    }


    public ContextType getExtractionFrom() {
        return extractionFrom;
    }

    public void setExtractionFrom(ContextType extractionFrom) {
        this.extractionFrom = extractionFrom;
    }

    public String getContextStr() {
        return contextStr;
    }

    public void setContextStr(String contextStr) {
        this.contextStr = contextStr;
    }
}
