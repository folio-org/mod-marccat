/*
 * (c) LibriCore
 *
 * Created on 22-mar-2005
 *
 * LoanInTransitPolicyKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LoanInTransitPolicyKey implements Serializable {

  private int homeBranchNumber;

  private int foreignBranchNumber;

  /**
   * Class constructor
   */
  public LoanInTransitPolicyKey() {
    super ( );
  }

  public LoanInTransitPolicyKey(int homeBranch, int foreignBranch) {
    this.setHomeBranchNumber (homeBranch);
    this.setForeignBranchNumber (foreignBranch);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof LoanInTransitPolicyKey) {
      LoanInTransitPolicyKey aKey = (LoanInTransitPolicyKey) anObject;
      return (homeBranchNumber == aKey.getHomeBranchNumber ( ) && foreignBranchNumber == aKey
        .getForeignBranchNumber ( ));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return homeBranchNumber + foreignBranchNumber;
  }

  /**
   * @return Returns the foreignBranchNumber.
   * @throws
   * @since 1.0
   */
  public int getForeignBranchNumber() {
    return foreignBranchNumber;
  }

  /**
   * @param foreignBranchNumber The foreignBranchNumber to set.
   * @throws
   * @since 1.0
   */
  public void setForeignBranchNumber(int foreignBranchNumber) {
    this.foreignBranchNumber = foreignBranchNumber;
  }

  /**
   * @return Returns the homeBranchNumber.
   * @throws
   * @since 1.0
   */
  public int getHomeBranchNumber() {
    return homeBranchNumber;
  }

  /**
   * @param homeBranchNumber The homeBranchNumber to set.
   * @throws
   * @since 1.0
   */
  public void setHomeBranchNumber(int homeBranchNumber) {
    this.homeBranchNumber = homeBranchNumber;
  }
}
