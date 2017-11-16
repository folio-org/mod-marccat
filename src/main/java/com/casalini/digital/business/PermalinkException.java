package com.casalini.digital.business;

import librisuite.business.exception.LibrisuiteException;

public class PermalinkException extends LibrisuiteException {

	private static final long serialVersionUID = 250170570458564169L;

	public PermalinkException() {
		super();
	}
	
	public PermalinkException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermalinkException(String message) {
		super(message);
	}

	public PermalinkException(Throwable cause) {
		super(cause);
	}

}
