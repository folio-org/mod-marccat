/*
 * (c) LibriCore
 * 
 * Created on Nov 18, 2004
 * 
 * AuthorisationException.java
 */
package org.folio.cataloging.business.authorisation;

import org.folio.cataloging.exception.ModCatalogingException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/11/19 16:48:32 $
 * @since 1.0
 */
public class AuthorisationException extends ModCatalogingException {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorisationException() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @since 1.0
	 */
	public AuthorisationException(String message) {
		super(message);
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @param cause
	 * @since 1.0
	 */
	public AuthorisationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 * @since 1.0
	 */
	public AuthorisationException(Throwable cause) {
		super(cause);
	}

}
