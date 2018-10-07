/*
 * (c) LibriCore
 *
 * Created on Oct 21, 2005
 *
 * InvalidDescriptorException.java
 */
package org.folio.cataloging.exception;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/10/21 13:33:25 $
 * @since 1.0
 */
public class InvalidDescriptorException extends ModCatalogingException {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public InvalidDescriptorException() {
    super ( );
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public InvalidDescriptorException(String message) {
    super (message);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public InvalidDescriptorException(String message, Throwable cause) {
    super (message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public InvalidDescriptorException(Throwable cause) {
    super (cause);
    // TODO Auto-generated constructor stub
  }

}
