package edu.washington.cs.knowitall.nlp;

import com.google.common.base.Joiner;

import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

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
 * A class that combines OpenNLP tokenizer, POS tagger and Tree Tagger chunker objects into a single
 * object that converts String sentences to {@link ChunkedSentence} objects.
 */
public class TreeTaggerSentenceChunker implements SentenceChunker {

    private static final String
        TREETAGGER_HOME = System.getenv("TREETAGGER_HOME");
    private static final String CHUNK_COMMAND = TREETAGGER_HOME + "cmd/tagger-chunker-german";

    private POSTagger posTagger;
    private Tokenizer tokenizer;

    Pattern convertToSpace = Pattern.compile("\\xa0");

    public TreeTaggerSentenceChunker() throws IOException {
        this.tokenizer = DefaultObjects.getDefaultTokenizer();
        this.posTagger = DefaultObjects.getDefaultPosTagger();
    }

    @Override
    public ChunkedSentence chunkSentence(String sent) throws ChunkerException {
        // OpenNLP cannot handle non-breaking whitespace
        sent = convertToSpace.matcher(sent).replaceAll(" ");

        ArrayList<Range> ranges;
        String[] tokens, posTags, npChunkTags;

        // OpenNLP can throw a NullPointerException. Catch it, and raise it
        // as a checked exception.
        // TODO: try to figure out what caused the NPE and actually fix the problem
        try {
            Span[] offsets = tokenizer.tokenizePos(sent);
            ranges = new ArrayList<Range>();
            ArrayList<String> tokenList = new ArrayList<String>();
            for (Span span : offsets) {
                ranges.add(Range.fromInterval(span.getStart(), span.getEnd()));
                tokenList.add(sent.substring(span.getStart(), span.getEnd()));
            }
            tokens = tokenList.toArray(new String[tokenList.size()]);

            posTags = posTagger.tag(tokens);
            try {
                npChunkTags = chunk(tokens);
            } catch (IOException | InterruptedException e) {
                throw new ChunkerException("TreeTagger threw an exception on '" + sent + "'", e);
            }

        } catch (NullPointerException e) {
            throw new ChunkerException("OpenNLP threw NPE on '" + sent + "'", e);
        }

        return new ChunkedSentence(ranges.toArray(new Range[ranges.size()]), tokens, posTags,
                                   npChunkTags);
    }


    /**
     * Chunk the given tokens using TreeTagger.
     *
     * @param tokens the tokens
     * @return an Array containing the chunk tags
     * @throws java.io.IOException  if the TreeTagger command could not be executed or if the result
     *                              could not be read
     * @throws InterruptedException if the process, which executes TreeTagger, got interrupted.
     */
    private String[] chunk(String[] tokens) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(
            new String[]{"/bin/sh", "-c",
                         "echo \"" + Joiner.on(" ").join(tokens) + "\" | " + CHUNK_COMMAND}
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

        return getPhrases(output.trim());
    }


    /**
     * Given the output of the TreeTagger, convert it into an array of chunk tags.
     *
     * @param content the output of the TreeTagger
     * @return an array containing the chunk tags
     */
    private String[] getPhrases(String content) throws IOException {
        List<String> chunkTags = new ArrayList<String>();

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
                if (currentTag.equals("NC")) {
                    currentTag = "NP"; // noun phrase

                } else if (currentTag.equals("VC")) {
                    currentTag = "VP"; // verb phrase

                } else if (currentTag.equals("PC")) {
                    currentTag = "PP"; // prepositional phrase

                }
                inChunk = true;

                // token
            } else {
                // token belongs to a chunk
                if (inChunk) {
                    count++;

                    // token does not belong to a chunk
                } else {
                    chunkTags.add("O");
                }
            }
        }

        return chunkTags.toArray(new String[chunkTags.size()]);
    }

}
