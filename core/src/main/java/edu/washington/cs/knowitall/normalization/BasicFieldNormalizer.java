package edu.washington.cs.knowitall.normalization;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedExtraction;

/**
 * A field normalizer that applies the stemmer to every token and strips nothing.
 *
 * @author Rob
 */
public class BasicFieldNormalizer implements FieldNormalizer {

    private MateToolLemmatizer lexer;

    public BasicFieldNormalizer() {

        lexer = new MateToolLemmatizer();
    }

    @Override
    public NormalizedField normalizeField(ChunkedExtraction field) {

        String[] normTokens = new String[field.getLength()];

        for (int i = 0; i < field.getLength(); ++i) {

            normTokens[i] = lexer.lemmatize(field.getToken(i));
        }

        return new NormalizedField(field, normTokens, field.getPosTags()
            .toArray(new String[normTokens.length]));
    }

}
