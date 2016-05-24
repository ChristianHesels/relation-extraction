package edu.washington.cs.knowitall.nlp.chunking;

import opennlp.tools.postag.POSTagger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.washington.cs.knowitall.commonlib.Range;
import edu.washington.cs.knowitall.util.DefaultObjects;

/**
 * A class that combines OpenNLP POS tagger and Tree Tagger tokenizer and chunker objects into a
 * single object that converts String sentences to {@link ChunkedSentence} objects.
 */
public class TreeTaggerSentenceChunker implements SentenceChunker {

    private static final String
        TREETAGGER_HOME = System.getenv("TREETAGGER_HOME");
    private static final String CHUNK_COMMAND = TREETAGGER_HOME + "cmd/tagger-chunker-german";

    private POSTagger posTagger;
    private Pattern convertToSpace = Pattern.compile("\\xa0");

    public TreeTaggerSentenceChunker() throws IOException {
        this.posTagger = DefaultObjects.getDefaultPosTagger();
    }

    @Override
    public ChunkedSentence chunkSentence(String sent) throws ChunkerException {
        // OpenNLP cannot handle non-breaking whitespace
        sent = convertToSpace.matcher(sent).replaceAll(" ");

        try {
            String treeTaggerOutput = chunk(sent);
            return convert(treeTaggerOutput);
        } catch (Exception e) {
            throw new ChunkerException("Could not process sentence '" + sent + "'", e);
        }
    }


    /**
     * Tokenize and chunk the given string using TreeTagger.
     *
     * @param str the string
     * @return the output string of TreeTagger
     * @throws java.io.IOException  if the TreeTagger command could not be executed or if the result
     *                              could not be read
     * @throws InterruptedException if the process, which executes TreeTagger, got interrupted.
     */
    public String chunk(String str) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(
            new String[]{"/bin/sh", "-c",
                         "echo \"" + str + "\" | " + CHUNK_COMMAND}
        );
        p.waitFor();

        InputStream in = new BufferedInputStream(p.getInputStream());
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader buff = new BufferedReader(isr);

        String output = "";
        String line;
        while ((line = buff.readLine()) != null) {
            output += System.getProperty("line.separator") + line;
        }

        in.close();
        buff.close();
        p.destroy();

        return output.trim();
    }


    /**
     * Given the output of TreeTagger, convert it into a ChunkedSentence.
     *
     * @param content the output of TreeTagger
     * @return a ChunkedSentence
     */
    public ChunkedSentence convert(String content) throws IOException {
        List<String> chunkTags = new ArrayList<>();
        List<String> tokens = new ArrayList<>();

        String[] lines = content.split(System.getProperty("line.separator"));

        String currentTag = "";
        boolean inChunk = false;
        int count = 0;

        // the output format of the TreeTagger is
        // <chunk-tag>
        // token  pos-tag   lemma
        // </chunk-tag>

        for (String line : lines) {
            // end of chunk
            if (line.startsWith("</") & line.endsWith(">")) {
                // add the chunk tag 'count'-times
                chunkTags.add("B-" + currentTag);
                for (int i = 1; i < count; i++) {
                    chunkTags.add("I-" + currentTag);
                }
                // reset values
                currentTag = "";
                inChunk = false;
                count = 0;

                // begin of chunk
            } else if (line.startsWith("<") & line.endsWith(">")) {
                currentTag = line.substring(1, line.length() - 1);

                // change name of tag so that it matches the English tags
                switch (currentTag) {
                    case "NC":
                        currentTag = "NP"; // noun phrase
                        break;
                    case "VC":
                        currentTag = "VP"; // verb phrase
                        break;
                    case "PC":
                        currentTag = "PP"; // prepositional phrase
                        break;
                }
                inChunk = true;

                // token
            } else {
                String[] parts = line.split("\t");
                tokens.add(parts[0]);
                // token belongs to a chunk
                if (inChunk) {
                    count++;

                    // token does not belong to a chunk
                } else {
                    chunkTags.add("O");
                }
            }
        }

        String[] tokenArr = tokens.toArray(new String[tokens.size()]);
        String[] chunkTagArr = chunkTags.toArray(new String[chunkTags.size()]);
        String[] posTagArr = posTagger.tag(tokenArr);

        ArrayList<Range> ranges = new ArrayList<>();
        int start = 0;
        for (String token : tokenArr) {
            ranges.add(Range.fromInterval(start, start + token.length()));
            start = start + token.length();
        }

        return new ChunkedSentence(ranges.toArray(new Range[ranges.size()]), tokenArr, posTagArr,
                                   chunkTagArr);
    }

}
