package de.hpi.nlp.extraction;

public class SimpleBinaryRelation {

    private String relation;
    private String argument1;
    private String argument2;
    private String sentence;
    private String parsedSentence;
    private String context;

    public SimpleBinaryRelation() {}

    public SimpleBinaryRelation(String relation, String argument1, String argument2, String sentence) {
        this.relation = relation;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.sentence = sentence;
        this.parsedSentence = "";
        this.context = "";
    }

    public SimpleBinaryRelation(String relation, String argument1, String argument2, String sentence, String parsedSentence, String context) {
        this.relation = relation;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.sentence = sentence;
        this.parsedSentence = parsedSentence;
        this.context = context;
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

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getParsedSentence() {
        return parsedSentence;
    }

    public void setParsedSentence(String parsedSentence) {
        this.parsedSentence = parsedSentence;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
