/*
 * (c) LibriCore
 *
 * Created on Aug 26, 2004
 *
 * TitleSecondaryFunction.java
 */
package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOCasTrnsfPrdct;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/08/27 08:04:46 $
 * @since 1.0
 */
public class CasTransfRec implements Persistence {

  static DAOCasTrnsfPrdct dao = new DAOCasTrnsfPrdct();


  private int transactionId;
  private PersistenceState persistenceState = new PersistenceState();


  public CasTransfRec(int transactionId) {
    this();
    setTransactionId(transactionId);

  }

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public CasTransfRec() {
    super();
    // TODO Auto-generated constructor stub
  }

  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
  }

  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

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
    return dao;
  }

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  public void setUpdateStatus(int i) {
    // TODO Auto-generated method stub

  }

  public void generateNewKey() throws DataAccessException {
    // TODO Auto-generated method stub

  }

  public boolean isChanged() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isDeleted() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isNew() {
    // TODO Auto-generated method stub
    return false;
  }

  public void markChanged() {
    // TODO Auto-generated method stub

  }

  public void markDeleted() {
    // TODO Auto-generated method stub

  }

  public void markNew() {
    // TODO Auto-generated method stub

  }

  public void markUnchanged() {
    // TODO Auto-generated method stub

  }

  public boolean onDelete(Session arg0) throws CallbackException {
    // TODO Auto-generated method stub
    return false;
  }


  public void onLoad(Session arg0, Serializable arg1) {
    // TODO Auto-generated method stub

  }


  public boolean onSave(Session arg0) throws CallbackException {
    // TODO Auto-generated method stub
    return false;
  }


  public boolean onUpdate(Session arg0) throws CallbackException {
    // TODO Auto-generated method stub
    return false;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + transactionId;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CasTransfRec other = (CasTransfRec) obj;
    return transactionId == other.getTransactionId();
  }
}
