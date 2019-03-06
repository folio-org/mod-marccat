/*
 * (c) LibriCore
 *
 * Created on Dec 8, 2004
 *
 * T_AMICUS_FIXED.java
 */
package org.folio.marccat.dao.persistence;

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


  public T_AMICUS_FIXED() {
    super();

  }


  public int getCodeTableNumber() {
    return codeTableNumber;
  }


  public void setCodeTableNumber(int i) {
    codeTableNumber = i;
  }


  public int getNumberCode() {
    return numberCode;
  }


  public void setNumberCode(int i) {
    numberCode = i;
  }


  public int getSequence() {
    return sequence;
  }


  public void setSequence(int i) {
    sequence = i;
  }


  public String getStringCode() {
    return stringCode;
  }


  public void setStringCode(String string) {
    stringCode = string;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof T_AMICUS_FIXED) {
      T_AMICUS_FIXED t = (T_AMICUS_FIXED) arg0;
      return t.getCodeTableNumber() == this.getCodeTableNumber()
        && t.getSequence() == this.getSequence();
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getCodeTableNumber() + getSequence();
  }


  public int getTranslationKey() {
    return translationKey;
  }


  public void setTranslationKey(int i) {
    translationKey = i;
  }

}
