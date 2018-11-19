/*
 * (c) LibriCore
 *
 * Created on Aug 9, 2004
 *
 * XmlDocumentException.java
 */
package org.folio.marccat.exception;

/**
 * This exception is thrown when there is a XML Document exception.
 *
 * @author Wim Crols
 * @version $Revision: 1.1 $, $Date: 2004/08/09 11:43:44 $
 * @since 1.0
 */
public class XmlDocumentException extends ModMarccatException {

  /**
   * @see Exception#Exception(String)
   */
  public XmlDocumentException(String message) {
    super(message);
  }


}
