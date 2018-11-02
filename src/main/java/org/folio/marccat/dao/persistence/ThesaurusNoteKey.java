/*
 * (c) LibriCore
 *
 * Created on 09-jul-2004
 *
 * CopyNoteKey.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * Represents composite key for HLDG_NTE class
 *
 * @author Carmen
 * @version $Revision: 1.3 $, $Date: 2010/02/26 13:37:53 $
 * @since 1.0
 */

public class ThesaurusNoteKey implements Serializable {
  private int headingNumber;
  private int noteNumber;


  /**
   * Class constructor
   */
  public ThesaurusNoteKey() {
    super();
  }

  public ThesaurusNoteKey(int headingNumber, int noteNumber) {
    this.setHeadingNumber(headingNumber);
    this.setNoteNumber(noteNumber);
  }

  public int getHeadingNumber() {
    return headingNumber;
  }

  public void setHeadingNumber(int headingNumber) {
    this.headingNumber = headingNumber;
  }

  public int getNoteNumber() {
    return noteNumber;
  }

  public void setNoteNumber(int noteNumber) {
    this.noteNumber = noteNumber;
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof ThesaurusNoteKey) {
      ThesaurusNoteKey aKey = (ThesaurusNoteKey) anObject;
      return (
        headingNumber == aKey.getHeadingNumber()
          && noteNumber == aKey.getNoteNumber());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return headingNumber + noteNumber;
  }


}
