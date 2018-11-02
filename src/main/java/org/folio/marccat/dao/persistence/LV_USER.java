/*
 * (c) LibriCore
 *
 * Created on 21-ene-2005
 *
 * LV_USER.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LV_USER implements Serializable {

  private int userNumber;
  private int personNumber;
  private String userLogin;
  private String userPassword;
  private String userDescription;
  private short deletionIndicator;


  /**
   * @return Returns the deletionIndicator.
   * @throws
   * @since 1.0
   */
  public short getDeletionIndicator() {
    return deletionIndicator;
  }

  /**
   * @param deletionIndicator The deletionIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setDeletionIndicator(short deletionIndicator) {
    this.deletionIndicator = deletionIndicator;
  }

  /**
   * @return Returns the personNumber.
   * @throws
   * @since 1.0
   */
  public int getPersonNumber() {
    return personNumber;
  }

  /**
   * @param personNumber The personNumber to set.
   * @throws
   * @since 1.0
   */
  public void setPersonNumber(int personNumber) {
    this.personNumber = personNumber;
  }

  /**
   * @return Returns the userDescription.
   * @throws
   * @since 1.0
   */
  public String getUserDescription() {
    return userDescription;
  }

  /**
   * @param userDescription The userDescription to set.
   * @throws
   * @since 1.0
   */
  public void setUserDescription(String userDescription) {
    this.userDescription = userDescription;
  }

  /**
   * @return Returns the userLogin.
   * @throws
   * @since 1.0
   */
  public String getUserLogin() {
    return userLogin;
  }

  /**
   * @param userLogin The userLogin to set.
   * @throws
   * @since 1.0
   */
  public void setUserLogin(String userLogin) {
    this.userLogin = userLogin;
  }

  /**
   * @return Returns the userNumber.
   * @throws
   * @since 1.0
   */
  public int getUserNumber() {
    return userNumber;
  }

  /**
   * @param userNumber The userNumber to set.
   * @throws
   * @since 1.0
   */
  public void setUserNumber(int userNumber) {
    this.userNumber = userNumber;
  }

  /**
   * @return Returns the userPassword.
   * @throws
   * @since 1.0
   */
  public String getUserPassword() {
    return userPassword;
  }

  /**
   * @param userPassword The userPassword to set.
   * @throws
   * @since 1.0
   */
  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }
}
