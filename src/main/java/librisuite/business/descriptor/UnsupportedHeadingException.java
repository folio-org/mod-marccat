package librisuite.business.descriptor;

import librisuite.business.exception.LibrisuiteException;

public class UnsupportedHeadingException extends LibrisuiteException {

	public UnsupportedHeadingException() {
		super();
	}

	public UnsupportedHeadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedHeadingException(String message) {
		super(message);
	}

	public UnsupportedHeadingException(Throwable cause) {
		super(cause);
	}

}
