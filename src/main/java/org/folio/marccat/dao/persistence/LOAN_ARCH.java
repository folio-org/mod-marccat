/*
 * (c) LibriCore
 *
 * Created on 02-nov-2004
 *
 * LOAN_ARCH.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class LOAN_ARCH implements Serializable {
  private int loanArchiveNumber;
  private int organisationNumber;
  private int copyIdNumber;
  private int borrowerNumber;
  private short loanStatisticsTypeCode;
  private Date loanChargeOutDate;
  private Date loanDueDate;
  private Date loanPreviousDueDate;
  private Date loanRecallNotificationDate;
  private Date loanFirstOverdueDate;
  private Date loanSecondOverdueDate;
  private Date loanThirdOverdueDate;
  private int loanRenewalCounterCount;
  private char loanExtendedLoanIndex;
  private Date archiveDate;


  public Date getArchiveDate() {
    return archiveDate;
  }

  public void setArchiveDate(Date archiveDate) {
    this.archiveDate = archiveDate;
  }

  public int getBorrowerNumber() {
    return borrowerNumber;
  }

  public void setBorrowerNumber(int borrowerNumber) {
    this.borrowerNumber = borrowerNumber;
  }

  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  public void setCopyIdNumber(int copyIdNumber) {
    this.copyIdNumber = copyIdNumber;
  }

  public int getLoanArchiveNumber() {
    return loanArchiveNumber;
  }

  public void setLoanArchiveNumber(int loanArchiveNumber) {
    this.loanArchiveNumber = loanArchiveNumber;
  }

  public Date getLoanChargeOutDate() {
    return loanChargeOutDate;
  }

  public void setLoanChargeOutDate(Date loanChargeOutDate) {
    this.loanChargeOutDate = loanChargeOutDate;
  }

  public Date getLoanDueDate() {
    return loanDueDate;
  }

  public void setLoanDueDate(Date loanDueDate) {
    this.loanDueDate = loanDueDate;
  }

  public char getLoanExtendedLoanIndex() {
    return loanExtendedLoanIndex;
  }

  public void setLoanExtendedLoanIndex(char loanExtendedLoanIndex) {
    this.loanExtendedLoanIndex = loanExtendedLoanIndex;
  }

  public Date getLoanFirstOverdueDate() {
    return loanFirstOverdueDate;
  }

  public void setLoanFirstOverdueDate(Date loanFirstOverdueDate) {
    this.loanFirstOverdueDate = loanFirstOverdueDate;
  }

  public Date getLoanPreviousDueDate() {
    return loanPreviousDueDate;
  }

  public void setLoanPreviousDueDate(Date loanPreviousDueDate) {
    this.loanPreviousDueDate = loanPreviousDueDate;
  }

  public Date getLoanRecallNotificationDate() {
    return loanRecallNotificationDate;
  }

  public void setLoanRecallNotificationDate(Date loanRecallNotificationDate) {
    this.loanRecallNotificationDate = loanRecallNotificationDate;
  }

  public int getLoanRenewalCounterCount() {
    return loanRenewalCounterCount;
  }

  public void setLoanRenewalCounterCount(int loanRenewalCounterCount) {
    this.loanRenewalCounterCount = loanRenewalCounterCount;
  }

  public Date getLoanSecondOverdueDate() {
    return loanSecondOverdueDate;
  }

  public void setLoanSecondOverdueDate(Date loanSecondOverdueDate) {
    this.loanSecondOverdueDate = loanSecondOverdueDate;
  }

  public short getLoanStatisticsTypeCode() {
    return loanStatisticsTypeCode;
  }

  public void setLoanStatisticsTypeCode(short loanStatisticsTypeCode) {
    this.loanStatisticsTypeCode = loanStatisticsTypeCode;
  }

  public Date getLoanThirdOverdueDate() {
    return loanThirdOverdueDate;
  }

  public void setLoanThirdOverdueDate(Date loanThirdOverdueDate) {
    this.loanThirdOverdueDate = loanThirdOverdueDate;
  }

  public int getOrganisationNumber() {
    return organisationNumber;
  }

  public void setOrganisationNumber(int organisationNumber) {
    this.organisationNumber = organisationNumber;
  }
}
