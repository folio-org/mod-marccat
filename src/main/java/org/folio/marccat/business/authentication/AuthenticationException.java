/*
 * Created on Jun 10, 2004
 */
package org.folio.marccat.business.authentication;

import org.folio.marccat.exception.ModCatalogingException;

/**
 * An extendable exception to represent authentication exceptions thrown by implementations
 * of the AuthenticationBroker interface
 *
 * @author paulm
 */
public class AuthenticationException extends ModCatalogingException {

  /**
   *
   */
  public AuthenticationException() {
    super();
  }

  /**
   * @param message
   */
  public AuthenticationException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public AuthenticationException(Throwable cause) {
    super(cause);
  }

}
