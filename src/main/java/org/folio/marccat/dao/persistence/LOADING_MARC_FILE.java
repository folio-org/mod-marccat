/*
 * (c) LibriCore
 *
 * Created on Dec 7, 2004
 *
 * LOADING_MARC_FILE.java
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
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class LOADING_MARC_FILE implements Persistence {
  private Integer fileNumber;
  private String fileName;
  //private byte[] blob;
  private int loadingStatisticsNumber;
  private PersistenceState persistenceState = new PersistenceState();


  public LOADING_MARC_FILE() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   *
   * @since 1.0
   */
	/*public Blob getBlob() {
		return blob;
	}*/

  /**
   * @since 1.0
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @since 1.0
   */
  public void setFileName(String string) {
    fileName = string;
  }

  /**
   * @since 1.0
   */
  public Integer getFileNumber() {
    return fileNumber;
  }

  /**
   *
   * @since 1.0
   */
	/*public void setBlob(Blob blob) {
		this.blob = blob;
	}*/

  /**
   * @since 1.0
   */
  public void setFileNumber(Integer i) {
    fileNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getLoadingStatisticsNumber() {
    return loadingStatisticsNumber;
  }

  /**
   * @since 1.0
   */
  public void setLoadingStatisticsNumber(int i) {
    loadingStatisticsNumber = i;
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
    if (getFileNumber() == null) {
      return 0;
    } else {
      return getFileNumber().intValue();
    }
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
    // keys are assigned from a sequence by Hibernate
  }

}
