package edu.washington.cs.knowitall.normalization;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import is2.data.SentenceData09;
import is2.lemmatizer.Lemmatizer;
import is2.util.DB;

public class MateToolLemmatizer {

    Lemmatizer lemmatizer;

    public MateToolLemmatizer() {
        DB.setDebug(false);

        // Load lemmatizer
        URL in = VerbalRelationNormalizer.class.getClassLoader().getResource("lemma-ger-3.6.model");
        if (in != null) {
            lemmatizer = new Lemmatizer(in.getPath(), false);
        }
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
        sent = lemmatizer.apply(sent);

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
        sent = lemmatizer.apply(sent);

        if (sent.plemmas.length == 1) {
            return sent.plemmas[0];
        }
        return token;
    }

}
