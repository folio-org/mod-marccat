/*
 * (c) LibriCore
 *
 * Created on 17-ago-2004
 *
 * T_008_TYPKey.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2004/08/18 14:39:35 $
 * @since 1.0
 */
public class T_008_TYPKey implements Serializable {

  private char levelCode;
  private char recordTypeCode;

  /**
   * Class constructor
   *
   * @since 1.0
   */

  public T_008_TYPKey() {
    super ( );
  }

  /**
   * Class constructor
   *
   * @since 1.0
   */

  public T_008_TYPKey(char l, char r) {
    this.setLevelCode (l);
    this.setRecordTypeCode (r);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof T_008_TYPKey) {
      T_008_TYPKey aKey = (T_008_TYPKey) anObject;
      return (
        levelCode == aKey.getLevelCode ( )
          && recordTypeCode == aKey.getRecordTypeCode ( ));
    } else {
      return false;
    }
  }

  public int hashCode() {
    return levelCode + recordTypeCode;
  }

  /**
   * GETTER AND SETTERS *
   **/
  public char getLevelCode() {
    return levelCode;
  }

  public void setLevelCode(char c) {
    levelCode = c;
  }

  public char getRecordTypeCode() {
    return recordTypeCode;
  }

  public void setRecordTypeCode(char c) {
    recordTypeCode = c;
  }

}
