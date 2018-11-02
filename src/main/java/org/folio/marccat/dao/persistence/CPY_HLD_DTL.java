/*
 * (c) LibriCore
 *
 * Created on 28-ene-2005
 *
 * CPY_HLD_DTL.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Elena
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class CPY_HLD_DTL implements Serializable {

  private int copyHoldDetailNumber;
  private int copyHoldNumber;
  private Date copyHoldOnShelfDate;
  private Date copyHoldTimeStartingDate;
  private Date copyHoldTimeEndDate;
  private char copyHoldTimeStatusIndicator;
  private char holdRecallQueueTypeCode;


  /**
   * @return Returns the copyHoldDetailNumber.
   * @throws
   * @since 1.0
   */
  public int getCopyHoldDetailNumber() {
    return copyHoldDetailNumber;
  }

  /**
   * @param copyHoldDetailNumber The copyHoldDetailNumber to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldDetailNumber(int copyHoldDetailNumber) {
    this.copyHoldDetailNumber = copyHoldDetailNumber;
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
   * @return Returns the copyHoldOnShelfDate.
   * @throws
   * @since 1.0
   */
  public Date getCopyHoldOnShelfDate() {
    return copyHoldOnShelfDate;
  }

  /**
   * @param copyHoldOnShelfDate The copyHoldOnShelfDate to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldOnShelfDate(Date copyHoldOnShelfDate) {
    this.copyHoldOnShelfDate = copyHoldOnShelfDate;
  }

  /**
   * @return Returns the copyHoldTimeEndDate.
   * @throws
   * @since 1.0
   */
  public Date getCopyHoldTimeEndDate() {
    return copyHoldTimeEndDate;
  }

  /**
   * @param copyHoldTimeEndDate The copyHoldTimeEndDate to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldTimeEndDate(Date copyHoldTimeEndDate) {
    this.copyHoldTimeEndDate = copyHoldTimeEndDate;
  }

  /**
   * @return Returns the copyHoldTimeStartingDate.
   * @throws
   * @since 1.0
   */
  public Date getCopyHoldTimeStartingDate() {
    return copyHoldTimeStartingDate;
  }

  /**
   * @param copyHoldTimeStartingDate The copyHoldTimeStartingDate to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldTimeStartingDate(Date copyHoldTimeStartingDate) {
    this.copyHoldTimeStartingDate = copyHoldTimeStartingDate;
  }

  /**
   * @return Returns the copyHoldTimeStatusIndicator.
   * @throws
   * @since 1.0
   */
  public char getCopyHoldTimeStatusIndicator() {
    return copyHoldTimeStatusIndicator;
  }

  /**
   * @param copyHoldTimeStatusIndicator The copyHoldTimeStatusIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setCopyHoldTimeStatusIndicator(char copyHoldTimeStatusIndicator) {
    this.copyHoldTimeStatusIndicator = copyHoldTimeStatusIndicator;
  }

  /**
   * @return Returns the holdRecallQueueTypeCode.
   * @throws
   * @since 1.0
   */
  public char getHoldRecallQueueTypeCode() {
    return holdRecallQueueTypeCode;
  }

  /**
   * @param holdRecallQueueTypeCode The holdRecallQueueTypeCode to set.
   * @throws
   * @since 1.0
   */
  public void setHoldRecallQueueTypeCode(
    char holdRecallQueueTypeCode) {
    this.holdRecallQueueTypeCode = holdRecallQueueTypeCode;
  }
}
