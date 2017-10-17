/*
 * (c) LibriCore
 * 
 * Created on Jul 2, 2004
 * 
 * DataAccessException.java
 */
package librisuite.business.common;

import librisuite.business.exception.LibrisuiteException;


/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class CollectionException extends LibrisuiteException {

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException()
	 */
	public CollectionException() {
		super();
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(String)
	 */
	public CollectionException(String message) {
		super(message);
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(String, Throwable)
	 */
	public CollectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(Throwable)
	 */
	public CollectionException(Throwable cause) {
		super(cause);
	}

}
