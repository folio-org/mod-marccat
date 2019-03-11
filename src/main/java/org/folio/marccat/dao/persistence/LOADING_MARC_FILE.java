/*
 * (c) LibriCore
 *
 * Created on Dec 7, 2004
 *
 * LOADING_MARC_FILE.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;


/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class LOADING_MARC_FILE implements Persistence {
  private Integer fileNumber;
  private String fileName;
  private int loadingStatisticsNumber;
  private PersistenceState persistenceState = new PersistenceState();


  public LOADING_MARC_FILE() {
    super();

  }

  public String getFileName() {
    return fileName;
  }


  public void setFileName(String string) {
    fileName = string;
  }


  public Integer getFileNumber() {
    return fileNumber;
  }

  public void setFileNumber(Integer i) {
    fileNumber = i;
  }


  public int getLoadingStatisticsNumber() {
    return loadingStatisticsNumber;
  }


  public void setLoadingStatisticsNumber(int i) {
    loadingStatisticsNumber = i;
  }


  public PersistenceState getPersistenceState() {
    return persistenceState;
  }


  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof LOADING_MARC_FILE) {
      LOADING_MARC_FILE l = (LOADING_MARC_FILE) arg0;
      return l.getFileNumber() == getFileNumber();
    } else {
      return false;
    }
  }


  public void evict(Object obj) {
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
    if (getFileNumber() == null) {
      return 0;
    } else {
      return getFileNumber().intValue();
    }
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
    // keys are assigned from a sequence by Hibernate
  }

}
