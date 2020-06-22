package org.folio.marccat.business.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Lifecycle;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import java.io.Serializable;

/**
 * Class for the persistence state in hibernate.
 */
public class PersistenceState implements Lifecycle, Serializable {

  /**
   * The update status.
   */
  private int updateStatus = UpdateStatus.NEW;

  /**
   * The committed status.
   */
  private Integer committedStatus = null;

  /**
   * Gets the update status.
   *
   * @return the update status
   */
  public int getUpdateStatus() {
    return updateStatus;
  }

  /**
   * Sets the update status.
   *
   * @param i the new update status
   */
  public void setUpdateStatus(int i) {
    updateStatus = i;
  }

  /**
   * On load.
   *
   * @param arg0 the arg 0
   * @param arg1 the arg 1
   */
  public void onLoad(Session arg0, Serializable arg1) {
    setUpdateStatus(UpdateStatus.UNCHANGED);
  }

  /**
   * On delete.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    registerNextStatus(updateStatus, UpdateStatus.REMOVED);
    return false;
  }

  /**
   * On save.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   */

  public boolean onSave(Session arg0) throws CallbackException {
    registerNextStatus(updateStatus, UpdateStatus.UNCHANGED);
    return false;
  }

  /**
   * On update.
   *
   * @param arg0 the arg 0
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return onSave(arg0);
  }

  /**
   * Checks if is changed.
   *
   * @return true, if is changed
   */
  public boolean isChanged() {
    return updateStatus == UpdateStatus.CHANGED;
  }

  /**
   * Checks if is deleted.
   *
   * @return true, if is deleted
   */
  public boolean isDeleted() {
    return updateStatus == UpdateStatus.DELETED;
  }

  /**
   * Checks if is new.
   *
   * @return true, if is new
   */
  public boolean isNew() {
    return updateStatus == UpdateStatus.NEW;
  }

  /**
   * Checks if is removed.
   *
   * @return true, if is removed
   */
  public boolean isRemoved() {
    return updateStatus == UpdateStatus.REMOVED;
  }

  /**
   * If object is now UNCHANGED or REMOVED (set via Hibernate)
   * make it CHANGED (otherwise leave it alone).
   */
  public void markChanged() {
    if (updateStatus == UpdateStatus.UNCHANGED
      || updateStatus == UpdateStatus.REMOVED) {
      setUpdateStatus(UpdateStatus.CHANGED);
    }
  }

  /**
   * Mark new.
   */
  public void markNew() {
    setUpdateStatus(UpdateStatus.NEW);
  }

  /**
   * Mark unchanged.
   */
  public void markUnchanged() {
    setUpdateStatus(UpdateStatus.UNCHANGED);
  }

  /**
   * Mark deleted.
   */
  public void markDeleted() {
    setUpdateStatus(UpdateStatus.DELETED);
  }



  /**
   * Default implementation for Persistence objects.
   *
   * @return the dao
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return new AbstractDAO();
  }

  /**
   * Save next status instead to change it immediately.
   *
   * @param fromStatus previous state (unused in this release)
   * @param toStatus   definitive state (assumed after the commit)
   */
  private void registerNextStatus(int fromStatus, int toStatus) {
    if (fromStatus == toStatus) return;
    committedStatus = toStatus;
    TransactionalHibernateOperation.register(this);
  }

  /**
   * Commit the changes.
   */
  public void confirmChanges() {
    if (committedStatus == null) return;
    updateStatus = committedStatus.intValue();
  }

  /**
   * rollback the changes.
   */
  public void cancelChanges() {
    // do nothing
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return super.toString() + " " + updateStatus + "->" + committedStatus;
  }


}
