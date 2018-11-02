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
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;

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

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public ORDR_ITM_BIB_ITM() {
    super();
    // TODO Auto-generated constructor stub
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

  /**
   * @since 1.0
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public void evict() throws DataAccessException {
    evict(this);
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
    return this.getOrderNumber() + getOrderItemNumber();
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

  /**
   * @since 1.0
   */
  public int getBibItemNumber() {
    return bibItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getOrderItemNumber() {
    return orderItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setOrderItemNumber(int i) {
    orderItemNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getOrderNumber() {
    return orderNumber;
  }

  /**
   * @since 1.0
   */
  public void setOrderNumber(int i) {
    orderNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getPackageNumber() {
    return packageNumber;
  }

  /**
   * @since 1.0
   */
  public void setPackageNumber(int i) {
    packageNumber = i;
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
