/*
 * (c) LibriCore
 *
 * Created on Aug 9, 2004
 *
 * XslTransformerException.java
 */
package org.folio.marccat.exception;

/**
 * This exception is thrown when there is a XSLT exception.
 *
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2004/08/09 11:43:44 $
 * @since 1.0
 */
public class XslTransformerException extends ModCatalogingException {

  /**
   * @see Exception#Exception()
   */
  public XslTransformerException() {
    super();
  }

  /**
   * @see Exception#Exception(String)
   */
  public XslTransformerException(String message) {
    super(message);
  }

  /**
   * @see Exception#Exception(String, Throwable)
   */
  public XslTransformerException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @see Exception#Exception(Throwable)
   */
  public XslTransformerException(Throwable cause) {
    super(cause);
  }

}
