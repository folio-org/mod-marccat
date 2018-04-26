/*
 * (c) LibriCore
 * 
 * Created on Jul 2, 2004
 * 
 * DataAccessException.java
 */
package org.folio.cataloging.business.common;

import org.folio.cataloging.exception.ModCatalogingException;


/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class DataAccessException extends ModCatalogingException {

	/**
	 * @see ModCatalogingException#ModCatalogingException()
	 */
	public DataAccessException() {
		super();
	}

	/**
	 * @see ModCatalogingException#ModCatalogingException(String)
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * @see ModCatalogingException#ModCatalogingException(String, Throwable)
	 */
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see ModCatalogingException#ModCatalogingException(Throwable)
	 */
	public DataAccessException(Throwable cause) {
		super(cause);
	}

}
