/*
 * (c) LibriCore
 *
 * Created on 19-nov-2004
 *
 * BRWR_FINE.java
 */

package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class BRWR_FINE implements Serializable {
  private int borrowerFineNumber;
  private int borrowerNumber;
  private int copyIdNumber;
  private Date loanDueDate;
  private Date loanChargeOutDate;
  private Date loanPreviousDueDate;
  private Date loanRecallNotificationDate;
  private Date loanFirstOverdueDate;
  private Date loanSecondOverdueDate;
  private Date loanThirdOverdueDate;
  private Date borrowerFineCreationDate;
  private Date borrowerFineFinalPaymentDate;
  private int loanRenewalCounterCnt;
  private float borrowerFineBlanceDueAmount;
  private float borrowerFineTotalFineAmount;

  public BRWR_FINE() {
    super();
  }

  public BRWR_FINE(int fineNbr, int brwrNbr, int copy, Date due, Date chargeOut,
                   Date fineCreation, int renewal, float blanc, float total) {
    this.borrowerFineNumber = fineNbr;
    this.borrowerNumber = brwrNbr;
    this.copyIdNumber = copy;
    this.loanDueDate = due;
    this.loanChargeOutDate = chargeOut;
    this.borrowerFineCreationDate = fineCreation;
    this.loanRenewalCounterCnt = renewal;
    this.borrowerFineBlanceDueAmount = blanc;
    this.borrowerFineTotalFineAmount = total;
  }

  /**
   * @return Returns the borrowerFineBlanceDueAmount.
   */
  public float getBorrowerFineBlanceDueAmount() {
    return borrowerFineBlanceDueAmount;
  }

  /**
   * @param borrowerFineBlanceDueAmount The borrowerFineBlanceDueAmount to set.
   */
  public void setBorrowerFineBlanceDueAmount(float borrowerFineBlanceDueAmount) {
    this.borrowerFineBlanceDueAmount = borrowerFineBlanceDueAmount;
  }

  /**
   * @return Returns the borrowerFineCreationDate.
   */
  public Date getBorrowerFineCreationDate() {
    return borrowerFineCreationDate;
  }

  /**
   * @param borrowerFineCreationDate The borrowerFineCreationDate to set.
   */
  public void setBorrowerFineCreationDate(Date borrowerFineCreationDate) {
    this.borrowerFineCreationDate = borrowerFineCreationDate;
  }

  /**
   * @return Returns the borrowerFineFinalPaymentDate.
   */
  public Date getBorrowerFineFinalPaymentDate() {
    return borrowerFineFinalPaymentDate;
  }

  /**
   * @param borrowerFineFinalPaymentDate The borrowerFineFinalPaymentDate to set.
   */
  public void setBorrowerFineFinalPaymentDate(
    Date borrowerFineFinalPaymentDate) {
    this.borrowerFineFinalPaymentDate = borrowerFineFinalPaymentDate;
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
   * @return Returns the borrowerFineTotalFineAmount.
   */
  public float getBorrowerFineTotalFineAmount() {
    return borrowerFineTotalFineAmount;
  }

  /**
   * @param borrowerFineTotalFineAmount The borrowerFineTotalFineAmount to set.
   */
  public void setBorrowerFineTotalFineAmount(float borrowerFineTotalFineAmount) {
    this.borrowerFineTotalFineAmount = borrowerFineTotalFineAmount;
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
   * @return Returns the copyIdNumber.
   */
  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  /**
   * @param copyIdNumber The copyIdNumber to set.
   */
  public void setCopyIdNumber(int copyIdNumber) {
    this.copyIdNumber = copyIdNumber;
  }

  /**
   * @return Returns the loanRenewalCounterCnt.
   */
  public int getLoanRenewalCounterCnt() {
    return loanRenewalCounterCnt;
  }

  /**
   * @param loanRenewalCounterCnt The loanRenewalCounterCnt to set.
   */
  public void setLoanRenewalCounterCnt(int loanRenewalCounterCnt) {
    this.loanRenewalCounterCnt = loanRenewalCounterCnt;
  }

  /**
   * @return Returns the loanChargeOutDate.
   */
  public Date getLoanChargeOutDate() {
    return loanChargeOutDate;
  }

  /**
   * @param loanChargeOutDate The loanChargeOutDate to set.
   */
  public void setLoanChargeOutDate(Date loanChargeOutDate) {
    this.loanChargeOutDate = loanChargeOutDate;
  }

  /**
   * @return Returns the loanDueDate.
   */
  public Date getLoanDueDate() {
    return loanDueDate;
  }

  /**
   * @param loanDueDate The loanDueDate to set.
   */
  public void setLoanDueDate(Date loanDueDate) {
    this.loanDueDate = loanDueDate;
  }

  /**
   * @return Returns the loanFirstOverdueDate.
   */
  public Date getLoanFirstOverdueDate() {
    return loanFirstOverdueDate;
  }

  /**
   * @param loanFirstOverdueDate The loanFirstOverdueDate to set.
   */
  public void setLoanFirstOverdueDate(Date loanFirstOverdueDate) {
    this.loanFirstOverdueDate = loanFirstOverdueDate;
  }

  /**
   * @return Returns the loanPreviousDueDate.
   */
  public Date getLoanPreviousDueDate() {
    return loanPreviousDueDate;
  }

  /**
   * @param loanPreviousDueDate The loanPreviousDueDate to set.
   */
  public void setLoanPreviousDueDate(Date loanPreviousDueDate) {
    this.loanPreviousDueDate = loanPreviousDueDate;
  }

  /**
   * @return Returns the loanRecallNotificationDate.
   */
  public Date getLoanRecallNotificationDate() {
    return loanRecallNotificationDate;
  }

  /**
   * @param loanRecallNotificationDate The loanRecallNotificationDate to set.
   */
  public void setLoanRecallNotificationDate(Date loanRecallNotificationDate) {
    this.loanRecallNotificationDate = loanRecallNotificationDate;
  }

  /**
   * @return Returns the loanSecondOverdueDate.
   */
  public Date getLoanSecondOverdueDate() {
    return loanSecondOverdueDate;
  }

  /**
   * @param loanSecondOverdueDate The loanSecondOverdueDate to set.
   */
  public void setLoanSecondOverdueDate(Date loanSecondOverdueDate) {
    this.loanSecondOverdueDate = loanSecondOverdueDate;
  }

  /**
   * @return Returns the loanThirdOverdueDate.
   */
  public Date getLoanThirdOverdueDate() {
    return loanThirdOverdueDate;
  }

  /**
   * @param loanThirdOverdueDate The loanThirdOverdueDate to set.
   */
  public void setLoanThirdOverdueDate(Date loanThirdOverdueDate) {
    this.loanThirdOverdueDate = loanThirdOverdueDate;
  }
}
