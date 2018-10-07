/*
 * (c) LibriCore
 *
 * Created on 28-ene-2005
 *
 * CPY_HLD.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class CPY_HLD implements Serializable {

  private int copyHoldNumber;
  private int borrowerNumber;
  private int copyIdNumber;
  private short priorityNumber;
  private Date copyHoldCreationDate;
  private char copyTimeHoldIndicator;
  private char copyHoldPrintIndicator;
  private int pickupLocationOrganisationNumber;


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
   * @return Returns the copyHoldCreationDate.
   * @throws
   * @since 1.0
   */
  public Date getCopyHoldCreationDate() {
    return copyHoldCreationDate;
  }

  /**
   * @param copyHoldCreationDate The copyHoldCreationDate to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldCreationDate(Date copyHoldCreationDate) {
    this.copyHoldCreationDate = copyHoldCreationDate;
  }

  /**
   * @return Returns the copyHoldNumber.
   * @throws
   * @since 1.0
   */
  public int getCopyHoldNumber() {
    return copyHoldNumber;
  }

  /**
   * @param copyHoldNumber The copyHoldNumber to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldNumber(int copyHoldNumber) {
    this.copyHoldNumber = copyHoldNumber;
  }

  /**
   * @return Returns the copyHoldPrintIndicator.
   * @throws
   * @since 1.0
   */
  public char getCopyHoldPrintIndicator() {
    return copyHoldPrintIndicator;
  }

  /**
   * @param copyHoldPrintIndicator The copyHoldPrintIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldPrintIndicator(char copyHoldPrintIndicator) {
    this.copyHoldPrintIndicator = copyHoldPrintIndicator;
  }

  /**
   * @return Returns the copyIdNumber.
   * @throws
   * @since 1.0
   */
  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  /**
   * @param copyIdNumber The copyIdNumber to set.
   * @throws
   * @since 1.0
   */
  public void setCopyIdNumber(int copyIdNumber) {
    this.copyIdNumber = copyIdNumber;
  }

  /**
   * @return Returns the copyTimeHoldIndicator.
   * @throws
   * @since 1.0
   */
  public char getCopyTimeHoldIndicator() {
    return copyTimeHoldIndicator;
  }

  /**
   * @param copyTimeHoldIndicator The copyTimeHoldIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setCopyTimeHoldIndicator(char copyTimeHoldIndicator) {
    this.copyTimeHoldIndicator = copyTimeHoldIndicator;
  }

  /**
   * @return Returns the priorityNumber.
   * @throws
   * @since 1.0
   */
  public short getPriorityNumber() {
    return priorityNumber;
  }

  /**
   * @param priorityNumber The priorityNumber to set.
   * @throws
   * @since 1.0
   */
  public void setPriorityNumber(short priorityNumber) {
    this.priorityNumber = priorityNumber;
  }

  /**
   * @return Returns the pickupLocationOrganisationNumber.
   * @throws
   * @since 1.0
   */
  public int getPickupLocationOrganisationNumber() {
    return pickupLocationOrganisationNumber;
  }

  /**
   * @param pickupLocationOrganisationNumber The pickupLocationOrganisationNumber to set.
   * @throws
   * @since 1.0
   */
  public void setPickupLocationOrganisationNumber(
    int pickupLocationOrganisationNumber) {
    this.pickupLocationOrganisationNumber = pickupLocationOrganisationNumber;
  }
}
