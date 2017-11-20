package org.folio.cataloging.business.marchelper.parser;

import org.folio.cataloging.business.marchelper.MarcHelperException;

public class PunctuationParsingException extends MarcHelperException {

	/**
	 * 
	 */
	public PunctuationParsingException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PunctuationParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public PunctuationParsingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PunctuationParsingException(Throwable cause) {
		super(cause);
	}

}
