package org.folio.marccat.business.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;

/**
 * The helper for the view.
 *
 * @author paulm
 * @author carment
 */
public class UserViewHelper implements PersistentObjectWithView, Serializable {

  /** The persistence state. */
  private final PersistenceState persistenceState = new PersistenceState();

  /** The user view string. */
  String userViewString = "0000000000000000";

  /**
   * Gets the user view string.
   *
   * @return the user view string
   * @since 1.0
   */
  public String getUserViewString() {
    return userViewString;
  }

  /**
   * Sets the user view string.
   *
   * @param string the new user view string
   */
  public void setUserViewString(String string) {
    userViewString = string;
  }

  /**
   * Gets the update status.
   *
   * @return the update status
   */
  public int getUpdateStatus() {
    return persistenceState.getUpdateStatus();
  }

  /**
   * Sets the update status.
   *
   * @param i the new update status
   */
  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
  }

  /**
   * Checks if is changed.
   *
   * @return true, if is changed
   */
  public boolean isChanged() {
    return persistenceState.isChanged();
  }

  /**
   * Checks if is deleted.
   *
   * @return true, if is deleted
   */
  public boolean isDeleted() {
    return persistenceState.isDeleted();
  }

  /**
   * Checks if is new.
   *
   * @return true, if is new
   */
  public boolean isNew() {
    return persistenceState.isNew();
  }

  /**
   * Checks if is removed.
   *
   * @return true, if is removed
   */
  public boolean isRemoved() {
    return persistenceState.isRemoved();
  }

  /**
   * Mark changed.
   *
   */
  public void markChanged() {
    persistenceState.markChanged();
  }

  /**
   * Mark deleted.
   *
   */
  public void markDeleted() {
    persistenceState.markDeleted();
  }

  /**
   * Mark new.
   *
   */
  public void markNew() {
    persistenceState.markNew();
  }

  /**
   * Mark unchanged.
   *
   */
  public void markUnchanged() {
    persistenceState.markUnchanged();
  }

  /**
   * On delete.
   *
   * @param s the s
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onDelete(Session s) throws CallbackException {
    return persistenceState.onDelete(s);
  }

  /**
   * On load.
   *
   * @param s the s
   * @param id the id
   */
  public void onLoad(Session s, Serializable id) {
    persistenceState.onLoad(s, id);
  }

  /**
   * On save.
   *
   * @param s the s
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onSave(Session s) throws CallbackException {
    return persistenceState.onSave(s);
  }

  /**
   * On update.
   *
   * @param s the s
   * @return true, if successful
   * @throws CallbackException the callback exception
   */
  public boolean onUpdate(Session s) throws CallbackException {
    return persistenceState.onUpdate(s);
  }

  /**
   * Evict.
   *
   * @throws DataAccessException the data access exception
   */
  public void evict() throws DataAccessException {

  }

  /**
   * Generate new key.
   *
   * @throws DataAccessException the data access exception
   */

  public void generateNewKey() throws DataAccessException {

  }

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public AbstractDAO getDAO() {
    return null;
  }

}
