/*
 * Created on Jun 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.folio.cataloging.business.authentication;

/**
 * Denotes that the user's password has expired
 *
 * @author paulm
 */
public final class PasswordExpiredException extends AuthenticationException {

  /**
   *
   */
  public PasswordExpiredException() {
    super();
  }

  /**
   * @param message
   */
  public PasswordExpiredException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public PasswordExpiredException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public PasswordExpiredException(Throwable cause) {
    super(cause);
  }

}
