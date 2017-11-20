/*
 * (c) LibriCore
 * 
 * Created on Jul 2, 2004
 * 
 * DataAccessException.java
 */
package org.folio.cataloging.business.common;

import org.folio.cataloging.exception.LibrisuiteException;


/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class DataAccessException extends LibrisuiteException {

	/**
	 * @see LibrisuiteException#LibrisuiteException()
	 */
	public DataAccessException() {
		super();
	}

	/**
	 * @see LibrisuiteException#LibrisuiteException(String)
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * @see LibrisuiteException#LibrisuiteException(String, Throwable)
	 */
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see LibrisuiteException#LibrisuiteException(Throwable)
	 */
	public DataAccessException(Throwable cause) {
		super(cause);
	}

}
