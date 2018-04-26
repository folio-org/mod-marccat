package org.folio.cataloging.business.digital;

import org.folio.cataloging.exception.ModCatalogingException;

public class DigitalDoiException extends ModCatalogingException {

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
