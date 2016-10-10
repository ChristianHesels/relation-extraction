package de.hpi.nlp.morphology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class, which checks if a substantive is in nominative using the "Deutsches Morpholgie-Lexikon".
 */
public class Morphy implements Morphology {

    private InputStream inputStream;
    private boolean debug = false;
    private Map<String, List<Subject>> lexicon = new HashMap<>();

    public Morphy(InputStream inputStream) throws IOException {
        this(inputStream, false);
    }

    public Morphy(InputStream inputStream, boolean debug) throws IOException {
        this.inputStream = inputStream;
        this.debug = debug;
        initialize();
    }

    /**
     * Read the "Deutsches Morphologie-Lexikon".
     * We are only interested in substantives, all other type of words are ignored.
     * @throws IOException if the lexicon could not be read
     */
    private void initialize() throws IOException {
        if (debug) System.out.println("Reading \"Deutsches Morphologie-Lexikon\" ... ");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

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

    /**
     * Checks if the given word is in nominative
     * @param word the word
     * @return true, if the word is in nominative, false otherwise
     */
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

