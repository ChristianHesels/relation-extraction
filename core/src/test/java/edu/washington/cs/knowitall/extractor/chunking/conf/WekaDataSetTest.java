package edu.washington.cs.knowitall.extractor.chunking.conf;

import opennlp.model.Event;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WekaDataSetTest {

    @Test
    public void testGetWekaInstances() {
        List<Event> events = StringFeatures.dataSet.getInstances();
        assertEquals(4, events.size());
    }

}
