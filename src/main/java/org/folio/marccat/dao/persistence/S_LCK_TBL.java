package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

public class S_LCK_TBL implements Persistence, Serializable {
  static final long serialVersionUID = 1;

  private PersistenceState persistenceState = new PersistenceState();

  private int tableKey;
  private String tableType;
  private String dbSession;
  private String userName;

  public S_LCK_TBL() {
  }

  public S_LCK_TBL(int key, String type) {
    setTableKey(key);
    setTableType(type);
  }

  public boolean equals(Object arg0) {
    if (arg0 != null && (arg0.getClass() == S_LCK_TBL.class)) {
      S_LCK_TBL arg = (S_LCK_TBL) arg0;
      return arg.getTableKey() == this.getTableKey()
        && arg.getTableType() == this.getTableType();
    } else {
      return false;
    }
  }



  public void generateNewKey() {
    // Do nothing because it doesn't have a key to generate
  }

  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  public String getDbSession() {
    return dbSession;
  }

  public void setDbSession(String dbSession) {
    this.dbSession = dbSession;
  }

  public int getTableKey() {
    return tableKey;
  }

  public void setTableKey(int tableKey) {
    this.tableKey = tableKey;
  }

  public String getTableType() {
    return tableType;
  }

  public void setTableType(String tableType) {
    this.tableType = tableType;
  }

  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int hashCode() {
    return getTableKey();
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

  public String toString() {
    return persistenceState.toString();
  }

}
