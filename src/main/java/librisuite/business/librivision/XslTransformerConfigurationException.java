/*
 * (c) LibriCore
 * 
 * Created on Aug 9, 2004
 * 
 * XslTransformerConfigurationException.java
 */
package librisuite.business.librivision;

import librisuite.business.exception.LibrisuiteException;

/**
 * This exception is thrown when there is a XSLT configuration exception.
 * 
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2004/08/09 11:43:44 $
 * @since 1.0
 */
public class XslTransformerConfigurationException extends LibrisuiteException {

	/**
	 * @see Exception#Exception()
	 */
	public XslTransformerConfigurationException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public XslTransformerConfigurationException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public XslTransformerConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public XslTransformerConfigurationException(Throwable cause) {
		super(cause);
	}

}
