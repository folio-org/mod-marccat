package org.folio.cataloging.business.marchelper;

public class NoMatchException extends MarcHelperException {

	public NoMatchException() {
		super();
	}

	public NoMatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMatchException(String message) {
		super(message);
	}

	public NoMatchException(Throwable cause) {
		super(cause);
	}

}
