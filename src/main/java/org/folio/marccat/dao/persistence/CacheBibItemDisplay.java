/*
 * (c) LibriCore
 *
 * Created on 06-nov-2004
 *
 * CacheBibItemDisplay.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * Represents composite key for S_CACHE_BIB_ITM_DSPLY class
 *
 * @author paulm
 */

public class CacheBibItemDisplay implements Serializable {
  private int amicusNumber;
  private int transactionViewNumber;


  /**
   * Class constructor
   */
  public CacheBibItemDisplay() {
    super();
  }

  public CacheBibItemDisplay(int amicusNbr, int transactionViewNbr) {
    this.setAmicusNumber(amicusNbr);
    this.setTransactionViewNumber(transactionViewNbr);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof CacheBibItemDisplay) {
      CacheBibItemDisplay aKey = (CacheBibItemDisplay) anObject;
      return (
        amicusNumber == aKey.getAmicusNumber()
          && transactionViewNumber == aKey.getTransactionViewNumber());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return amicusNumber + transactionViewNumber;
  }


  /**
   * @return Returns the amicusNumber.
   */
  public int getAmicusNumber() {
    return amicusNumber;
  }

  /**
   * @param amicusNumber The amicusNumber to set.
   */
  public void setAmicusNumber(int amicusNumber) {
    this.amicusNumber = amicusNumber;
  }

  /**
   * @return Returns the transactionViewNumber.
   */
  public int getTransactionViewNumber() {
    return transactionViewNumber;
  }

  /**
   * @param transactionViewNumber The transactionViewNumber to set.
   */
  public void setTransactionViewNumber(int transactionViewNumber) {
    this.transactionViewNumber = transactionViewNumber;
  }
}
