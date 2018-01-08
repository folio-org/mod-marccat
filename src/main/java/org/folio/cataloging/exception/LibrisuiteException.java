/*
 * (c) LibriCore
 * 
 * Created on Jul 1, 2004
 * 
 * LibrisuiteException.java
 */
package org.folio.cataloging.exception;

/**
 * @author Wim Crols
 * @version $Revision: 1.3 $, $Date: 2004/07/12 10:15:16 $
 * @since 1.0
 */
public class LibrisuiteException extends RuntimeException {

	/**
	 * @see Exception#Exception()
	 */
	public LibrisuiteException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public LibrisuiteException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public LibrisuiteException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public LibrisuiteException(Throwable cause) {
		super(cause);
	}

}
