package edu.washington.cs.knowitall.nlp;

import com.google.common.base.Predicate;

import java.io.IOException;
import java.io.Reader;

import edu.washington.cs.knowitall.extractor.chunking.SentenceExtractor;
import edu.washington.cs.knowitall.io.BufferedReaderIterator;
import edu.washington.cs.knowitall.io.TextBlockIterator;
import edu.washington.cs.knowitall.util.IterableAdapter;

/**
 * A class that combines a <code>SentenceExtractor</code> with a <code>SentenceChunker</code> to
 * read <code>ChunkedSentence</code> objects from a <code>BufferedReader</code>. This object uses
 * the <code>SentenceExtractor</code> to obtain <code>String</code> sentences from the input, and
 * then chunks the sentences using the <code>SentenceChunker</code> object.
 *
 * @author afader
 */
public class ChunkedSentenceReader implements Iterable<ChunkedSentence> {

    /**
     * The singleton default sentence chunker
     */
    private static TreeTaggerSentenceChunker SENT_CHUNKER;

    private SentenceExtractor sentExtractor;
    private SentenceChunker sentChunker;
    private ChunkedSentenceIterator chunkedSentIter;

    /**
     * Constructs a reader from <code>r</code> using the sentence extractor <code>se</code> and the
     * default <code>TreeTaggerSentenceChunker</code> object.
     *
     * @param r  the reader
     * @param se the sentence extractor
     */
    public ChunkedSentenceReader(Reader r, SentenceExtractor se)
        throws IOException {
        if (SENT_CHUNKER == null) {
            SENT_CHUNKER = new TreeTaggerSentenceChunker();
        }
        init(r, se, SENT_CHUNKER);
    }

    /**
     * Constructs a reader from <code>r</code> using the chunker <code>sc</code> and the default
     * <code>SentenceExtractor</code>.
     *
     * @param r  the reader
     * @param sc the sentence chunker
     */
    public ChunkedSentenceReader(Reader r, SentenceChunker sc)
        throws IOException {
        init(r, new SentenceExtractor(), sc);
    }

    /**
     * Constructs a reader from <code>r</code> using the default <code>SentenceExtractor</code> and
     * <code>OpenNlpSentenceChunker</code>.
     *
     * @param r  the reader
     */
    public ChunkedSentenceReader(Reader r) throws IOException {
        if (SENT_CHUNKER == null) {
            SENT_CHUNKER = new TreeTaggerSentenceChunker();
        }
        init(r, new SentenceExtractor(), SENT_CHUNKER);
    }

    /**
     * Constructs a reader from <code>r</code> using the sentence extractor <code>se</code> and the
     * sentence chunker <code>sc</code>.
     *
     * @param r  the reader
     * @param se the sentence extractor
     * @param sc the sentence chunker
     */
    public ChunkedSentenceReader(Reader r, SentenceExtractor se,
                                 SentenceChunker sc) {
        init(r, se, sc);
    }

    private void init(Reader reader, SentenceExtractor sentExtractor,
                      SentenceChunker sentChunker) {
        this.sentChunker = sentChunker;
        this.sentExtractor = sentExtractor;
        BufferedReaderIterator bri = new BufferedReaderIterator(reader);
        TextBlockIterator tbi = new TextBlockIterator(bri);
        SentenceBlocksIterator sbi = new SentenceBlocksIterator(tbi,
                                                                sentExtractor);
        chunkedSentIter = new ChunkedSentenceIterator(sbi, sentChunker);
    }

    /**
     * This filter is used for sentences AFTER they have been chunked.
     *
     * @param filter the filter to add
     */
    public void addFilter(Predicate<ChunkedSentence> filter) {
        this.chunkedSentIter.addFilter(filter);
    }

    /**
     * @return the object used to extract sentences from strings.
     */
    public SentenceExtractor getSentenceExtractor() {
        return this.sentExtractor;
    }

    /**
     * @return the object used to chunk sentences.
     */
    public SentenceChunker getSentenceChunker() {
        return this.sentChunker;
    }

    /**
     * @return an iterator over the sentences from the <code>BufferedReader</code> given during
     * construction.
     */
    @Override
    public ChunkedSentenceIterator iterator() {
        return chunkedSentIter;
    }

    /**
     * @return an iterable object over the sentences from the <code>BufferedReader</code> given
     * during construction.
     */
    public Iterable<ChunkedSentence> getSentences() {
        return new IterableAdapter<ChunkedSentence>(chunkedSentIter);
    }
}
