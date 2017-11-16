/*
 * Created on Jun 10, 2004
 */
package librisuite.business.authentication;

import librisuite.business.exception.LibrisuiteException;

/**
 * An extendable exception to represent authentication exceptions thrown by implementations
 * of the AuthenticationBroker interface
 * 
 * @author paulm
 */
public class AuthenticationException extends LibrisuiteException {

	/**
	 * 
	 */
	public AuthenticationException() {
		super();
	}

	/**
	 * @param message
	 */
	public AuthenticationException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public AuthenticationException(Throwable cause) {
		super(cause);
	}

}
