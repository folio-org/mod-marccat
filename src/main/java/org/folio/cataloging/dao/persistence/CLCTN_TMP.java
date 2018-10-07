package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOCollectionMaster;

import java.io.Serializable;

public class CLCTN_TMP implements Persistence {
  private static final long serialVersionUID = 2522128570785338271L;
  static DAOCollectionMaster dao = new DAOCollectionMaster ( );
  private PersistenceState persistenceState = new PersistenceState ( );

  private Integer type;
  private String id;

  public CLCTN_TMP() {
    super ( );
  }

  public CLCTN_TMP(Integer type, String id) {
    super ( );
    this.type = type;
    this.id = id;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String toString() {
    return "CLCTN_TMP --> Id = " + id + " Type = " + type;
  }

  public PersistenceState getPersistenceState() {
    return persistenceState;
  }

  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict (obj);
  }

  public void evict() throws DataAccessException {
    evict (this);
  }

  public AbstractDAO getDAO() {
    return dao;
  }

  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus ( );
  }

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus (i);
  }

  public boolean isChanged() {
    return persistenceState.isChanged ( );
  }

  public boolean isDeleted() {
    return persistenceState.isDeleted ( );
  }

  public boolean isNew() {
    return persistenceState.isNew ( );
  }

  public boolean isRemoved() {
    return persistenceState.isRemoved ( );
  }

  public void markChanged() {
    persistenceState.markChanged ( );
  }

  public void markDeleted() {
    persistenceState.markDeleted ( );
  }

  public void markNew() {
    persistenceState.markNew ( );
  }

  public void markUnchanged() {
    persistenceState.markUnchanged ( );
  }

  public boolean onDelete(Session arg0) throws CallbackException {
    return persistenceState.onDelete (arg0);
  }

  public void onLoad(Session arg0, Serializable arg1) {
    persistenceState.onLoad (arg0, arg1);
  }

  public boolean onSave(Session arg0) throws CallbackException {
    return persistenceState.onSave (arg0);
  }

  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate (arg0);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // not applicable for this class
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode ( ));
    result = prime * result + ((type == null) ? 0 : type.hashCode ( ));
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass ( ) != obj.getClass ( ))
      return false;
    CLCTN_TMP other = (CLCTN_TMP) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals (other.id))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals (other.type))
      return false;
    return true;
  }

}
