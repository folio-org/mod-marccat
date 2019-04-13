/*
 * (c) LibriCore
 *
 * Created on Jun 15, 2004
 *
 * NME_HDG_KEY.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * Represents composite key for NME_HDG class
 *
 * @author paulm
 * @version $Revision: 1.7 $, $Date: 2005/07/13 12:45:10 $
 * @since 1.0
 */
public class DescriptorKey implements Serializable {
  private int keyNumber;
  private String userViewString;

  /**
   * Class constructor
   */
  public DescriptorKey() {
    super();
    keyNumber = -1;
    userViewString = "0000000000000000";
  }

  /**
   * Class constructor
   */
  public DescriptorKey(int keyNumber, String view) {
    this.setKeyNumber(keyNumber);
    this.setUserViewString(view);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof DescriptorKey) {
      DescriptorKey aKey = (DescriptorKey) anObject;
      return (
        keyNumber == aKey.getKeyNumber()
          && userViewString.equals(aKey.getUserViewString()));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return keyNumber + userViewString.hashCode();
  }

  /**
   * Getter for keyNumber
   *
   * @return keyNumber
   */
  public int getKeyNumber() {
    return keyNumber;
  }

  /**
   * Setter for keyNumber
   *
   * @param i keyNumber
   */
  public void setKeyNumber(int i) {
    keyNumber = i;
  }

  /**
   * Getter for view
   *
   * @return view
   */
  public String getUserViewString() {
    return userViewString;
  }

  /**
   * Setter for view
   *
   * @param string view
   */
  public void setUserViewString(String string) {
    userViewString = string;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return "Heading nbr: " + (keyNumber) + "  User view: " + userViewString;
  }

}
