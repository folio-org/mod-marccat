/*
 * (c) LibriCore
 *
 * Created on 21-ene-2005
 *
 * UserGroupKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class UserGroupKey implements Serializable {
  private int userNumber;
  private int groupNumber;


  /**
   * Class constructor
   */
  public UserGroupKey() {
    super ( );
  }

  public UserGroupKey(int userNumber, int groupNumber) {
    this.setUserNumber (userNumber);
    this.setGroupNumber (groupNumber);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof UserGroupKey) {
      UserGroupKey aKey = (UserGroupKey) anObject;
      return (
        userNumber == aKey.getUserNumber ( )
          && groupNumber == aKey.getGroupNumber ( ));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return userNumber + groupNumber;
  }

  /**
   * @return Returns the groupNumber.
   * @throws
   * @since 1.0
   */
  public int getGroupNumber() {
    return groupNumber;
  }

  /**
   * @param groupNumber The groupNumber to set.
   * @throws
   * @since 1.0
   */
  public void setGroupNumber(int groupNumber) {
    this.groupNumber = groupNumber;
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
}
