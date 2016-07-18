package edu.washington.cs.knowitall.normalization;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.nlp.chunking.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedRelationExtraction;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VerbalRelationNormalizerTest {

    private static VerbalRelationNormalizer normalizer;

    @Before
    public void setUp() throws Exception {
        normalizer = new VerbalRelationNormalizer(true, false, true);
    }

    private static void assertNorm(String expectedStr, String tokensStr, String posTagsStr)
        throws Exception {
        List<String> tokens = Arrays.asList(tokensStr.split(" "));
        List<String> posTags = Arrays.asList(posTagsStr.split(" "));
        List<String> npChunkTags = new ArrayList<String>(posTags.size());
        for (int i = 0; i < posTags.size(); i++) {
            npChunkTags.add("O");
        }

        ChunkedSentence sent = new ChunkedSentence(tokens, posTags, npChunkTags);
        ChunkedRelationExtraction
            extr =
            new ChunkedRelationExtraction(sent, new Range(0, posTags.size()));

        NormalizedField normField = normalizer.normalizeField(extr);
        String resultStr = normField.toString();
        assertEquals(expectedStr, resultStr);
    }

    @Test
    public void testNormalize() throws Exception {
        assertNorm("fahren", "fuhr", "VVFIN");
        assertNorm("sein in", "war in", "VAFIN APPR");
        assertNorm("einarbeiten in", "sind eingearbeitet in", "VAFIN VVPP APPR");
        assertNorm("nehmen durchschnittlich", "nahm durchschnittlich", "VVFIN ADJD");
        assertNorm("sein NOUN für", "ist die perfekte Zeit für", "VAFIN ART ADJA NN APPR");
        assertNorm("sein NOUN von", "ist das Ergebnis von", "VAFIN ART NN APPR");
        assertNorm("können kaufen an", "kann gekauft werden am", "VMFIN VVPP VAINF APPRART");
        assertNorm("können nicht löschen von", "kann nicht gelöscht werden von",
                   "VMFIN PTKNEG VVPP VAFIN APPR");
        assertNorm("können nicht sein", "kann nicht sein", "VMFIN PTKNEG VAINF");
        assertNorm("benötigen NOUN an", "benötigt auch ein hohes Level an",
                   "ADJD ADV ART ADJA NN APPR");
        assertNorm("nehmen NOUN mit zu", "nahm meine Kinder mit zu", "VVFIN PPOSAT NN APPR APPR");
        assertNorm("verwüsten von", "wurde verwüstet von", "VAFIN VVFIN APPR");
        assertNorm("haben nichts damit zu tun", "hat nichts damit zu tun",
                   "VAFIN PIS PROAV PTKZU VVINF");
        assertNorm("sein gut darin", "sind gut darin", "VAFIN ADJD PAV");
        assertNorm("sein gut darin", "sind viel besser darin", "VAFIN ADV ADJD PROAV");
        assertNorm("sehen gut aus", "sieht ziemlich gut aus", "VAFIN ADV ADJD APPR");
        assertNorm("sein groß als", "ist größer als", "VAFIN ADJD KOKOM");
        assertNorm("sein NOUN für", "ist eine exzellente Quelle für", "VAFIN ART ADJA NN APPR");
    }

}
