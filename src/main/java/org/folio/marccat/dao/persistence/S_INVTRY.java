/*
 * (c) LibriCore
 *
 * Created on Feb 1, 2005
 *
 * S_INVTRY.java
 */
package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class S_INVTRY {
  private int mainLibrary;
  private int nextNumber;


  public S_INVTRY() {
    super();
    // TODO Auto-generated constructor stub
  }


  public int getMainLibrary() {
    return mainLibrary;
  }


  public void setMainLibrary(int i) {
    mainLibrary = i;
  }


  public int getNextNumber() {
    return nextNumber;
  }


  public void setNextNumber(int i) {
    nextNumber = i;
  }

}
