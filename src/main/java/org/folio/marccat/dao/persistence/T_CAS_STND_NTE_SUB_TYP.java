package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOCodeTable;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;


public class T_CAS_STND_NTE_SUB_TYP implements Persistence {
  private int code;
  private int sequence;
  //-----------------------------
  // Persistence and LifeCycle
  //-----------------------------
  private PersistenceState persistenceState = new PersistenceState();

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  /**
   * Getter for code
   *
   * @return code
   */
  public int getCode() {
    return code;
  }

  /**
   * Setter for code
   *
   * @param s code
   */
  public void setCode(int s) {
    code = s;
  }

  /* (non-Javadoc)
   * @see CodeTable#getCodeString()
   */
  public String getCodeString() {
    return String.valueOf(code);
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + sequence;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    T_CAS_STND_NTE_SUB_TYP other = (T_CAS_STND_NTE_SUB_TYP) obj;
    return sequence == other.sequence;
  }


  public void generateNewKey() throws DataAccessException {
   // Do nothing because it doesn't have a key to generate
  }

  public AbstractDAO getDAO() {
    return new DAOCodeTable();
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
  //-------------------------------
  // END Persistence and LifeCycle
  //-------------------------------

}
