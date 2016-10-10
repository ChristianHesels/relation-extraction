package de.hpi.extractor;

import de.hpi.nlp.NlpException;

/**
 * An exception class for errors related to {@link Extractor}.
 *
 * @author afader
 */
public class ExtractorException extends NlpException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ExtractorException(String message) {
        super(message);
    }

    public ExtractorException(String message, Exception cause) {
        super(message, cause);
    }

    public ExtractorException(Exception cause) {
        super(cause);
    }

}
