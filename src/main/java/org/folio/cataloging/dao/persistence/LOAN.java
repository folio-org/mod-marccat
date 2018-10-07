/*
 * (c) LibriCore
 *
 * Created on 02-nov-2004
 *
 * LOAN.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class LOAN implements Serializable {
  private int loanNumber;
  private int borrowerNumber;
  private int copyIdNumber;
  private short loanStatisticsTypeCode;

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
   * @return Returns the loanNumber.
   */
  public int getLoanNumber() {
    return loanNumber;
  }

  /**
   * @param loanNumber The loanNumber to set.
   */
  public void setLoanNumber(int loanNumber) {
    this.loanNumber = loanNumber;
  }

  /**
   * @return Returns the loanStatisticsTypeCode.
   */
  public short getLoanStatisticsTypeCode() {
    return loanStatisticsTypeCode;
  }

  /**
   * @param loanStatisticsTypeCode The loanStatisticsTypeCode to set.
   */
  public void setLoanStatisticsTypeCode(short loanStatisticsTypeCode) {
    this.loanStatisticsTypeCode = loanStatisticsTypeCode;
  }
}
