/*
 * (c) LibriCore
 *
 * Created on 01-Dec-2004
 *
 */
package org.folio.cataloging.dao.persistence;

public class OrderNames {

  private int valueCode;
  private String tagNumber;

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getTagNumber() {
    return tagNumber;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setTagNumber(String string) {
    tagNumber = string;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getValueCode() {
    return valueCode;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setValueCode(int i) {
    valueCode = i;
  }

}
