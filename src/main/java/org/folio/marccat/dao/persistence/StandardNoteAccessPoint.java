package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.BibliographicStandardNoteDAO;

import java.io.Serializable;

/**
 * @author paulm
 * @since 1.0
 */
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


  /**
   * @since 1.0
   */
  public void setPersistenceState(PersistenceState state) {
    persistenceState = state;
  }


  /**
   * @since 1.0
   */
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public void evict() throws DataAccessException {
    evict(this);
  }

  /**
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return new BibliographicStandardNoteDAO();
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

  /**
   * @since 1.0
   */
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
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // not applicable for this class

  }


}
