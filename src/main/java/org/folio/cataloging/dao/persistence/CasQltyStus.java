package org.folio.cataloging.dao.persistence;


import java.io.Serializable;

public class CasQltyStus implements Serializable {
  private static final long serialVersionUID = 2299675142382967471L;

  private int transactionId;

  private String operator;

  private String status;

  private String description;

  /**
   * default constructor
   */
  public CasQltyStus() {
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
  }
}
