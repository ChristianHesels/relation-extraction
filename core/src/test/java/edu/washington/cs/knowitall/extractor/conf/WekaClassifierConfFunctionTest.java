package edu.washington.cs.knowitall.extractor.conf;

import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.model.ListEventStream;

import org.junit.Test;

import edu.washington.cs.knowitall.extractor.conf.featureset.BooleanFeatureSet;
import edu.washington.cs.knowitall.extractor.conf.opennlp.OpenNlpConfFunction;

import static org.junit.Assert.assertTrue;

public class WekaClassifierConfFunctionTest {

    @Test
    public void testGetConf() throws Exception {
        BooleanFeatureSet<String> features = StringFeatures.featureSet;
        GIS.PRINT_MESSAGES = false;
        GISModel model = GIS.trainModel(
            new ListEventStream(StringFeatures.dataSet.getInstances()), 100, 0);
        OpenNlpConfFunction<String> conf = new OpenNlpConfFunction<String>(model, features);
        double janeConf = conf.getConf("jane");
        double ofConf = conf.getConf("of");
        assertTrue(ofConf < janeConf);
        assertTrue(ofConf < 1.0);
        assertTrue(0.0 < janeConf);
    }
}
