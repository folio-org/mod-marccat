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
public class HierarchyException extends LibrisuiteException {

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException()
	 */
	public HierarchyException() {
		super();
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(String)
	 */
	public HierarchyException(String message) {
		super(message);
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(String, Throwable)
	 */
	public HierarchyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(Throwable)
	 */
	public HierarchyException(Throwable cause) {
		super(cause);
	}

}
