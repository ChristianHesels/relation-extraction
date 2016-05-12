package edu.washington.cs.knowitall.nlp.chunking;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.washington.cs.knowitall.commonlib.Range;

/**
 * A representation of a token in a ChunkedSentence.
 *
 * @author schmmd
 */
public class ChunkedSentenceToken {

    public final ChunkedSentence ChunkedSentence;
    public final int index;

    public ChunkedSentenceToken(ChunkedSentence ChunkedSentence, int index) {
        this.ChunkedSentence = ChunkedSentence;
        this.index = index;
    }

    public static List<ChunkedSentenceToken> tokenize(ChunkedSentence sentence) {
        return ChunkedSentenceToken.tokenize(sentence, sentence.getRange());
    }

    public static List<ChunkedSentenceToken> tokenize(ChunkedSentence sentence, Range range) {
        List<ChunkedSentenceToken> tokens = new ArrayList<ChunkedSentenceToken>(
            sentence.getLength());
        for (int i = range.getStart(); i < range.getEnd(); i++) {
            tokens.add(new ChunkedSentenceToken(sentence, i));
        }

        return tokens;
    }

    /**
     * @return The string of this token.
     */
    public String string() {
        return this.ChunkedSentence.getTokens().get(this.index);
    }

    /**
     * @return The part of speech tag of this token.
     */
    public String pos() {
        return this.ChunkedSentence.getPosTag(this.index);
    }

    /**
     * @return The chunk tag of this token.
     */
    public String chunk() {
        return this.ChunkedSentence.getChunkTag(this.index);
    }

    public String toString() {
        return this.ChunkedSentence.getToken(index);
    }

    public static final Function<ChunkedSentenceToken, String>
        toStringFunction =
        new Function<ChunkedSentenceToken, String>() {
            @Override
            public String apply(ChunkedSentenceToken token) {
                return token.ChunkedSentence.getToken(token.index);
            }
        };

    /**
     * An expression that is evaluated against a token.
     *
     * @author schmmd
     */
    protected static abstract class Expression implements
                                               Predicate<ChunkedSentenceToken> {

    }

    /**
     * A regular expression that is evaluated against the string portion of a token.
     *
     * @author schmmd
     */
    protected static class StringExpression extends Expression {

        final Pattern pattern;

        public StringExpression(String string, int flags) {
            pattern = Pattern.compile(string, flags);
        }

        public StringExpression(String string) {
            this(string, Pattern.CASE_INSENSITIVE);
        }

        @Override
        public boolean apply(ChunkedSentenceToken token) {
            return pattern.matcher(token.string()).matches();
        }
    }

    /**
     * A regular expression that is evaluated against the POS tag portion of a token.
     *
     * @author schmmd
     */
    protected static class PosTagExpression extends Expression {

        final Pattern pattern;

        public PosTagExpression(String string, int flags) {
            pattern = Pattern.compile(string, flags);
        }

        public PosTagExpression(String string) {
            this(string, Pattern.CASE_INSENSITIVE);
        }

        @Override
        public boolean apply(ChunkedSentenceToken token) {
            return pattern.matcher(token.pos()).matches();
        }
    }

    /**
     * A regular expression that is evaluated against the chunk tag portion of a token.
     *
     * @author schmmd
     */
    protected static class ChunkTagExpression extends Expression {

        final Pattern pattern;

        public ChunkTagExpression(String string, int flags) {
            pattern = Pattern.compile(string, flags);
        }

        public ChunkTagExpression(String string) {
            this(string, Pattern.CASE_INSENSITIVE);
        }

        @Override
        public boolean apply(ChunkedSentenceToken token) {
            return pattern.matcher(token.chunk()).matches();
        }
    }
}
