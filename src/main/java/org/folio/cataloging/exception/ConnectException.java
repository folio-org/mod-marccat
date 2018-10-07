/*
 * Created on May 5, 2004
 *
 */
package org.folio.cataloging.exception;


/**
 * Exception with socket connections to server processes
 *
 * @author paulm
 */
public class ConnectException extends ModCatalogingException {

  /**
   * Class constructor
   *
   * @param arg0
   * @since 1.0
   */
  public ConnectException(String arg0) {
    super (arg0);
  }

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public ConnectException() {
    super ( );
  }


}
