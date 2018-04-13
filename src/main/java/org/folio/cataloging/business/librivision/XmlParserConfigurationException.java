/*
 * (c) LibriCore
 * 
 * Created on Aug 9, 2004
 * 
 * XmlDocumentException.java
 */
package org.folio.cataloging.business.librivision;

import org.folio.cataloging.exception.ModCatalogingException;

/**
 * This exception is thrown when there is a XML Document exception.
 * 
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2004/12/02 08:44:16 $
 * @since 1.0
 */
public class XmlParserConfigurationException extends ModCatalogingException {

	/**
	 * @see Exception#Exception()
	 */
	public XmlParserConfigurationException() {
		super();
	}

	/**
	 * @see Exception#Exception(String)
	 */
	public XmlParserConfigurationException(String message) {
		super(message);
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public XmlParserConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see Exception#Exception(Throwable)
	 */
	public XmlParserConfigurationException(Throwable cause) {
		super(cause);
	}

}
