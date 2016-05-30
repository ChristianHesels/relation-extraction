package edu.washington.cs.knowitall.nlp.morphology;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class, which checks if a substantive is in nominative using zmorge.
 */
public class ZmorgeMorphology implements Morphology {

    private static final String ZMORGE_FILE = "zmorge-20150315-smor_newlemma.ca";

    @Override
    public boolean isNominative(String word) {
        try {
            List<String> output = runZmorge(word);
            // check if word can be nominative
            for (String line : output) {
                if (line.contains("<Nom>") && !line.contains("<Old>")) {
                    return true;
                }
            }
            return false;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Run zmorge.
     * @param word the word
     * @return the output of zmorge
     * @throws IOException if the library file could not be read
     * @throws InterruptedException if the process gets interrupted
     */
    private List<String> runZmorge(String word) throws IOException, InterruptedException {
        String command = "echo \"" + word + "\" | fst-infl2 " + getZmorgePath();
        Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        p.waitFor();

        List<String> output = new ArrayList<>();
        String line;
        // skip the first line
        stdInput.readLine();
        while ((line = stdInput.readLine()) != null) {
            output.add(line);
        }

        stdInput.close();
        p.destroy();

        return output;
    }

    /**
     * Get the path of the zmorge file.
     * @return the path
     * @throws IOException if the file could not be found/read
     */
    private String getZmorgePath() throws IOException {
        URL url = getClass().getClassLoader().getResource(ZMORGE_FILE);
        if (url != null)
            return url.getPath();
        return "";
    }
}
