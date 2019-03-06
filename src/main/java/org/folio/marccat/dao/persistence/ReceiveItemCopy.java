/*
 * (c) LibriCore
 *
 * Created on Jan 25, 2005
 *
 * ReceiveItemCopy.java
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
public class ReceiveItemCopy implements Persistence {
  private final PersistenceState persistenceState = new PersistenceState();
  private int receiveItemNumber;
  private int copyNumber;


  public ReceiveItemCopy() {
    super();

  }


  public int getCopyNumber() {
    return copyNumber;
  }


  public void setCopyNumber(int i) {
    copyNumber = i;
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public int getReceiveItemNumber() {
    return receiveItemNumber;
  }


  public void setReceiveItemNumber(int i) {
    receiveItemNumber = i;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof ReceiveItemCopy) {
      ReceiveItemCopy r = (ReceiveItemCopy) arg0;
      return r.getReceiveItemNumber() == this.getReceiveItemNumber()
        && r.getCopyNumber() == this.getCopyNumber();
    } else {
      return false;
    }
  }


  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
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
    return getCopyNumber() + getReceiveItemNumber();
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

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#evict()
   */
  public void evict() throws DataAccessException {
    evict(this);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // TODO Auto-generated method stub

  }

}
