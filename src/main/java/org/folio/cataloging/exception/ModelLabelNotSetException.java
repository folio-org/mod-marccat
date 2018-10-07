/*
 * (c) LibriCore
 *
 * Created on Sep 19, 2005
 *
 * ModelLabelNotSetException.java
 */
package org.folio.cataloging.exception;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:01:25 $
 * @since 1.0
 */
public class ModelLabelNotSetException extends ModCatalogingException {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public ModelLabelNotSetException() {
    super ( );
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public ModelLabelNotSetException(String message) {
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
  public ModelLabelNotSetException(String message, Throwable cause) {
    super (message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public ModelLabelNotSetException(Throwable cause) {
    super (cause);
    // TODO Auto-generated constructor stub
  }

}
