package org.folio.marccat.exception;

/**
 * @author Elena
 *
 */
@SuppressWarnings("serial")
public class NoHeadingSetException extends ValidationException {

	public NoHeadingSetException() {
		super();
	}
	public NoHeadingSetException(String message) {
		super(message);
	}
	public NoHeadingSetException(String message, Throwable cause) {
		super(message, cause);
	}
	public NoHeadingSetException(Throwable cause) {
		super(cause);
	}
	public NoHeadingSetException(int index) {
		super(index);
	}

}
