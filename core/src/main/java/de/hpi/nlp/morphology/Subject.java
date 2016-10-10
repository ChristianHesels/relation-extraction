package de.hpi.nlp.morphology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Subject {

    private List<String> kasus;
    private List<String> numerus;
    private List<String> genus;
    private List<String> derivation;
    private List<String> derivedOf;
    private String lemma;

    public Subject(String s) {
        this.kasus = extract(s, "kas");
//        this.numerus = extract(s, "num");
//        this.genus = extract(s, "gen");
//        this.derivation = extract(s, "der");
//        this.derivedOf = extract(s, "abl");
//        this.lemma = extractLemma(s);
    }

    /**
     * Extracts the value of the attribute in the given xml line.
     * @param xmlLine   xml line
     * @param attribute attribute
     * @return value of attribute
     */
    protected List<String> extract(String xmlLine, String attribute)  {
        List<String> attributes = new ArrayList<>();

        String[] fields = xmlLine.split(attribute + "=\"");
        if (fields.length != 2) {
            return attributes;
        }
        String[] parts = fields[1].split("\"");
        if (parts.length < 1) {
            return attributes;
        }
        String value = parts[0];
        if (value.contains(",")) {
            Collections.addAll(attributes, value.split(","));
        } else {
            attributes.add(value);
        }
        return attributes;
    }

    /**
     * Extracts the lemma of the given xml line.
     * @param xmlLine xml line
     * @return lemma
     */
    protected String extractLemma(String xmlLine) {
        String[] parts = xmlLine.replace("</lemma>", "").split(">");
        if (parts.length != 2) {
            return "";
        } else {
            return parts[1];
        }
    }

    public List<String> getKasus() {
        return kasus;
    }

    public List<String> getNumerus() {
        return numerus;
    }

    public List<String> getGenus() {
        return genus;
    }

    public List<String> getDerivation() {
        return derivation;
    }

    public List<String> getDerivedOf() {
        return derivedOf;
    }

    public String getLemma() {
        return lemma;
    }
}
