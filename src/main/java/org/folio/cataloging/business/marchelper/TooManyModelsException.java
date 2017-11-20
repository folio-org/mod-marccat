package org.folio.cataloging.business.marchelper;

import org.folio.cataloging.exception.LibrisuiteException;

public class TooManyModelsException extends LibrisuiteException {

	public TooManyModelsException() {
		super();
	}

	public TooManyModelsException(String message, Throwable cause) {
		super(message, cause);
	}

	public TooManyModelsException(String message) {
		super(message);
	}

	public TooManyModelsException(Throwable cause) {
		super(cause);
	}

}
