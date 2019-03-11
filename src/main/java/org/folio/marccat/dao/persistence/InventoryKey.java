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
public class InventoryKey implements Serializable {
  private int inventoryNumber;
  private int mainLibraryNumber;

  /**
   * override equals and hashcode for hibernate key comparison
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof InventoryKey) {
      InventoryKey aKey = (InventoryKey) anObject;
      return (
        inventoryNumber == aKey.getInventoryNumber()
          && mainLibraryNumber == aKey.getMainLibraryNumber());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return inventoryNumber + mainLibraryNumber;
  }

  public int getInventoryNumber() {
    return inventoryNumber;
  }

  public void setInventoryNumber(int inventoryNumber) {
    this.inventoryNumber = inventoryNumber;
  }

  public int getMainLibraryNumber() {
    return mainLibraryNumber;
  }

  public void setMainLibraryNumber(int mainLibraryNumber) {
    this.mainLibraryNumber = mainLibraryNumber;
  }


}
