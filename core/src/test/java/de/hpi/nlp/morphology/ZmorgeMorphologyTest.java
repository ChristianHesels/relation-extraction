package de.hpi.nlp.morphology;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ZmorgeMorphologyTest {

    @Test
    public void testIsNominative() {
        ZmorgeMorphology zmorge = new ZmorgeMorphology();

        // Nominative
        assertTrue(zmorge.isNominative("Vermittlungsgespräche"));

        // No nominative
        assertFalse(zmorge.isNominative("Hauses"));
    }

}
