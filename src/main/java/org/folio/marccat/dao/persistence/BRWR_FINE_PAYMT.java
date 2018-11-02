/*
 * (c) LibriCore
 *
 * Created on 19-nov-2004
 *
 * BRWR_FINE_PAYMT.java
 */

package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class BRWR_FINE_PAYMT implements Serializable {

  private int borrowerFineNumber;
  private Date borrowerFinePaymentTimeStamp;
  private int borrowerNumber;
  private float borrowerFinePaymentAmount;
  private char finePaymentAdjmtIndicator;
  private String finePaymentAdjmtNote;

  public BRWR_FINE_PAYMT() {
    super();
  }

  public BRWR_FINE_PAYMT(int fineNbr, Date pay, int brwrNbr, float amount, char indic) {
    this.borrowerFineNumber = fineNbr;
    this.borrowerFinePaymentTimeStamp = pay;
    this.borrowerNumber = brwrNbr;
    this.borrowerFinePaymentAmount = amount;
    this.finePaymentAdjmtIndicator = indic;
  }

  /**
   * @return Returns the borrowerFineNumber.
   */
  public int getBorrowerFineNumber() {
    return borrowerFineNumber;
  }

  /**
   * @param borrowerFineNumber The borrowerFineNumber to set.
   */
  public void setBorrowerFineNumber(int borrowerFineNumber) {
    this.borrowerFineNumber = borrowerFineNumber;
  }

  /**
   * @return Returns the borrowerFinePaymentAmount.
   */
  public float getBorrowerFinePaymentAmount() {
    return borrowerFinePaymentAmount;
  }

  /**
   * @param borrowerFinePaymentAmount The borrowerFinePaymentAmount to set.
   */
  public void setBorrowerFinePaymentAmount(float borrowerFinePaymentAmount) {
    this.borrowerFinePaymentAmount = borrowerFinePaymentAmount;
  }

  public Date getBorrowerFinePaymentTimeStamp() {
    return borrowerFinePaymentTimeStamp;
  }

  /**
   * @param borrowerFinePaymentTimeStamp The borrowerFinePaymentTimeStamp to set.
   */
  public void setBorrowerFinePaymentTimeStamp(
    Date borrowerFinePaymentTimeStamp) {
    this.borrowerFinePaymentTimeStamp = borrowerFinePaymentTimeStamp;
  }

  /**
   * @return Returns the borrowerNumber.
   */
  public int getBorrowerNumber() {
    return borrowerNumber;
  }

  /**
   * @param borrowerNumber The borrowerNumber to set.
   */
  public void setBorrowerNumber(int borrowerNumber) {
    this.borrowerNumber = borrowerNumber;
  }

  /**
   * @return Returns the finePaymentAdjmtIndicator.
   */
  public char getFinePaymentAdjmtIndicator() {
    return finePaymentAdjmtIndicator;
  }

  /**
   * @param finePaymentAdjmtIndicator The finePaymentAdjmtIndicator to set.
   */
  public void setFinePaymentAdjmtIndicator(char finePaymentAdjmtIndicator) {
    this.finePaymentAdjmtIndicator = finePaymentAdjmtIndicator;
  }

  /**
   * @return Returns the finePaymentAdjmtNote.
   */
  public String getFinePaymentAdjmtNote() {
    return finePaymentAdjmtNote;
  }

  /**
   * @param finePaymentAdjmtNote The finePaymentAdjmtNote to set.
   */
  public void setFinePaymentAdjmtNote(String finePaymentAdjmtNote) {
    this.finePaymentAdjmtNote = finePaymentAdjmtNote;
  }

}
