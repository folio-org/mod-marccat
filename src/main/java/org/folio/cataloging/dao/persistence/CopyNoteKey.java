/*
 * (c) LibriCore
 *
 * Created on 09-jul-2004
 *
 * CopyNoteKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * Represents composite key for HLDG_NTE class
 *
 * @author elena
 * @version $Revision: 1.3 $, $Date: 2004/08/03 13:37:53 $
 * @since 1.0
 */

public class CopyNoteKey implements Serializable {
  private int copyIdNumber;
  private int copyNoteNumber;


  /**
   * Class constructor
   */
  public CopyNoteKey() {
    super();
  }

  public CopyNoteKey(int copyId, int copyNote) {
    this.setCopyIdNumber(copyId);
    this.setCopyNoteNumber(copyNote);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof CopyNoteKey) {
      CopyNoteKey aKey = (CopyNoteKey) anObject;
      return (
        copyIdNumber == aKey.getCopyIdNumber()
          && copyNoteNumber == aKey.getCopyNoteNumber());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return copyIdNumber + copyNoteNumber;
  }

  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  public void setCopyIdNumber(int i) {
    copyIdNumber = i;
  }

  public int getCopyNoteNumber() {
    return copyNoteNumber;
  }

  public void setCopyNoteNumber(int i) {
    copyNoteNumber = i;
  }

}
