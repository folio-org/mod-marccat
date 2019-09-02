
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class HoldingRecallQueueKey implements Serializable {
  private int bibItemNumber;
  private int personNumber;
  private int organisationNumber;

  /**
   * Class constructor
   */
  public HoldingRecallQueueKey() {
    super();
  }

  public HoldingRecallQueueKey(int bibItemNumber, int personNumber, int organisationNumber) {
    this.setBibItemNumber(bibItemNumber);
    this.setPersonNumber(personNumber);
    this.setOrganisationNumber(organisationNumber);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof HoldingRecallQueueKey) {
      HoldingRecallQueueKey aKey = (HoldingRecallQueueKey) anObject;
      return (
        bibItemNumber == aKey.getBibItemNumber()
          && personNumber == aKey.getPersonNumber()
          && organisationNumber == aKey.getOrganisationNumber());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return bibItemNumber + personNumber + organisationNumber;
  }


  /**
   * @return Returns the bibItemNumber.
   * @throws
   * @since 1.0
   */
  public int getBibItemNumber() {
    return bibItemNumber;
  }

  /**
   * @param bibItemNumber The bibItemNumber to set.
   * @throws
   * @since 1.0
   */
  public void setBibItemNumber(int bibItemNumber) {
    this.bibItemNumber = bibItemNumber;
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
