/*
 * (c) LibriCore
 *
 * Created on 10-ene-2005
 *
 * BorrowerNoteKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * Represents composite key for BRWR_NTE class
 *
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class BorrowerNoteKey implements Serializable {

  private int borrowerNumber;
  private int borrowerNoteNumber;


  /**
   * Class constructor
   */
  public BorrowerNoteKey() {
    super ( );
  }

  public BorrowerNoteKey(int brwrNbr, int brwrNote) {
    this.setBorrowerNumber (brwrNbr);
    this.setBorrowerNoteNumber (brwrNote);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof BorrowerNoteKey) {
      BorrowerNoteKey aKey = (BorrowerNoteKey) anObject;
      return (
        borrowerNumber == aKey.getBorrowerNumber ( )
          && borrowerNoteNumber == aKey.getBorrowerNoteNumber ( ));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return borrowerNumber + borrowerNoteNumber;
  }


  /**
   * @return Returns the borrowerNoteNumber.
   * @throws
   * @since 1.0
   */
  public int getBorrowerNoteNumber() {
    return borrowerNoteNumber;
  }

  /**
   * @param borrowerNoteNumber The borrowerNoteNumber to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerNoteNumber(int borrowerNoteNumber) {
    this.borrowerNoteNumber = borrowerNoteNumber;
  }

  /**
   * @return Returns the borrowerNumber.
   * @throws
   * @since 1.0
   */
  public int getBorrowerNumber() {
    return borrowerNumber;
  }

  /**
   * @param borrowerNumber The borrowerNumber to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerNumber(int borrowerNumber) {
    this.borrowerNumber = borrowerNumber;
  }
}
