package org.folio.cataloging.business.digital;

import org.folio.cataloging.exception.LibrisuiteException;

public class DigitalTagFormatException extends LibrisuiteException {

	public DigitalTagFormatException() {
		super();
	}
	
	public DigitalTagFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public DigitalTagFormatException(String message) {
		super(message);
	}

	public DigitalTagFormatException(Throwable cause) {
		super(cause);
	}

}
