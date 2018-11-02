/*
 * (c) LibriCore
 *
 * Created on 02-nov-2004
 *
 * BRWR.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class BRWR implements Serializable {
  private int borrowerNumber;
  private int personNumber;
  private String borrowerBarcodeNumber;
  private Integer borrowerLocalNumber;
  private short personLocal1stTypeCode;
  private short personLocal2ndTypeCode;
  private short personLocal3rdTypeCode;
  private short personLocal4thTypeCode;
  private short borrowerTypeCode;
  private Date borrowerUpdateTimeStamp;
  private Date borrowerBlacklistedDate;
  private char borrowerDeliveryIndicator;
  private char borrowerBorrowIndicator;
  private char borrowerTrapIndicator;
  private String borrowerSummaryText;
  private int organisationNumber;
  private int branchNumber;
  private Date circulationDate;
  private Date circulationEndDate;


  public BRWR() {
    super();
  }

  /**
   * @return Returns the borrowerBarcodeNumber.
   * @throws
   * @since 1.0
   */
  public String getBorrowerBarcodeNumber() {
    return borrowerBarcodeNumber;
  }

  /**
   * @param borrowerBarcodeNumber The borrowerBarcodeNumber to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerBarcodeNumber(String borrowerBarcodeNumber) {
    this.borrowerBarcodeNumber = borrowerBarcodeNumber;
  }

  /**
   * @return Returns the borrowerBlacklistedDate.
   * @throws
   * @since 1.0
   */
  public Date getBorrowerBlacklistedDate() {
    return borrowerBlacklistedDate;
  }

  /**
   * @param borrowerBlacklistedDate The borrowerBlacklistedDate to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerBlacklistedDate(Date borrowerBlacklistedDate) {
    this.borrowerBlacklistedDate = borrowerBlacklistedDate;
  }

  /**
   * @return Returns the borrowerDeliveryIndicator.
   * @throws
   * @since 1.0
   */
  public char getBorrowerDeliveryIndicator() {
    return borrowerDeliveryIndicator;
  }

  /**
   * @param borrowerDeliveryIndicator The borrowerDeliveryIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerDeliveryIndicator(char borrowerDeliveryIndicator) {
    this.borrowerDeliveryIndicator = borrowerDeliveryIndicator;
  }

  /**
   * @return Returns the borrowerLocalNumber.
   * @throws
   * @since 1.0
   */
  public Integer getBorrowerLocalNumber() {
    return borrowerLocalNumber;
  }

  /**
   * @param borrowerLocalNumber The borrowerLocalNumber to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerLocalNumber(Integer borrowerLocalNumber) {
    this.borrowerLocalNumber = borrowerLocalNumber;
  }

  /**
   * @return Returns the borrowerBorrowIndicator.
   * @throws
   * @since 1.0
   */
  public char getBorrowerBorrowIndicator() {
    return borrowerBorrowIndicator;
  }

  /**
   * @param borrowerBorrowIndicator The borrowerNotBorrowIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerBorrowIndicator(char borrowerBorrowIndicator) {
    this.borrowerBorrowIndicator = borrowerBorrowIndicator;
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

  /**
   * @return Returns the borrowerSummaryText.
   * @throws
   * @since 1.0
   */
  public String getBorrowerSummaryText() {
    return borrowerSummaryText;
  }

  /**
   * @param borrowerSummaryText The borrowerSummaryText to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerSummaryText(String borrowerSummaryText) {
    this.borrowerSummaryText = borrowerSummaryText;
  }

  /**
   * @return Returns the borrowerTrapIndicator.
   * @throws
   * @since 1.0
   */
  public char getBorrowerTrapIndicator() {
    return borrowerTrapIndicator;
  }

  /**
   * @param borrowerTrapIndicator The borrowerTrapIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerTrapIndicator(char borrowerTrapIndicator) {
    this.borrowerTrapIndicator = borrowerTrapIndicator;
  }

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
   * @return Returns the borrowerUpdateTimeStamp.
   * @throws
   * @since 1.0
   */
  public Date getBorrowerUpdateTimeStamp() {
    return borrowerUpdateTimeStamp;
  }

  /**
   * @param borrowerUpdateTimeStamp The borrowerUpdateTimeStamp to set.
   * @throws
   * @since 1.0
   */
  public void setBorrowerUpdateTimeStamp(Date borrowerUpdateTimeStamp) {
    this.borrowerUpdateTimeStamp = borrowerUpdateTimeStamp;
  }

  /**
   * @return Returns the branchNumber.
   * @throws
   * @since 1.0
   */
  public int getBranchNumber() {
    return branchNumber;
  }

  /**
   * @param branchNumber The branchNumber to set.
   * @throws
   * @since 1.0
   */
  public void setBranchNumber(int branchNumber) {
    this.branchNumber = branchNumber;
  }

  /**
   * @return Returns the circulationDate.
   * @throws
   * @since 1.0
   */
  public Date getCirculationDate() {
    return circulationDate;
  }

  /**
   * @param circulationDate The circulationDate to set.
   * @throws
   * @since 1.0
   */
  public void setCirculationDate(Date circulationDate) {
    this.circulationDate = circulationDate;
  }

  /**
   * @return Returns the circulationEndDate.
   * @throws
   * @since 1.0
   */
  public Date getCirculationEndDate() {
    return circulationEndDate;
  }

  /**
   * @param circulationEndDate The circulationEndDate to set.
   * @throws
   * @since 1.0
   */
  public void setCirculationEndDate(Date circulationEndDate) {
    this.circulationEndDate = circulationEndDate;
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
   * @return Returns the personLocal1stTypeCode.
   * @throws
   * @since 1.0
   */
  public short getPersonLocal1stTypeCode() {
    return personLocal1stTypeCode;
  }

  /**
   * @param personLocal1stTypeCode The personLocal1stTypeCode to set.
   * @throws
   * @since 1.0
   */
  public void setPersonLocal1stTypeCode(
    short personLocal1stTypeCode) {
    this.personLocal1stTypeCode = personLocal1stTypeCode;
  }

  /**
   * @return Returns the personLocal2ndTypeCode.
   * @throws
   * @since 1.0
   */
  public short getPersonLocal2ndTypeCode() {
    return personLocal2ndTypeCode;
  }

  /**
   * @param personLocal2ndTypeCode The personLocal2ndTypeCode to set.
   * @throws
   * @since 1.0
   */
  public void setPersonLocal2ndTypeCode(
    short personLocal2ndTypeCode) {
    this.personLocal2ndTypeCode = personLocal2ndTypeCode;
  }

  /**
   * @return Returns the personLocal3rdTypeCode.
   * @throws
   * @since 1.0
   */
  public short getPersonLocal3rdTypeCode() {
    return personLocal3rdTypeCode;
  }

  /**
   * @param personLocal3rdTypeCode The personLocal3rdTypeCode to set.
   * @throws
   * @since 1.0
   */
  public void setPersonLocal3rdTypeCode(
    short personLocal3rdTypeCode) {
    this.personLocal3rdTypeCode = personLocal3rdTypeCode;
  }

  /**
   * @return Returns the personLocal4thTypeCode.
   * @throws
   * @since 1.0
   */
  public short getPersonLocal4thTypeCode() {
    return personLocal4thTypeCode;
  }

  /**
   * @param personLocal4thTypeCode The personLocal4thTypeCode to set.
   * @throws
   * @since 1.0
   */
  public void setPersonLocal4thTypeCode(
    short personLocal4thTypeCode) {
    this.personLocal4thTypeCode = personLocal4thTypeCode;
  }

  /**
   * @return Returns the personNumber.
   * @throws
   * @since 1.0
   */
  public int getPersonNumber() {
    return personNumber;
  }

  /**
   * @param personNumber The personNumber to set.
   * @throws
   * @since 1.0
   */
  public void setPersonNumber(int personNumber) {
    this.personNumber = personNumber;
  }
}
