package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOSubjectTerm;

import java.io.Serializable;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2004/08/12 14:31:56 $
 * @since 1.0
 */
public class SubjectTerm implements Persistence {

  static DAOSubjectTerm dao = new DAOSubjectTerm();
  private int headingNumber;
  private int codeTerm;
  private PersistenceState persistenceState = new PersistenceState();

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public SubjectTerm() {
    super();
    // TODO Auto-generated constructor stub
  }

  public int getCodeTerm() {
    return codeTerm;
  }

  public void setCodeTerm(int codeTerm) {
    this.codeTerm = codeTerm;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + codeTerm;
    result = prime * result + headingNumber;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SubjectTerm other = (SubjectTerm) obj;
    if (codeTerm != other.codeTerm)
      return false;
    return headingNumber == other.headingNumber;
  }

  /**
   * @return the headingNumber
   */
  public int getHeadingNumber() {
    return headingNumber;
  }

  /**
   * @param headingNumber the headingNumber to set
   */
  public void setHeadingNumber(int headingNumber) {
    this.headingNumber = headingNumber;
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
    return dao;
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
