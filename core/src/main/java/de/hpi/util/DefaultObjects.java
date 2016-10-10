package de.hpi.util;

import de.hpi.nlp.morphology.Morphy;
import de.hpi.nlp.morphology.ZmorgeMorphology;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.IOException;
import java.io.InputStream;

public class DefaultObjects {

    public static final String taggerModelFile = "de-pos-maxent.bin";
    public static final String sentDetectorModelFile = "de-sent.bin";
    public static final String morphologyLexiconFile = "morphy-export-20110722.xml";
    public static final String smallMorphologyLexiconFile = "morphy-export-20110722.small.xml";

    /**
     * Default singleton objects
     */
    private static Morphy MORPHY = null;
    private static ZmorgeMorphology ZMORGE = null;

    public static InputStream getResourceAsStream(String resource)
        throws IOException {
        InputStream in = DefaultObjects.class.getClassLoader().getResourceAsStream(resource);
        if (in == null) {
            throw new IOException("Couldn't load resource: " + resource);
        } else {
            return in;
        }
    }

    public static Morphy getMorphy(boolean test) throws IOException {
        if (MORPHY == null) {
            String f = (test) ? smallMorphologyLexiconFile : morphologyLexiconFile;
            InputStream in = getResourceAsStream(f);
            if (in == null) {
                throw new IOException("Couldn't load resource: " + f);
            }
            MORPHY = new Morphy(in, test);
        }
        return MORPHY;
    }

    public static ZmorgeMorphology getZmorge() {
        if (ZMORGE == null) {
            ZMORGE = new ZmorgeMorphology();
        }
        return ZMORGE;
    }

    public static POSTagger getDefaultPosTagger() throws IOException {
        return new POSTaggerME(new POSModel(
            getResourceAsStream(taggerModelFile)));
    }

    public static SentenceDetector getDefaultSentenceDetector()
        throws IOException {
        return new SentenceDetectorME(new SentenceModel(
            getResourceAsStream(sentDetectorModelFile)));
    }

}
