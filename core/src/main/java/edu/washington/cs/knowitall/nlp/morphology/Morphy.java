package edu.washington.cs.knowitall.nlp.morphology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class, which holds a morphological lexicon.
 */
public class Morphy {

    private String fileName;
    private boolean debug = false;
    private Map<String, List<Subject>> lexicon = new HashMap<>();

    public Morphy(String fileName) throws IOException {
        this(fileName, false);
    }

    public Morphy(String fileName, boolean debug) throws IOException {
        this.fileName = fileName;
        this.debug = debug;
        initialize();
    }

    private void initialize() throws IOException {
        if (debug) System.out.println("Reading \"Deutsches Morphologie-Lexikon\" ... ");
        BufferedReader br = new BufferedReader(new FileReader(this.fileName));

        // skip header
        String line = br.readLine();
        while (line != null && !line.equals("<item>")) {
            line = br.readLine();
        }

        // read entries
        while (line != null) {
            String form = "";
            final List<Subject> subjects = new ArrayList<>();

            line = br.readLine();
            while (line != null && !line.equals("<item>")) {
                if (line.startsWith("<form>")) {
                    form = line.replace("<form>", "").replace("</form>", "");
                } else if (line.startsWith("<lemma")) {
                    if (line.substring(12, 15).equals("SUB")) {
                        subjects.add(new Subject(line));
                    }
                }
                line = br.readLine();
            }

            this.lexicon.put(form, subjects);
        }
        if (debug) System.out.println("Done.");
    }

    public boolean isNominative(String word) {
        if (this.lexicon.containsKey(word)) {
            List<Subject> subjects = this.lexicon.get(word);
            for (Subject sub : subjects) {
                if (sub.getKasus().contains("NOM")) {
                    return true;
                }
            }
            return false;
        } else {
            throw new NoSuchElementException("Key not found: " + word);
        }
    }
}

