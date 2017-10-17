/*
 * (c) LibriCore
 * 
 * Created on Dec 8, 2004
 * 
 * MarcLoadingException.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.business.exception.LibrisuiteException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class MarcLoadingException extends LibrisuiteException {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public MarcLoadingException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param message
	 * @since 1.0
	 */
	public MarcLoadingException(String message) {
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
	public MarcLoadingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Class constructor
	 *
	 * @param cause
	 * @since 1.0
	 */
	public MarcLoadingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
