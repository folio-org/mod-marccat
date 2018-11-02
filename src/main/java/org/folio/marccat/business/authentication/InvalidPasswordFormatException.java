/*
 * (c) LibriCore
 *
 * Created on Jun 11, 2004
 *
 * InvalidPasswordFormatException.java
 */
package org.folio.marccat.business.authentication;

/**
 * Denotes an improper format for password
 *
 * @author paulm
 */
public class InvalidPasswordFormatException extends AuthenticationException {

  /**
   *
   */
  public InvalidPasswordFormatException() {
    super();
  }

  /**
   * @param message
   */
  public InvalidPasswordFormatException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public InvalidPasswordFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public InvalidPasswordFormatException(Throwable cause) {
    super(cause);
  }

}
