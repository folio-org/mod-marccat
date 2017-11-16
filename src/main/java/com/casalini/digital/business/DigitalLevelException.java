package com.casalini.digital.business;

import librisuite.business.exception.LibrisuiteException;

public class DigitalLevelException extends LibrisuiteException {

	private static final long serialVersionUID = -5156470499299759964L;

	public DigitalLevelException() {
		super();
	}
	
	public DigitalLevelException(String message, Throwable cause) {
		super(message, cause);
	}

	public DigitalLevelException(String message) {
		super(message);
	}

	public DigitalLevelException(Throwable cause) {
		super(cause);
	}

}
