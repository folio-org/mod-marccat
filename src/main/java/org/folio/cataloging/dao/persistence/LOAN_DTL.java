/*
 * (c) LibriCore
 *
 * Created on 02-nov-2004
 *
 * LOAN_DTL.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class LOAN_DTL implements Serializable {
  private Integer loanDetailNumber;
  private Integer loanNumber;
  private short loanActivityCode;
  private Date activityTimeDate;
  private Date loanDueDate;

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getActivityTimeDate() {
    return activityTimeDate;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setActivityTimeDate(Date date) {
    activityTimeDate = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public short getLoanActivityCode() {
    return loanActivityCode;
  }

  /**
   * @param integer
   * @throws
   * @since 1.0
   */
  public void setLoanActivityCode(short integer) {
    loanActivityCode = integer;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Integer getLoanDetailNumber() {
    return loanDetailNumber;
  }

  /**
   * @param integer
   * @throws
   * @since 1.0
   */
  public void setLoanDetailNumber(Integer integer) {
    loanDetailNumber = integer;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getLoanDueDate() {
    return loanDueDate;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setLoanDueDate(Date date) {
    loanDueDate = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Integer getLoanNumber() {
    return loanNumber;
  }

  /**
   * @param integer
   * @throws
   * @since 1.0
   */
  public void setLoanNumber(Integer integer) {
    loanNumber = integer;
  }

}
