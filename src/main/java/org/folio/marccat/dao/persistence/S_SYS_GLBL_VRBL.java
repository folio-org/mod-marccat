/*
 * Created on May 6, 2004
 * */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * Hibernate class for table S_SYS_GLBL_VRBL
 *
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2004/07/22 09:30:01 $
 * @since 1.0
 */
public class S_SYS_GLBL_VRBL implements Serializable {
  private String name;
  private String value;
  private String note;

  /**
   * Getter for name
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for name
   *
   * @param string name
   */
  public void setName(String string) {
    name = string;
  }

  /**
   * Getter for stringValue
   *
   * @return stringValue
   */
  public String getValue() {
    return value;
  }

  /**
   * Setter for stringValue
   *
   * @param string stringValue
   */
  public void setValue(String string) {
    value = string;
  }

  /**
   * Getter for note
   *
   * @return note
   */
  public String getNote() {
    return note;
  }

  /**
   * Setter for note
   *
   * @param string note
   */
  public void setNote(String string) {
    note = string;
  }

}
