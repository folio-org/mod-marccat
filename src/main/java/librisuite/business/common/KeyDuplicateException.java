package librisuite.business.common;

import librisuite.business.exception.LibrisuiteException;

public class KeyDuplicateException extends LibrisuiteException 
{
	private static final long serialVersionUID = -8207968816842862351L;

	public KeyDuplicateException() {
		super();
	}

	public KeyDuplicateException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyDuplicateException(String message) {
		super(message);
	}

	public KeyDuplicateException(Throwable cause) {
		super(cause);
	}
}
