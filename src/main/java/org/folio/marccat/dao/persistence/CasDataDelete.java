package org.folio.cataloging.dao.persistence;


import java.io.Serializable;
import java.util.Date;

public class CasDataDelete implements Serializable {


  /**
   * identifier field
   */
  private long bibItmNbr;

  /**
   * nullable persistent field
   */
  private String transactionId;

  /**
   * nullable persistent field
   */
  private String operator;

  /**
   * nullable persistent field
   */
  private Date delDate;
  /**
   * identifier field
   */
  private long recordNbr;

  /**
   * default constructor
   */
  public CasDataDelete() {
  }

  /**
   * minimal constructor
   */
  public CasDataDelete(long bibItmNbr) {
    this.bibItmNbr = bibItmNbr;
  }

  public long getBibItmNbr() {
    return bibItmNbr;
  }

  public void setBibItmNbr(long bibItmNbr) {
    this.bibItmNbr = bibItmNbr;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public Date getDelDate() {
    return delDate;
  }

  public void setDelDate(Date delDate) {
    this.delDate = delDate;
  }

  public long getRecordNbr() {
    return recordNbr;
  }

  public void setRecordNbr(long recordNbr) {
    this.recordNbr = recordNbr;
  }
}

