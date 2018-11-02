/*
 * (c) LibriCore
 *
 * Created on 16-mar-2005
 *
 * LIB_DTE_CLSE.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LIB_DTE_CLSE implements Serializable {
  private LibraryDateClosedKey key;
  private char generalIndicator;


  /**
   * @return Returns the generalIndicator.
   * @throws
   * @since 1.0
   */
  public char getGeneralIndicator() {
    return generalIndicator;
  }

  /**
   * @param generalIndicator The generalIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setGeneralIndicator(char generalIndicator) {
    this.generalIndicator = generalIndicator;
  }

  /**
   * @return Returns the key.
   * @throws
   * @since 1.0
   */
  public LibraryDateClosedKey getKey() {
    return key;
  }

  /**
   * @param key The key to set.
   * @throws
   * @since 1.0
   */
  public void setKey(LibraryDateClosedKey key) {
    this.key = key;
  }
}
