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
public class LOADING_MARC_RECORDS implements Serializable, Persistence {
  private int sequence;
  private int loadingStatisticsNumber;
  private int oldBibItemNumber;
  private int BibItemNumber;
  private PersistenceState persistenceState = new PersistenceState();


  public LOADING_MARC_RECORDS() {
    super();
    // TODO Auto-generated constructor stub
  }

  public void generateNewKey() throws DataAccessException {
    // key is stats number + sequence -- managed by app
  }

  /**
   * @since 1.0
   */
  public int getBibItemNumber() {
    return BibItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setBibItemNumber(int i) {
    BibItemNumber = i;
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
  public int getOldBibItemNumber() {
    return oldBibItemNumber;
  }

  /**
   * @since 1.0
   */
  public void setOldBibItemNumber(int i) {
    oldBibItemNumber = i;
  }

  /**
   * @since 1.0
   */
  public int getSequence() {
    return sequence;
  }

  /**
   * @since 1.0
   */
  public void setSequence(int i) {
    sequence = i;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof LOADING_MARC_RECORDS) {
      LOADING_MARC_RECORDS l = (LOADING_MARC_RECORDS) arg0;
      return l.getLoadingStatisticsNumber()
        == this.getLoadingStatisticsNumber()
        && l.getSequence() == this.getSequence();
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return this.getLoadingStatisticsNumber() + this.getSequence();
  }

  public void cancelChanges() {
    persistenceState.cancelChanges();
  }

  public void confirmChanges() {
    persistenceState.confirmChanges();
  }

  @Deprecated
  public void evict(Object obj) throws DataAccessException {
    persistenceState.evict(obj);
  }

  public AbstractDAO getDAO() {
    return persistenceState.getDAO();
  }

  @Override
  @Deprecated
  public void evict() throws DataAccessException {
    evict(this);
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

  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistenceState.onUpdate(arg0);
  }

  public String toString() {
    return persistenceState.toString();
  }

  @Override
  public boolean onSave(Session session) throws CallbackException {
    return false;
  }
}
