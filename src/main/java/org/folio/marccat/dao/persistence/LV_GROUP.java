/*
 * (c) LibriCore
 *
 * Created on 19-ene-2005
 *
 * LV_GROUP.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LV_GROUP implements Serializable {

  private int groupNumber;
  private String groupName;
  private String groupDescription;
  private String groupPassword;
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
   * @return Returns the groupDescription.
   * @throws
   * @since 1.0
   */
  public String getGroupDescription() {
    return groupDescription;
  }

  /**
   * @param groupDescription The groupDescription to set.
   * @throws
   * @since 1.0
   */
  public void setGroupDescription(String groupDescription) {
    this.groupDescription = groupDescription;
  }

  /**
   * @return Returns the groupName.
   * @throws
   * @since 1.0
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * @param groupName The groupName to set.
   * @throws
   * @since 1.0
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
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
   * @return Returns the groupPassword.
   * @throws
   * @since 1.0
   */
  public String getGroupPassword() {
    return groupPassword;
  }

  /**
   * @param groupPassword The groupPassword to set.
   * @throws
   * @since 1.0
   */
  public void setGroupPassword(String groupPassword) {
    this.groupPassword = groupPassword;
  }
}
