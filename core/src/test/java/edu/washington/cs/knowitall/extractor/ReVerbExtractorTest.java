package edu.washington.cs.knowitall.extractor;

import com.google.common.collect.Iterables;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

import static org.junit.Assert.assertEquals;

public class ReVerbExtractorTest {

    private static ReVerbIExtractor reverb;
    private static ReVerbIExtractor regReverb;
    private static ReVerbIExtractor relaxedReverb;
    private static HashSet<String> expected, got;


    @Before
    public void setUp() throws Exception {
        if (regReverb == null) {
            regReverb = new ReVerbIExtractor(0, true); // TODO
        }
        if (relaxedReverb == null) {
            relaxedReverb = new ReVerbIExtractor(0, false);
            relaxedReverb.setAllowUnary(true);
            relaxedReverb.setMergeOverlapRels(false);
        }
        expected = new HashSet<String>();
    }

    private static List<ChunkedBinaryExtraction> extract(ChunkedSentence sent) throws Exception {
        ArrayList<ChunkedBinaryExtraction> results = new ArrayList<ChunkedBinaryExtraction>();
        Iterables.addAll(results, reverb.extract(sent));
        return results;
    }

    private static HashSet<String> extractRels(String ts, String ps, String cs) throws Exception {
        List<ChunkedBinaryExtraction> extrs = extract(asSentence(ts, ps, cs));
        HashSet<String> results = new HashSet<String>();
        for (ChunkedBinaryExtraction extr : extrs) {
            results.add(extr.getRelation().toString());
        }
        return results;
    }

    private static HashSet<String> extractTriples(String ts, String ps, String cs)
        throws Exception {
        List<ChunkedBinaryExtraction> extrs = extract(asSentence(ts, ps, cs));
        HashSet<String> results = new HashSet<String>();
        for (ChunkedBinaryExtraction extr : extrs) {
            results.add(
                "(" + extr.getArgument1() + ", " + extr.getRelation() + ", " + extr.getArgument2()
                + ")");
        }
        return results;
    }

    public static ChunkedSentence asSentence(String tokensStr, String posTagsStr,
                                             String npChunkTagsStr) throws Exception {
        String[] tokens = tokensStr.split(" ");
        String[] posTags = posTagsStr.split(" ");
        String[] npChunkTags = npChunkTagsStr.split(" ");
        return new ChunkedSentence(tokens, posTags, npChunkTags);
    }

    @Test
    public void testExtract1() throws Exception {
        reverb = regReverb;
        got = extractRels(
            "Amazon ist ein sogenanntes Social-Commerce-Versandhaus .",
            "NE VAFIN ART ADJA NN $.",
            "B-NP B-VP B-NP I-NP I-NP O"
        );
        expected.add("ist ein");
        assertEquals(expected, got);
    }

    @Test
    public void testNoFiltersExtract1() throws Exception {
        reverb = relaxedReverb;
        got = extractRels(
            "Amazon ist ein sogenanntes Social-Commerce-Versandhaus .",
            "NE VAFIN ART ADJA NN $.",
            "B-NP B-VP B-NP I-NP I-NP O"
        );
        expected.add("ist ein");
        assertEquals(expected, got);
    }

    @Test
    public void testExtract2() throws Exception {
        reverb = regReverb;
        got = extractRels(
            "Das St. Johannes-Hospital ist heute ein modernes , leistungsfähiges Krankenhaus .",
            "ART ADJA NE VAFIN ADV ART ADJA $, ADJA NN $.",
            "B-NP I-NP I-NP B-VP O B-NP I-NP I-NP I-NP I-NP O"
        );
        expected.add("ist heute ein");
        assertEquals(expected, got);
    }

    @Test
    /**
     * @throws Exception
     */
    public void testNoFiltersExtract2() throws Exception {
        reverb = relaxedReverb;
        got = extractRels(
            "Das St. Johannes-Hospital ist heute ein modernes , leistungsfähiges Krankenhaus .",
            "ART ADJA NE VAFIN ADV ART ADJA $, ADJA NN $.",
            "B-NP I-NP I-NP B-VP O B-NP I-NP I-NP I-NP I-NP O"
        );
        expected.add("ist heute ein");
        assertEquals(expected, got);
    }

    @Test
    public void testExtract3() throws Exception {
        reverb = regReverb;
        got = extractRels(
            "B1 Systems GmbH ist stolzer Sponsor des openSUSE Projekts .",
            "ADJA NN NN VAFIN ADJA NN ART ADJA NN $.",
            "B-NP I-NP I-NP B-VP B-NP I-NP B-NP I-NP I-NP O"
        );
        expected.add("ist stolzer Sponsor des");
        assertEquals(expected, got);
    }

    @Test
    /**
     * Adds uninformative relations.
     * @throws Exception
     */
    public void testNoFiltersExtract3() throws Exception {
        reverb = relaxedReverb;
        got = extractRels(
            "B1 Systems GmbH ist stolzer Sponsor des openSUSE Projekts .",
            "ADJA NN NN VAFIN ADJA N ART ADJA NN $.",
            "B-NP I-NP I-NP B-VP B-NP I-NP B-NP I-NP I-NP O"
        );
        expected.add("ist");
        assertEquals(expected, got);
    }

//    @Test
//    public void testExtract3() throws Exception {
//
//    	reverb = regReverb;
//        got = extractRels(
//                "Unsere Kooperation als Exklusivpartner des Schleswig-Holsteinischen Fußballverbandes (SHFV) setzt sich ab dem 01.07.2016 für die nächsten drei Jahre fort .",
//                "",
//                ""
//        );
//        expected.add("");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * No filtering retains overlapping relations, relations with TO, and allows unary relations.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract3() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "Unsere Kooperation als Exklusivpartner des Schleswig-Holsteinischen Fußballverbandes (SHFV) setzt sich ab dem 01.07.2016 für die nächsten drei Jahre fort .",
//            "",
//            ""
//        );
//
//        expected.add("simply open");
//        expected.add("listen as");
//        expected.add("reads");//overlapping relation retained.
//        expected.add("reads the newspaper to");
//        expected.add("do");
//        expected.add("want to");//relation with TO retained.
//        expected.add("is published");//unary relation added.
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract4() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "Die Knoll-Gruppe mit Hauptsitz in Bayreuth ruft im Jahr 2015 die Manfred Knoll Familienstiftung ins Leben .",
//                "",
//                ""
//        );
//        expected.add("supports up to");
//        expected.add("has plenty of");
//        expected.add("has");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * No filtering adds relations that contain a VBN.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract4() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "Die Knoll-Gruppe mit Hauptsitz in Bayreuth ruft im Jahr 2015 die Manfred Knoll Familienstiftung ins Leben .",
//            "",
//            ""
//        );
//        expected.add("supports up to");
//        expected.add("packed into");//adds relation containing VBN.
//        expected.add("has plenty of");
//        expected.add("has");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract5() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "Nachdem HUGO BOSS bereits über mehrere Jahre karitative Projekte zugunsten von Kindern unterstützt hat, baut das Unternehmen ab 2007 im Rahmen seiner Partnerschaft mit der Kinderschutzorganisation UNICEF Schulen in den ärmsten Ländern Afrikas auf.",
//                "",
//                ""
//        );
//        expected.add("are in");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * No filtering adds relations that contain TO, and unary relations.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract5() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "Nachdem HUGO BOSS bereits über mehrere Jahre karitative Projekte zugunsten von Kindern unterstützt hat, baut das Unternehmen ab 2007 im Rahmen seiner Partnerschaft mit der Kinderschutzorganisation UNICEF Schulen in den ärmsten Ländern Afrikas auf.",
//            "",
//            ""
//        );
//        expected.add("is");//unary relation.
//        expected.add("compared to");//retains relation with TO
//        expected.add("are in");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract6() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "Der Trainingsanzug „Franz Beckenbauer“ war das erste Bekleidungsprodukt von adidas.",
//                "",
//                ""
//        );
//        expected.add("rose 25 cents to");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * No filtering adds overlapping relation.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract6() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "Der Trainingsanzug „Franz Beckenbauer“ war das erste Bekleidungsprodukt von adidas.",
//            "",
//            ""
//        );
//        expected.add("rose 25 cents to");
//        expected.add("rose");//overlapping relation
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract7() throws Exception {
//    	reverb = regReverb;
//        got = extractTriples(
//                "Derek Clayton bricht, von ASICS gesponsert, den Marathon-Weltrekord von 2 Stunden und 10 Minuten .",
//                "",
//                ""
//        );
//        expected.add("(the temple, was built between, the fifth and sixth centuries)");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * No filtering allows relations with lexical stopword "that".
//     * @throws Exception
//     */
//    public void testNoFiltersExtract7() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractTriples(
//            "Derek Clayton bricht, von ASICS gesponsert, den Marathon-Weltrekord von 2 Stunden und 10 Minuten .",
//            "",
//            ""
//        );
//        expected.add("(Jiwa, found that, the temple)");
//        expected.add("(the temple, was built between, the fifth and sixth centuries)");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract8() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "Samy Deluxe, 1977 in Hamburg geboren, gründete bereits 1997 seine erste HipHop-Band DYNAMITE DELUXE .",
//                "",
//        ""
//        );
//        expected.add("was n't able to realize");
//        expected.add("dropped");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * No filtering adds over-specified relations, relations with lexical stopword "because", relations with VBD and retains smaller overlapping relations.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract8() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "Samy Deluxe, 1977 in Hamburg geboren, gründete bereits 1997 seine erste HipHop-Band DYNAMITE DELUXE .",
//            "",
//            ""
//        );
//        expected.add("liquidate because");//relation with lexical stop word "because" is allowed.
//        expected.add("realize");
//        expected.add("had expected"); //relation with VBN is allowed.
//        expected.add("was n't able to realize");
//        expected.add("was n't");//overlapping smaller relation not merged.
//        expected.add("dropped");
//        expected.add("dropped its plan to liquidate because"); // over-specified relation with no support.
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract9() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "Die Spielemacher Gauselmann gründeten 1957 die Firma Gebrüder Gauselmann GmbH .",
//                "",
//                ""
//        );
//        expected.add("can serve up");
//        assertEquals(expected, got);
//    }
//    @Test
//    /**
//     * No filtering adds unary relations, and over-specified relations that have little or no support in a large corpus.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract9() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "Die Spielemacher Gauselmann gründeten 1957 die Firma Gebrüder Gauselmann GmbH .",
//            "",
//            ""
//        );
//        expected.add("can serve up files via"); // overspecified relation with no support added.
//        expected.add("Remote"); //unary relation added.
//        expected.add("can serve up");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract10() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "Siemens, mit Firmensitz in Berlin und München, zählt zu den weltweit größten und traditionsreichsten Firmen der Elektrotechnik und Elektronik mit führenden Marktpositionen auf all seinen Arbeitsgebieten .",
//                "",
//                ""
//        );
//        expected.add("were made by");
//        expected.add("were a name given to");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * No filtering does not modify the extraction of relations from relative clauses.
//     * No filtering however, adds relations that begin with VBN and allows relations that contain TO.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract10() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "Siemens, mit Firmensitz in Berlin und München, zählt zu den weltweit größten und traditionsreichsten Firmen der Elektrotechnik und Elektronik mit führenden Marktpositionen auf all seinen Arbeitsgebieten .",
//            "",
//            ""
//        );
//        expected.add("were"); //relations that begin with VBN are allowed.
//        expected.add("given to"); //relations that contain TO are allowed.
//        expected.add("were made by");
//        expected.add("were a name given to");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     *
//     * @throws Exception
//     */
//    public void testExtract11() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "CHEFS CULINAR ist mit acht Niederlassungen und 20 Stützpunkten in Deutschland vertreten .",
//                "",
//                ""
//        );
//        expected.add("is the underlying cause of");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testNoFiltersExtract11() throws Exception {
//    	reverb = relaxedReverb;
//        got = extractRels(
//            "CHEFS CULINAR ist mit acht Niederlassungen und 20 Stützpunkten in Deutschland vertreten .",
//            "",
//            ""
//        );
//
//        expected.add("is");//overlapping relations are not merged.
//        expected.add("underlying");//relation beginning with a VBG is  retained.
//        expected.add("is the underlying cause of");//same as no filter case.
//        assertEquals(expected, got);
//    }
//
//    @Test
//    public void testExtract12() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//            "Deutsche See ist offizieller Lieferant für Fisch der Olympiamannschaft der Bundesrepublik Deutschland und gehört zur Nordsee GmbH .",
//                "",
//                ""
//        );
//        expected.add("is the author of");
//        expected.add("is the capital of");
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * The extraction of relations in co-ordinate clauses is unchanged.
//     * However, noFilters adds an overlapping relation which are not merged.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract12() throws Exception {
//    	reverb = relaxedReverb;
//    	got = extractRels(
//            "Deutsche See ist offizieller Lieferant für Fisch der Olympiamannschaft der Bundesrepublik Deutschland und gehört zur Nordsee GmbH .",
//            "",
//            ""
//        );
//    	expected.add("is");
//        expected.add("is the author of");
//        expected.add("is the capital of");
//        assertEquals(expected, got);
//    }
//
//
//    @Test
//    /**
//     * Relations whose previous tag is an existential (EX) are not allowed.
//     * @throws Exception
//     */
//    public void testExtract13() throws Exception {
//    	reverb = regReverb;
//        got = extractRels(
//                "Es existieren von Arten Essen .",
//                "",
//                ""
//        );
//
//        assertEquals(expected, got);
//    }
//
//    @Test
//    /**
//     * With noFilters, relations whose previous tag is an existential (EX) are also allowed.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract13() throws Exception {
//    	reverb = relaxedReverb;
//    	got = extractRels(
//            "Es existieren von Arten Essen .",
//            "",
//            ""
//        );
//    	expected.add("are");
//    	expected.add("are five types of");
//    	assertEquals(expected, got);
//
//    }
//
//    @Test
//    public void testExtract14() throws Exception {
//    	reverb = regReverb;
//        got = extractTriples(
//            "Fluorpolymerharz , das von Plunkett entdeckt wurde , ist eine Art von Harz .",
//            "",
//            ""
//        );
//        String extr = "(Fluoropolymer resin, was discovered by, Plunkett)";
//        assertTrue(got.contains(extr));
//        extr = "(Fluoropolymer resin, is, a type of resin)";
//        assertFalse(got.contains(extr));
//    }
//
//    @Test
//    /**
//     * No filtering does not alter the dropping of "wh" determiners.
//     * However, no filtering adds overlapping relations.
//     * @throws Exception
//     */
//    public void testNoFiltersExtract14() throws Exception {
//        reverb = relaxedReverb;
//    	got = extractTriples(
//                "Fluorpolymerharz , das von Plunkett entdeckt wurde , ist eine Art von Harz .",
//                "",
//                ""
//        );
//        String extr = "(Fluoropolymer resin, was discovered by, Plunkett)";
//        assertTrue(got.contains(extr));
//        extr = "(Fluoropolymer resin, is, a type of resin)";
//        assertTrue(got.contains(extr));
//    }
//
//
//    @Test
//    public void testExtractOneChar() throws Exception {
//        // This is a real example from Wikipedia text.
//    	reverb = regReverb;
//        got = extractTriples(
//            ". ^ a b c d e f g h i j k l m Clinton , Bill .",
//            "",
//            ""
//        );
//        assertEquals(0, got.size());
//    }
//
//
//    @Test
//    public void testRelPronounError() throws Exception {
//        /*
//         * Relations should not contain pronouns.
//         */
//    	reverb = regReverb;
//        got = extractTriples(
//            "Ich holte das Paket bei Max ab .",
//            "",
//            ""
//        );
//        assertFalse(got.contains("(Ixh, holte, das Paket)"));
//    }
//
//    @Test
//    /*
//     * With no filtering, relations can contain pronouns.
//     */
//    public void testNoFilterRelPronounError() throws Exception {
//
//    	reverb = relaxedReverb;
//        got = extractTriples(
//            "Ich holte das Paket bei Max ab .",
//            "",
//            ""
//        );
//
//        assertTrue(got.contains("(Ich, holte, das Paket)"));
//    }
//
//    @Test
//    /**
//     *
//     * @throws Exception
//     */
//    public void testReflexivePronounArg1() throws Exception {
//    	reverb = regReverb;
//        got = extractTriples(
//            "Edison selbst erfand den Phonograph .",
//            "",
//            ""
//        );
//        assertFalse(got.contains("(selbst, erfand, den Phonograph)"));
//        assertTrue(got.contains("(Edison, erfand, den Phonograph)"));
//    }
//
//    @Test
//    /**
//     * Relations should include the noun and not the reflexive pronoun.
//     * No filtering should not affect this behavior.
//     * Question: Should the said text be quoted for this test?
//     * @throws Exception
//     */
//    public void testNoFilterReflexivePronounArg1() throws Exception {
//        reverb = relaxedReverb;
//    	got = extractTriples(
//            "Edison selbst erfand den Phonograph , sagte er selbst .",
//            "",
//            ""
//        );
//        assertFalse(got.contains("(selbst, erfand, den Phonograph)"));
//        assertTrue(got.contains("(Edison, erfand, den Phonograph)"));
//    }
//
//    @Test
//    public void testUnaryCases() throws Exception{
//        reverb = relaxedReverb;
//        got = extractTriples("Menschen auf der Erde werden langsamer",
//                "",
//                "");
//        assertTrue(got.contains("(Erde, werden langsamer, )"));
//
//        got = extractTriples("Es regnet",
//                "",
//                "");
//        assertTrue(got.contains("(Es, regnet, )"));
//
//    }

}
