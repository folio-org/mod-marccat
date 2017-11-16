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
 * @version $Revision: 1.1 $, $Date: 2004/12/02 08:44:16 $
 * @since 1.0
 */
public class XmlUnsupportedEncodingException extends LibrisuiteException {

	/**
	 * @see Exception#Exception()
	 */
	public XmlUnsupportedEncodingException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public XmlUnsupportedEncodingException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public XmlUnsupportedEncodingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public XmlUnsupportedEncodingException(Throwable cause) {
		super(cause);
	}

}
