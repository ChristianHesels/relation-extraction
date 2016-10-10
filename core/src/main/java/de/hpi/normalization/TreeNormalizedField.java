package de.hpi.normalization;


public class TreeNormalizedField {

    private String field;

    public TreeNormalizedField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return field;
    }
}
