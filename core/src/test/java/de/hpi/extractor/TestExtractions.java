package de.hpi.extractor;

import de.hpi.nlp.chunking.ChunkedSentence;
import de.hpi.nlp.extraction.chunking.ChunkedArgumentExtraction;
import de.hpi.nlp.extraction.chunking.ChunkedBinaryExtraction;
import de.hpi.nlp.extraction.chunking.ChunkedRelationExtraction;
import edu.washington.cs.knowitall.commonlib.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Some extractions that are used by other test cases.
 *
 * @author afader
 */
public abstract class TestExtractions {

    public static List<ChunkedSentence> sentences;
    public static List<ChunkedBinaryExtraction> extractions;


    static {

        sentences = new ArrayList<ChunkedSentence>();
        extractions = new ArrayList<ChunkedBinaryExtraction>();

        try {

            addSentExtr(
                "Mike ist der Bürgermeister von Seattle und Max ist der Finanzvorstand der Stadt .",
                "NE VAFIN ART NN APPR NE KON NE VAFIN ART NN APPR NN $.",
                "B-NP B-VP B-NP I-NP B-PP I-PP O B-NP B-VP B-NP I-NP B-PP I-PP O",
                0, 1, // Mike
                1, 4, // ist der Bügermeister von
                5, 1,  // Seattle
                0.5,
                "doc1"
            );

            addSentExtr(
                "Mike ist der Bürgermeister von Seattle und Max ist der Finanzvorstand der Stadt .",
                "NE VAFIN ART NN APPR NE KON NE VAFIN ART NN APPR NN $.",
                "B-NP B-VP B-NP I-NP B-PP I-PP O B-NP B-VP B-NP I-NP B-PP I-PP O",
                0, 1, // Mike
                1, 1, // ist
                2, 2,  // der Bügermeister
                0.5,
                "doc1"
            );

            addSentExtr(
                "Mike ist der Bürgermeister von Seattle und Max ist der Finanzvorstand der Stadt .",
                "NE VAFIN ART NN APPR NE KON NE VAFIN ART NN APPR NN $.",
                "B-NP B-VP B-NP I-NP B-PP I-PP O B-NP B-VP B-NP I-NP B-PP I-PP O",
                7, 1, // Max
                8, 1, // ist
                9, 2,  // der Finanzvorstand
                0.5,
                "doc1"
            );

            addSentExtr(
                "Er brachte uns auf die Idee der Unabhängigkeit .",
                "PPER VVFIN PPER APPR ART NN ART NN $.",
                "B-NP B-VP B-NP B-PP I-PP I-PP B-NP I-NP O",
                0, 1, // Er
                1, 6, // brachte uns auf die Idee
                7, 1, // der Unabhägigikeit
                0.9,
                "doc1"
            );

            addSentExtr(
                "Die XDH Gen-Mutation ist die zugrundeliegende Ursache der klassischen xanthinuria .",
                "ART NE NN VAFIN ART ADJA NN ART ADJA NN $.",
                "B-NP I-NP I-NP B-VP B-NP I-NP I-NP B-NP I-NP I-NP O",
                0, 3, // Die XDH Gen-Mutation
                3, 4, // ist die zugrundeliegende Ursache
                7, 3, // der klassischen xanthinuria
                0.3,
                "doc2"
            );

            addSentExtr(
                "Der Bau der Kirche begann im Jahre 1900 .",
                "ART NN ART NN VVFIN APPRART NN CARD $.",
                "B-NP I-NP B-NP I-NP B-VP B-PP I-PP I-PP O",
                0, 4, // Der Bau der Kirche
                4, 1, // begann
                5, 3, // im Jahre 1900,
                0.1,
                "doc3"
            );

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    private static void addSentExtr(String a, String b, String c,
                                    int xs, int xl, int rs, int rl, int ys, int yl, double conf,
                                    String docId) throws Exception {

        ChunkedSentence sent = toSent(a, b, c);
        sentences.add(sent);
        ChunkedBinaryExtraction extr = toExtr(sent, xs, xl, rs, rl, ys, yl);
        extr.setProperty("docId", docId);
        extr.setProperty("conf", Double.toString(conf));
        extractions.add(extr);

    }

    private static List<String> split(String s) {
        return Arrays.asList(s.split(" "));
    }

    private static ChunkedSentence toSent(String toks, String pos,
                                          String chunks) throws Exception {
        return new ChunkedSentence(split(toks), split(pos), split(chunks));
    }

    private static ChunkedBinaryExtraction toExtr(ChunkedSentence sent,
                                                  int xs, int xl, int rs, int rl, int ys, int yl) {
        ChunkedRelationExtraction rel = new ChunkedRelationExtraction(sent, new Range(rs, rl));
        ChunkedArgumentExtraction x = new ChunkedArgumentExtraction(sent,
                                                                    new Range(xs, xl), rel);
        ChunkedArgumentExtraction y = new ChunkedArgumentExtraction(sent,
                                                                    new Range(ys, yl), rel);
        return new ChunkedBinaryExtraction(rel, x, y);
    }


}
