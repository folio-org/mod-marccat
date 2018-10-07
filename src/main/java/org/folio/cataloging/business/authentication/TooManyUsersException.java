/*
 * (c) LibriCore
 *
 * Created on Jun 10, 2004
 *
 * TooManyUsersException.java
 */
package org.folio.cataloging.business.authentication;

/**
 * Denotes that there are currently too many concurrent users and
 * therefore the user is not able to logon at this time
 *
 * @author paulm
 */
public class TooManyUsersException extends AuthenticationException {

  /**
   *
   */
  public TooManyUsersException() {
    super ( );
  }

  /**
   * @param message
   */
  public TooManyUsersException(String message) {
    super (message);
  }

  /**
   * @param message
   * @param cause
   */
  public TooManyUsersException(String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * @param cause
   */
  public TooManyUsersException(Throwable cause) {
    super (cause);
  }

}
