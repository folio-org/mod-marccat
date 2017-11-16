/*
 * (c) LibriCore
 * 
 * Created on Aug 9, 2004
 * 
 * XmlDocumentException.java
 */
package librisuite.business.librivision;

import librisuite.business.exception.LibrisuiteException;

/**
 * This exception is thrown when there is a XML Document exception.
 * 
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2004/08/09 11:43:44 $
 * @since 1.0
 */
public class XmlDocumentException extends LibrisuiteException {

	/**
	 * @see Exception#Exception()
	 */
	public XmlDocumentException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public XmlDocumentException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public XmlDocumentException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public XmlDocumentException(Throwable cause) {
		super(cause);
	}

}
