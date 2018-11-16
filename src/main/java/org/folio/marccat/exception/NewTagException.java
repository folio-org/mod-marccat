/*
 * (c) LibriCore
 *
 * Created on Aug 13, 2004
 *
 * NewTagException.java
 */
package org.folio.marccat.exception;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2004/09/16 10:12:53 $
 * @since 1.0
 */
public class NewTagException extends ModCatalogingException {

  /**
   *
   */
  public NewTagException() {
    super();
  }

  /**
   * @param message
   */
  public NewTagException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public NewTagException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public NewTagException(Throwable cause) {
    super(cause);
  }

}
