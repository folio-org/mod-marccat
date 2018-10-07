/*
 * (c) LibriCore
 *
 * Created on 16-may-2005
 *
 * CIR_INVTRY_LGL.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

/**
 * @author Ruben
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class CIR_INVTRY_LGL implements Serializable {

  private int copyIdNumber;


  /**
   * @return Returns the copyIdNumber.
   * @throws
   * @since 1.0
   */
  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  /**
   * @param copyIdNumber The copyIdNumber to set.
   * @throws
   * @since 1.0
   */
  public void setCopyIdNumber(int copyIdNumber) {
    this.copyIdNumber = copyIdNumber;
  }
}
