package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOCasDigAdmin;

import java.io.Serializable;
import java.util.Date;

public class CasPublCdeIsbn implements Persistence {
  private static final long serialVersionUID = 2522128570785338271L;

  static DAOCasDigAdmin dao = new DAOCasDigAdmin ( );

  private String codEditore;
  //	private String codIsbn;
  private String isbnSortForm;
  private String isbnStringText;
  private String user;
  private Date date;

  private PersistenceState persistenceState = new PersistenceState ( );

  public CasPublCdeIsbn(String editor) {
    this ( );
    setCodEditore (editor);
  }

  public CasPublCdeIsbn() {
    super ( );
  }

  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }

  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict (obj);
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
      + ((codEditore == null) ? 0 : codEditore.hashCode ( ));
    result = prime * result
      + ((isbnSortForm == null) ? 0 : isbnSortForm.hashCode ( ));
    return result;
  }


  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass ( ) != obj.getClass ( ))
      return false;
    CasPublCdeIsbn other = (CasPublCdeIsbn) obj;
    if (codEditore == null) {
      if (other.codEditore != null)
        return false;
    } else if (!codEditore.equals (other.codEditore))
      return false;
    if (isbnSortForm == null) {
      if (other.isbnSortForm != null)
        return false;
    } else if (!isbnSortForm.equals (other.isbnSortForm))
      return false;
    return true;
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

  public String getCodEditore() {
    return codEditore;
  }

  public void setCodEditore(String codEditore) {
    this.codEditore = codEditore;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getIsbnSortForm() {
    return isbnSortForm;
  }

  public void setIsbnSortForm(String isbnSortForm) {
    this.isbnSortForm = isbnSortForm;
  }

  public String getIsbnStringText() {
    return isbnStringText;
  }

  public void setIsbnStringText(String isbnStringText) {
    this.isbnStringText = isbnStringText;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

}
