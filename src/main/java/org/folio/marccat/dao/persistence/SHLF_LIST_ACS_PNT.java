/*
 * (c) LibriCore
 *
 * Created on 21-jun-2004
 *
 * SHLF_LIST_ACS_PNT.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author Usuario
 * @version $Revision: 1.3 $, $Date: 2004/12/02 17:20:52 $
 * @since 1.0
 */
public class SHLF_LIST_ACS_PNT implements Serializable {
  //KEY
  private int shelfListKeyNumber;
  private int bibItemNumber;

  private int mainLibraryNumber;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public SHLF_LIST_ACS_PNT() {
    super();
  }

  public SHLF_LIST_ACS_PNT(
    int bibItemNumber,
    int mainLibraryNumber,
    int shelfListKeyNumber) {
    setBibItemNumber(bibItemNumber);
    setMainLibraryNumber(mainLibraryNumber);
    setShelfListKeyNumber(shelfListKeyNumber);
  }

  /**
   * @return bibItemNumber
   */
  public int getBibItemNumber() {
    return bibItemNumber;
  }

  /**
   * @param i bibItemNumber
   */
  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  /**
   * @return mainLibraryNumber
   */
  public int getMainLibraryNumber() {
    return mainLibraryNumber;
  }

  /**
   * @param i mainLibraryNumber
   */
  public void setMainLibraryNumber(int i) {
    mainLibraryNumber = i;
  }

  /**
   * @return shelfListKeyNumber
   */
  public int getShelfListKeyNumber() {
    return shelfListKeyNumber;
  }

  /**
   * @param i shelfListKeyNumber
   */
  public void setShelfListKeyNumber(int i) {
    shelfListKeyNumber = i;
  }

  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + bibItemNumber;
    result = PRIME * result + shelfListKeyNumber;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final SHLF_LIST_ACS_PNT other = (SHLF_LIST_ACS_PNT) obj;
    if (bibItemNumber != other.bibItemNumber)
      return false;
    return shelfListKeyNumber == other.shelfListKeyNumber;
  }
}
