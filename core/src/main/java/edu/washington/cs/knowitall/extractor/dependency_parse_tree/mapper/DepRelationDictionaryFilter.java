package edu.washington.cs.knowitall.extractor.dependency_parse_tree.mapper;

import edu.washington.cs.knowitall.extractor.FilterMapper;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import edu.washington.cs.knowitall.normalization.TreeNormalizedField;
import edu.washington.cs.knowitall.normalization.VerbalRelationNormalizer;
import edu.washington.cs.knowitall.util.DefaultObjects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

public class DepRelationDictionaryFilter extends
                                                FilterMapper<TreeBinaryExtraction> {

    private String relationDictFile = "dep_rel_dict_de.txt.gz";
    private static int defaultFreq = 20;

    private HashSet<String> relations;
    private VerbalRelationNormalizer normalizer;

    public DepRelationDictionaryFilter() {
        this(defaultFreq);
    }

    public DepRelationDictionaryFilter(int minFreq) {
        try {
            this.relations = getRelations(minFreq);
        } catch (IOException e) {
            this.relations = new HashSet<>();
        }
        normalizer = new VerbalRelationNormalizer(true, true, true);
    }

    /**
     * Returns true if the tokens in the given extraction appear in the set of relations.
     */
    public boolean doFilter(TreeBinaryExtraction extr) {
        TreeNormalizedField normField = normalizer.normalizeField(extr.getRel());
        return relations.contains(normField.toString());
    }

    /**
     * Reads the relation dictionary file and returns the relations, which have a frequency above the given min
     * frequency.
     * @param minFreq min frequency
     * @return a set of relations
     * @throws IOException if the relation file could not be read
     */
    private HashSet<String> getRelations(int minFreq) throws IOException {
        HashSet<String> relations = new HashSet<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));

        int lineNum = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            lineNum++;
            String[] fields = line.split("\t");
            if (fields.length != 2) {
                System.err.println("Could not read line " + lineNum + ": '" + line + "'");
                continue;
            }
            int freq = Integer.parseInt(fields[0]);
            if (freq >= minFreq) {
                relations.add(fields[1]);
            }
        }

        return relations;
    }

    /**
     * @return the input stream of the relation dictionary file
     * @throws IOException if the relation dictionary file could not be found/load
     */
    private InputStream getInputStream() throws IOException {
        InputStream in = DefaultObjects.getResourceAsStream(relationDictFile);
        if (in != null) {
            return new GZIPInputStream(in);
        } else {
            throw new IOException("Could not load file " + relationDictFile
                    + " from classpath.");
        }
    }
}
