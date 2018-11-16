/*
 * (c) LibriCore
 *
 * Created on Aug 9, 2004
 *
 * XslTransformerConfigurationException.java
 */
package org.folio.marccat.search;

import org.folio.marccat.exception.ModCatalogingException;

/**
 * This exception is thrown when there is a XSLT configuration exception.
 *
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2004/08/09 11:43:44 $
 * @since 1.0
 */
public class XslTransformerConfigurationException extends ModCatalogingException {

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
