/*
 * (c) LibriCore
 *
 * Created on Dec 8, 2004
 *
 * T_TRLTN.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class T_TRLTN implements Serializable {
  private long stringNumber;
  private int languageNumber;
  private String text;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public T_TRLTN() {
    super ( );
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public int getLanguageNumber() {
    return languageNumber;
  }

  /**
   * @since 1.0
   */
  public void setLanguageNumber(int i) {
    languageNumber = i;
  }

  /**
   * @since 1.0
   */
  public long getStringNumber() {
    return stringNumber;
  }

  /**
   * @since 1.0
   */
  public void setStringNumber(int i) {
    stringNumber = i;
  }

  /**
   * @since 1.0
   */
  public String getText() {
    return text;
  }

  /**
   * @since 1.0
   */
  public void setText(String string) {
    text = string;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof T_TRLTN) {
      T_TRLTN t = (T_TRLTN) arg0;
      return t.getStringNumber ( ) == this.getStringNumber ( ) &&
        t.getLanguageNumber ( ) == this.getLanguageNumber ( );
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return new Long (getStringNumber ( )).intValue ( ) + getLanguageNumber ( );
  }

}
