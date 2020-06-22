package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.BibliographicStandardNoteDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;


public class StandardNoteAccessPoint implements Persistence {
  protected short typeCode = -1;
  private PersistenceState persistenceState = new PersistenceState();
  private int bibItemNumber = -1;
  private String userViewString;
  private int noteNbr = -1;


  public StandardNoteAccessPoint(int bibItemNumber) {
    setBibItemNumber(bibItemNumber);

  }

  public StandardNoteAccessPoint() {

  }

  public short getTypeCode() {
    return typeCode;
  }


  public void setTypeCode(short typeCode) {
    this.typeCode = typeCode;
  }


  public int getBibItemNumber() {
    return bibItemNumber;
  }


  public void setBibItemNumber(int bibItemNumber) {
    this.bibItemNumber = bibItemNumber;
  }


  public String getUserViewString() {
    return userViewString;
  }


  public void setUserViewString(String userViewString) {
    this.userViewString = userViewString;
  }


  public int getNoteNbr() {
    return noteNbr;
  }


  public void setNoteNbr(int noteNbr) {
    this.noteNbr = noteNbr;
  }


  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }



  public AbstractDAO getDAO() {
    return new BibliographicStandardNoteDAO();
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

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + bibItemNumber;
    result = prime * result + noteNbr;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StandardNoteAccessPoint other = (StandardNoteAccessPoint) obj;
    if (bibItemNumber != other.bibItemNumber)
      return false;
    return noteNbr == other.noteNbr;
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


}
