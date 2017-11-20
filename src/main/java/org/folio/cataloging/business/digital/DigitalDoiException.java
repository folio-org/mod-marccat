package org.folio.cataloging.business.digital;

import org.folio.cataloging.exception.LibrisuiteException;

public class DigitalDoiException extends LibrisuiteException {

	private static final long serialVersionUID = 250170570458564169L;

	public DigitalDoiException() {
		super();
	}
	
	public DigitalDoiException(String message, Throwable cause) {
		super(message, cause);
	}

	public DigitalDoiException(String message) {
		super(message);
	}

	public DigitalDoiException(Throwable cause) {
		super(cause);
	}

}
