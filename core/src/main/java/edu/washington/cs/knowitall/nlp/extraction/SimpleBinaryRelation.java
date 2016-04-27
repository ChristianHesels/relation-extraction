package edu.washington.cs.knowitall.nlp.extraction;

public class SimpleBinaryRelation {

    private String relation;
    private String argument1;
    private String argument2;

    public SimpleBinaryRelation() {}

    public SimpleBinaryRelation(String relation, String argument1, String argument2) {
        this.relation = relation;
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getArgument1() {
        return argument1;
    }

    public void setArgument1(String argument1) {
        this.argument1 = argument1;
    }

    public String getArgument2() {
        return argument2;
    }

    public void setArgument2(String argument2) {
        this.argument2 = argument2;
    }
}
