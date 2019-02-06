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
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public int getCopyNumber() {
    return copyNumber;
  }

  /**
   * @since 1.0
   */
  public void setCopyNumber(int i) {
    copyNumber = i;
  }

  /**
   * @since 1.0
   */
  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  /**
   * @since 1.0
   */
  public int getReceiveItemNumber() {
    return receiveItemNumber;
  }

  /**
   * @since 1.0
   */
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

  /**
   * @since 1.0
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  /**
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * @since 1.0
   */
  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getCopyNumber() + getReceiveItemNumber();
  }

  /**
   * @since 1.0
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * @since 1.0
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * @since 1.0
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * @since 1.0
   */
  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  /**
   * @since 1.0
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * @since 1.0
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * @since 1.0
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * @since 1.0
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * @since 1.0
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete(arg0);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad(arg0, arg1);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave(arg0);
  }

  /**
   * @since 1.0
   */
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
