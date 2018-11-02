package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOFullCache;

import java.io.Serializable;

/**
 * 2018 Paul Search Engine Java
 *
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2017/02/25 14:07:37 $
 * @since 1.0
 */
public class FULL_CACHE implements Persistence {
  private int id;
  private long itemNumber;
  private long userView;
  private String recordData;
  private char dirty;
  private PersistenceState persistentState = new PersistenceState();

  /**
   * Default constructor
   */
  public FULL_CACHE() {
  }

  /**
   * Convenience constructor
   *
   * @param itemNumber
   * @param userView
   */
  public FULL_CACHE(int itemNumber, int userView) {
    this.itemNumber = itemNumber;
    this.userView = userView;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getItemNumber() {
    return itemNumber;
  }

  public void setItemNumber(long itemNumber) {
    this.itemNumber = itemNumber;
  }

  public long getUserView() {
    return userView;
  }

  public void setUserView(long userView) {
    this.userView = userView;
  }

  public String getRecordData() {
    return recordData;
  }

  public void setRecordData(String recordData) {
    this.recordData = recordData;
  }

  public PersistenceState getPersistentState() {
    return persistentState;
  }

  public void setPersistentState(PersistenceState persistentState) {
    this.persistentState = persistentState;
  }

  /**
   * @since 1.0
   */
  @Deprecated
  public void evict(Object obj) throws DataAccessException {
    persistentState.evict(obj);
  }

  /**
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return new DAOFullCache();
  }

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistentState.getUpdateStatus();
  }

  /**
   * @since 1.0
   */
  public void setUpdateStatus(int i) {
    persistentState.setUpdateStatus(i);
  }

  /**
   * @since 1.0
   */
  public boolean isChanged() {
    return persistentState.isChanged();
  }

  /**
   * @since 1.0
   */
  public boolean isDeleted() {
    return persistentState.isDeleted();
  }

  /**
   * @since 1.0
   */
  public boolean isNew() {
    return persistentState.isNew();
  }

  /**
   * @since 1.0
   */
  public boolean isRemoved() {
    return persistentState.isRemoved();
  }

  /**
   * @since 1.0
   */
  public void markChanged() {
    persistentState.markChanged();
  }

  /**
   * @since 1.0
   */
  public void markDeleted() {
    persistentState.markDeleted();
  }

  /**
   * @since 1.0
   */
  public void markNew() {
    persistentState.markNew();
  }

  /**
   * @since 1.0
   */
  public void markUnchanged() {
    persistentState.markUnchanged();
  }

  /**
   * @since 1.0
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistentState.onDelete(arg0);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistentState.onLoad(arg0, arg1);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistentState.onSave(arg0);
  }

  /**
   * @since 1.0
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistentState.onUpdate(arg0);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#evict()
   */
  @Deprecated
  public void evict() throws DataAccessException {
    evict(this);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // not applicable for this class
  }

  @Override
  public int hashCode() {
    return (int) getItemNumber();
  }


  @Override
  public boolean equals(Object arg0) {
    if (arg0 instanceof FULL_CACHE) {
      FULL_CACHE c = (FULL_CACHE) arg0;
      return this.getItemNumber() == c.getItemNumber() &&
        this.getUserView() == c.getUserView();
    } else {
      return false;
    }
  }


  public char getDirty() {
    return dirty;
  }

  public void setDirty(char dirty) {
    this.dirty = dirty;
  }

  @Override
  public String toString() {
    return "FULL_CACHE(" + getItemNumber() + ", " + getUserView() + ")";
  }

}
