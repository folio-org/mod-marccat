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
public class HierarchyDeleteException extends LibrisuiteException {

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException()
	 */
	public HierarchyDeleteException() {
		super();
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(String)
	 */
	public HierarchyDeleteException(String message) {
		super(message);
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(String, Throwable)
	 */
	public HierarchyDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see librisuite.business.exception.LibrisuiteException#LibrisuiteException(Throwable)
	 */
	public HierarchyDeleteException(Throwable cause) {
		super(cause);
	}

}
