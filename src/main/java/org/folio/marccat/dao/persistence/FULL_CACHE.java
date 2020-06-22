package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
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


  public AbstractDAO getDAO() {
    return new DAOFullCache();
  }


  public int getUpdateStatus() {
    return persistentState.getUpdateStatus();
  }


  public void setUpdateStatus(int i) {
    persistentState.setUpdateStatus(i);
  }


  public boolean isChanged() {
    return persistentState.isChanged();
  }


  public boolean isDeleted() {
    return persistentState.isDeleted();
  }


  public boolean isNew() {
    return persistentState.isNew();
  }


  public boolean isRemoved() {
    return persistentState.isRemoved();
  }


  public void markChanged() {
    persistentState.markChanged();
  }


  public void markDeleted() {
    persistentState.markDeleted();
  }


  public void markNew() {
    persistentState.markNew();
  }


  public void markUnchanged() {
    persistentState.markUnchanged();
  }


  public boolean onDelete(Session arg0) throws CallbackException {
    return persistentState.onDelete(arg0);
  }


  public void onLoad(Session arg0, Serializable arg1) {
    persistentState.onLoad(arg0, arg1);
  }


  public boolean onSave(Session arg0) throws CallbackException {
    return persistentState.onSave(arg0);
  }


  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistentState.onUpdate(arg0);
  }



  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() {
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
