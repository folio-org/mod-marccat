/*
 * (c) LibriCore
 *
 * Created on 02-mar-2005
 *
 * LIB_HOLD_PLCY.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Ruben
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class LIB_HOLD_PLCY implements Serializable {
  private int libraryHoldPolicyNumber;
  private int organisationNumber;
  private char loanPeriodCode;
  private short borrowerTypeCode;
  private int holdActivateDuration;
  private int holdOnShelfBefore;
  private int holdLimits;
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
   * @return Returns the holdActivateDuration.
   * @throws
   * @since 1.0
   */
  public int getHoldActivateDuration() {
    return holdActivateDuration;
  }

  /**
   * @param holdActivateDuration The holdActivateDuration to set.
   * @throws
   * @since 1.0
   */
  public void setHoldActivateDuration(int holdActivateDuration) {
    this.holdActivateDuration = holdActivateDuration;
  }

  /**
   * @return Returns the holdLimits.
   * @throws
   * @since 1.0
   */
  public int getHoldLimits() {
    return holdLimits;
  }

  /**
   * @param holdLimits The holdLimits to set.
   * @throws
   * @since 1.0
   */
  public void setHoldLimits(int holdLimits) {
    this.holdLimits = holdLimits;
  }

  /**
   * @return Returns the holdOnShelfBefore.
   * @throws
   * @since 1.0
   */
  public int getHoldOnShelfBefore() {
    return holdOnShelfBefore;
  }

  /**
   * @param holdOnShelfBefore The holdOnShelfBefore to set.
   * @throws
   * @since 1.0
   */
  public void setHoldOnShelfBefore(int holdOnShelfBefore) {
    this.holdOnShelfBefore = holdOnShelfBefore;
  }

  /**
   * @return Returns the libraryHoldPolicyNumber.
   * @throws
   * @since 1.0
   */
  public int getLibraryHoldPolicyNumber() {
    return libraryHoldPolicyNumber;
  }

  /**
   * @param libraryHoldPolicyNumber The libraryHoldPolicyNumber to set.
   * @throws
   * @since 1.0
   */
  public void setLibraryHoldPolicyNumber(int libraryHoldPolicyNumber) {
    this.libraryHoldPolicyNumber = libraryHoldPolicyNumber;
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
}
