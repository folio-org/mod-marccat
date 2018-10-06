/*
 * (c) LibriCore
 *
 * Created on Dec 8, 2004
 *
 * T_AMICUS_FIXED.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class T_AMICUS_FIXED implements Serializable {
  private int codeTableNumber;
  private int numberCode;
  private String stringCode;
  private int sequence;
  private int translationKey;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public T_AMICUS_FIXED() {
    super ( );
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public int getCodeTableNumber() {
    return codeTableNumber;
  }

  /**
   * @since 1.0
   */
  public void setCodeTableNumber(int i) {
    codeTableNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getNumberCode() {
    return numberCode;
  }

  /**
   * @since 1.0
   */
  public void setNumberCode(int i) {
    numberCode = i;
  }

  /**
   * @since 1.0
   */
  public int getSequence() {
    return sequence;
  }

  /**
   * @since 1.0
   */
  public void setSequence(int i) {
    sequence = i;
  }

  /**
   * @since 1.0
   */
  public String getStringCode() {
    return stringCode;
  }

  /**
   * @since 1.0
   */
  public void setStringCode(String string) {
    stringCode = string;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof T_AMICUS_FIXED) {
      T_AMICUS_FIXED t = (T_AMICUS_FIXED) arg0;
      return t.getCodeTableNumber ( ) == this.getCodeTableNumber ( )
        && t.getSequence ( ) == this.getSequence ( );
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getCodeTableNumber ( ) + getSequence ( );
  }

  /**
   * @since 1.0
   */
  public int getTranslationKey() {
    return translationKey;
  }

  /**
   * @since 1.0
   */
  public void setTranslationKey(int i) {
    translationKey = i;
  }

}
