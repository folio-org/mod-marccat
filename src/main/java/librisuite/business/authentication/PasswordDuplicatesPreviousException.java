/*
 * (c) LibriCore
 * 
 * Created on Jun 11, 2004
 * 
 * PasswordDuplicatesPreviousException.java
 */
package librisuite.business.authentication;

/**
 * Denotes an attempt to reuse a previous password when
 * account settings restrict this
 * @author paulm
 */
public class PasswordDuplicatesPreviousException
	extends AuthenticationException {

	/**
	 * 
	 */
	public PasswordDuplicatesPreviousException() {
		super();
	}

	/**
	 * @param message
	 */
	public PasswordDuplicatesPreviousException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PasswordDuplicatesPreviousException(
		String message,
		Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public PasswordDuplicatesPreviousException(Throwable cause) {
		super(cause);
	}

}
