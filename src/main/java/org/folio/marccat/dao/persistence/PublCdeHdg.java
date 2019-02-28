package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOPublisher;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

public class PublCdeHdg implements Persistence {

  private static final long serialVersionUID = -4706609571988909462L;
  static DAOPublisher dao = new DAOPublisher();
  private PersistenceState persistenceState = new PersistenceState();
  private String publisherCode;
  private String hdrNumber;

  public PublCdeHdg() {
    super();
  }

  public PublCdeHdg(String codEdi, String hdgNbr) {
    setPublisherCode(codEdi);
    setHdrNumber(hdgNbr);
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
      + ((hdrNumber == null) ? 0 : hdrNumber.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final PublCdeHdg other = (PublCdeHdg) obj;
    if (hdrNumber == null) {
      return other.hdrNumber == null;
    } else return hdrNumber.equals(other.hdrNumber);
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
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


  public AbstractDAO getDAO() {
    return dao;
  }


  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }


  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
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
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // not applicable for this class

  }


  public String getPublisherCode() {
    return publisherCode;
  }


  public void setPublisherCode(String publisherCode) {
    this.publisherCode = publisherCode;
  }


  public String getHdrNumber() {
    return hdrNumber;
  }


  public void setHdrNumber(String hdrNumber) {
    this.hdrNumber = hdrNumber;
  }

}
