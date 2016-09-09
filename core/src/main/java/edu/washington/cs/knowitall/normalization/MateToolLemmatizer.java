package edu.washington.cs.knowitall.normalization;


import is2.data.SentenceData09;
import is2.lemmatizer.Lemmatizer;
import is2.util.DB;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MateToolLemmatizer {

    private Lemmatizer lemmatizer = null;

    public MateToolLemmatizer() {
        DB.setDebug(false);

        // Load lemmatizer
        lemmatizer = new Lemmatizer(getFilePath(), false);
    }

    public List<String> lemmatize(List<String> tokens) {
        // convert tokens into array with 'root' element
        SentenceData09 sent = new SentenceData09();
        ArrayList<String> forms = new ArrayList<>();
        forms.add("<root>");
        for (String t : tokens) {
            forms.add(t);
        }
        sent.init(forms.toArray(new String[forms.size()]));

        // lemmatize
        try {
            sent = lemmatizer.apply(sent);
        } catch (Exception e) {
            return tokens;
        }

        for (int i = 0; i < sent.plemmas.length; i++) {
            tokens.set(i, sent.plemmas[i]);
        }
        return tokens;
    }

    public String lemmatize(String token) {
        // convert tokens into array with 'root' element
        SentenceData09 sent = new SentenceData09();
        sent.init(new String[] { "<root>", token});

        // lemmatize
        try {
            sent = lemmatizer.apply(sent);
        } catch (Exception e) {
            return token;
        }

        if (sent.plemmas.length == 1) {
            return sent.plemmas[0];
        }
        return token;
    }


    private String getFilePath() {
        String resource = "lemma-ger-3.6.model";
        URL url = MateToolLemmatizer.class.getClassLoader().getResource(resource);

        if (url == null) {
            return "";
        }

        if (url.toString().startsWith("jar:")) {
            try {
                InputStream input = MateToolLemmatizer.class.getClassLoader().getResourceAsStream(resource);
                File file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                file.deleteOnExit();

                return file.getPath();
            } catch (IOException ex) {
                // Could not write tmp file
            }
        }
        return url.getPath();
    }

}
