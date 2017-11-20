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
public class EquivalentException extends LibrisuiteException {

	/**
	 * @see LibrisuiteException#LibrisuiteException()
	 */
	public EquivalentException() {
		super();
	}

	/**
	 * @see LibrisuiteException#LibrisuiteException(String)
	 */
	public EquivalentException(String message) {
		super(message);
	}

	/**
	 * @see LibrisuiteException#LibrisuiteException(String, Throwable)
	 */
	public EquivalentException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see LibrisuiteException#LibrisuiteException(Throwable)
	 */
	public EquivalentException(Throwable cause) {
		super(cause);
	}

}
