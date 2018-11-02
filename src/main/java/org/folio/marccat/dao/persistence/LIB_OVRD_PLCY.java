/*
 * (c) LibriCore
 *
 * Created on 01-mar-2005
 *
 * LIB_OVRD_PLCY.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author Ruben
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class LIB_OVRD_PLCY implements Serializable {
  private int libraryOverduePolicyNumber;
  private int organisationNumber;
  private char loanPeriodCode;
  private short borrowerTypeCode;
  private int overdueFirstNoticeTime;
  private int overdueSecondNoticeTime;
  private int overdueThirdNoticeTime;
  private int overdueBlackList;
  private char generalIndicator;


  /**
   * @return Returns the borrowerTypeCode.
   * @throws
   * @since 1.0
   */
  public short getBorrowerTypeCode() {
    return borrowerTypeCode;
  }

  /**
   * @param borrowerTypeCode The borrowerTypeCode to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerTypeCode(short borrowerTypeCode) {
    this.borrowerTypeCode = borrowerTypeCode;
  }

  /**
   * @return Returns the generalIndicator.
   * @throws
   * @since 1.0
   */
  public char getGeneralIndicator() {
    return generalIndicator;
  }

  /**
   * @param generalIndicator The generalIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setGeneralIndicator(char generalIndicator) {
    this.generalIndicator = generalIndicator;
  }

  /**
   * @return Returns the libraryOverduePolicyNumber.
   * @throws
   * @since 1.0
   */
  public int getLibraryOverduePolicyNumber() {
    return libraryOverduePolicyNumber;
  }

  /**
   * @param libraryOverduePolicyNumber The libraryOverduePolicyNumber to set.
   * @throws
   * @since 1.0
   */
  public void setLibraryOverduePolicyNumber(int libraryOverduePolicyNumber) {
    this.libraryOverduePolicyNumber = libraryOverduePolicyNumber;
  }

  /**
   * @return Returns the loanPeriodCode.
   * @throws
   * @since 1.0
   */
  public char getLoanPeriodCode() {
    return loanPeriodCode;
  }

  /**
   * @param loanPeriodCode The loanPeriodCode to set.
   * @throws
   * @since 1.0
   */
  public void setLoanPeriodCode(char loanPeriodCode) {
    this.loanPeriodCode = loanPeriodCode;
  }

  /**
   * @return Returns the organisationNumber.
   * @throws
   * @since 1.0
   */
  public int getOrganisationNumber() {
    return organisationNumber;
  }

  /**
   * @param organisationNumber The organisationNumber to set.
   * @throws
   * @since 1.0
   */
  public void setOrganisationNumber(int organisationNumber) {
    this.organisationNumber = organisationNumber;
  }

  /**
   * @return Returns the overdueBlackList.
   * @throws
   * @since 1.0
   */
  public int getOverdueBlackList() {
    return overdueBlackList;
  }

  /**
   * @param overdueBlackList The overdueBlackList to set.
   * @throws
   * @since 1.0
   */
  public void setOverdueBlackList(int overdueBlackList) {
    this.overdueBlackList = overdueBlackList;
  }

  /**
   * @return Returns the overdueFirstNoticeTime.
   * @throws
   * @since 1.0
   */
  public int getOverdueFirstNoticeTime() {
    return overdueFirstNoticeTime;
  }

  /**
   * @param overdueFirstNoticeTime The overdueFirstNoticeTime to set.
   * @throws
   * @since 1.0
   */
  public void setOverdueFirstNoticeTime(int overdueFirstNoticeTime) {
    this.overdueFirstNoticeTime = overdueFirstNoticeTime;
  }

  /**
   * @return Returns the overdueSecondNoticeTime.
   * @throws
   * @since 1.0
   */
  public int getOverdueSecondNoticeTime() {
    return overdueSecondNoticeTime;
  }

  /**
   * @param overdueSecondNoticeTime The overdueSecondNoticeTime to set.
   * @throws
   * @since 1.0
   */
  public void setOverdueSecondNoticeTime(int overdueSecondNoticeTime) {
    this.overdueSecondNoticeTime = overdueSecondNoticeTime;
  }

  /**
   * @return Returns the overdueThirdNoticeTime.
   * @throws
   * @since 1.0
   */
  public int getOverdueThirdNoticeTime() {
    return overdueThirdNoticeTime;
  }

  /**
   * @param overdueThirdNoticeTime The overdueThirdNoticeTime to set.
   * @throws
   * @since 1.0
   */
  public void setOverdueThirdNoticeTime(int overdueThirdNoticeTime) {
    this.overdueThirdNoticeTime = overdueThirdNoticeTime;
  }
}
