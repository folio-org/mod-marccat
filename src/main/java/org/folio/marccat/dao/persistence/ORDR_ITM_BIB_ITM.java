/*
 * (c) LibriCore
 *
 * Created on Dec 6, 2004
 *
 * ORDR_ITM_BIB_ITM.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class ORDR_ITM_BIB_ITM implements Persistence {
  private int orderNumber;
  private int orderItemNumber;
  private int packageNumber;
  private int bibItemNumber;
  private PersistenceState persistenceState = new PersistenceState();


  public ORDR_ITM_BIB_ITM() {
    super();

  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof ORDR_ITM_BIB_ITM) {
      ORDR_ITM_BIB_ITM o = (ORDR_ITM_BIB_ITM) arg0;
      return o.getOrderNumber() == getOrderNumber() &&
        o.getOrderItemNumber() == getOrderItemNumber() &&
        o.getPackageNumber() == getPackageNumber();
    } else {
      return false;
    }
  }


  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public void evict() throws DataAccessException {
    evict(this);
  }


  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }


  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }


  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return this.getOrderNumber() + getOrderItemNumber();
  }


  public boolean isChanged() {
    return persistenceState.isChanged();
  }


  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }


  public boolean isNew() {
    return persistenceState.isNew();
  }


  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }


  public void markChanged() {
    persistenceState.markChanged();
  }


  public void markDeleted() {
    persistenceState.markDeleted();
  }


  public void markNew() {
    persistenceState.markNew();
  }


  public void markUnchanged() {
    persistenceState.markUnchanged();
  }


  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }


  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }


  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }


  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
  }


  public int getBibItemNumber() {
    return bibItemNumber;
  }


  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }


  public int getOrderItemNumber() {
    return orderItemNumber;
  }


  public void setOrderItemNumber(int i) {
    orderItemNumber = i;
  }


  public int getOrderNumber() {
    return orderNumber;
  }


  public void setOrderNumber(int i) {
    orderNumber = i;
  }


  public int getPackageNumber() {
    return packageNumber;
  }


  public void setPackageNumber(int i) {
    packageNumber = i;
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // not applicable (yet)

  }

}
