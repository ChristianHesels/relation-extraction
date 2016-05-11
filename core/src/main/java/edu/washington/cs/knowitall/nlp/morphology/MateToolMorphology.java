package edu.washington.cs.knowitall.nlp.morphology;


import com.google.common.collect.Iterables;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import is2.data.SentenceData09;
import is2.util.DB;

public class MateToolMorphology {

    is2.mtag.Tagger morph;

    public MateToolMorphology() {
        DB.setDebug(false);

        // Load morphology
        URL in = MateToolMorphology.class.getClassLoader().getResource("morphology-ger-3.6.model");
        if (in != null) {
            morph = new is2.mtag.Tagger(in.getPath());
        }
    }

    public List<String> morphology(List<String> orgTokens) {
        // convert tokens into array with 'root' element
        List<String> tokens = new ArrayList<>();
        Iterables.addAll(tokens, orgTokens);
        SentenceData09 sent = new SentenceData09();
        ArrayList<String> forms = new ArrayList<>();
        forms.add("<root>");
        for (String t : tokens) {
            forms.add(t);
        }
        sent.init(forms.toArray(new String[forms.size()]));

        // get morphological analysis
        sent = morph.apply(sent);

        for (int i = 1; i < sent.pfeats.length; i++) {
            tokens.set(i - 1, sent.pfeats[i]);
        }
        return tokens;
    }
}
