/*
 * Standard interface to authentication brokers
 * Created on May 5, 2004
 */
package org.folio.cataloging.business.authentication;

import org.folio.cataloging.exception.ConnectException;


/**
 * A common interface for authentication agents for managing access to resources.
 *
 * @author paulm
 */
public interface AuthenticationBroker {
  /**
   * Validates the given user and password for access to the application.
   *
   * @param userName The name of the user to be validated.
   * @param password The clear password of the user to be validated
   * @return none
   * @throws AuthenticationException (and sub-types) for specific authentication failures.
   * @throws ConnectException        if problems connecting to the authentication broker.
   * @since 1.0
   */
  public void authenticateUser(String userName, String password)
    throws ConnectException, AuthenticationException;

  /**
   * Changes the password for the given user.
   *
   * @param userName    The name of the user.
   * @param oldPassword The clear password of the user (before the change)
   * @param newPassword The new password to be applied (in the clear)
   * @return none
   * @throws AuthenticationException (and sub-types) for specific authentication failures.
   * @throws ConnectException        if problems connecting to the authentication broker.
   * @since 1.0
   */
  public void changePassword(
    String userName,
    String oldPassword,
    String newPassword)
    throws ConnectException, AuthenticationException;
}
