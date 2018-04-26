/*
 * (c) LibriCore
 * 
 * Created on Dec 17, 2004
 * 
 * NoResultsFoundException.java
 */
package org.folio.cataloging.business.searching;

import org.folio.cataloging.exception.ModCatalogingException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/01/13 12:02:02 $
 * @since 1.0
 */
public class NoResultsFoundException extends ModCatalogingException {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public NoResultsFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @since 1.0
	 */
	public NoResultsFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @param cause
	 * @since 1.0
	 */
	public NoResultsFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 * @since 1.0
	 */
	public NoResultsFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
