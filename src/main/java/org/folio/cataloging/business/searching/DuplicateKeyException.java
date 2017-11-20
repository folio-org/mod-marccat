/*
 * (c) LibriCore
 * 
 * Created on Dec 17, 2004
 * 
 * NoResultsFoundException.java
 */
package org.folio.cataloging.business.searching;

import org.folio.cataloging.business.common.DataAccessException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/01/13 12:02:02 $
 * @since 1.0
 */
public class DuplicateKeyException extends DataAccessException {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public DuplicateKeyException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @since 1.0
	 */
	public DuplicateKeyException(String message) {
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
	public DuplicateKeyException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 * @since 1.0
	 */
	public DuplicateKeyException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
