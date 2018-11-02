/*
 * (c) LibriCore
 *
 * Created on 28-ene-2005
 *
 * TTL_HLD_DTL.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Elena
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class TTL_HLD_DTL implements Serializable {

  private int titleHoldDetailNumber;
  private int titleHoldNumber;
  private Date titleHoldTimeStartingDate;
  private Date titleHoldTimeEndDate;
  private char titleHoldTimeStatusIndicator;
  private char holdRecallQueueTypeCode;

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
  public void setHoldRecallQueueTypeCode(char holdRecallQueueTypeCode) {
    this.holdRecallQueueTypeCode = holdRecallQueueTypeCode;
  }

  /**
   * @return Returns the titleHoldDetailNumber.
   * @throws
   * @since 1.0
   */
  public int getTitleHoldDetailNumber() {
    return titleHoldDetailNumber;
  }

  /**
   * @param titleHoldDetailNumber The titleHoldDetailNumber to set.
   * @throws
   * @since 1.0
   */
  public void setTitleHoldDetailNumber(int titleHoldDetailNumber) {
    this.titleHoldDetailNumber = titleHoldDetailNumber;
  }

  /**
   * @return Returns the titleHoldNumber.
   * @throws
   * @since 1.0
   */
  public int getTitleHoldNumber() {
    return titleHoldNumber;
  }

  /**
   * @param titleHoldNumber The titleHoldNumber to set.
   * @throws
   * @since 1.0
   */
  public void setTitleHoldNumber(int titleHoldNumber) {
    this.titleHoldNumber = titleHoldNumber;
  }

  /**
   * @return Returns the titleHoldTimeEndDate.
   * @throws
   * @since 1.0
   */
  public Date getTitleHoldTimeEndDate() {
    return titleHoldTimeEndDate;
  }

  /**
   * @param titleHoldTimeEndDate The titleHoldTimeEndDate to set.
   * @throws
   * @since 1.0
   */
  public void setTitleHoldTimeEndDate(Date titleHoldTimeEndDate) {
    this.titleHoldTimeEndDate = titleHoldTimeEndDate;
  }

  /**
   * @return Returns the titleHoldTimeStartingDate.
   * @throws
   * @since 1.0
   */
  public Date getTitleHoldTimeStartingDate() {
    return titleHoldTimeStartingDate;
  }

  /**
   * @param titleHoldTimeStartingDate The titleHoldTimeStartingDate to set.
   * @throws
   * @since 1.0
   */
  public void setTitleHoldTimeStartingDate(Date titleHoldTimeStartingDate) {
    this.titleHoldTimeStartingDate = titleHoldTimeStartingDate;
  }

  /**
   * @return Returns the titleHoldTimeStatusIndicator.
   * @throws
   * @since 1.0
   */
  public char getTitleHoldTimeStatusIndicator() {
    return titleHoldTimeStatusIndicator;
  }

  /**
   * @param titleHoldTimeStatusIndicator The titleHoldTimeStatusIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setTitleHoldTimeStatusIndicator(
    char titleHoldTimeStatusIndicator) {
    this.titleHoldTimeStatusIndicator = titleHoldTimeStatusIndicator;
  }
}
