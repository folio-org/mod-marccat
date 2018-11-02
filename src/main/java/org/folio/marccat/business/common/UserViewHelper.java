/*
 * (c) LibriCore
 *
 * Created on Nov 10, 2005
 *
 * UserViewHelper.java
 */
package org.folio.marccat.business.common;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.AbstractDAO;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:37 $
 * @since 1.0
 */
public class UserViewHelper implements PersistentObjectWithView, Serializable {
  private final PersistenceState persistenceState = new PersistenceState();

  String userViewString = "0000000000000000";

  /**
   * @since 1.0
   */
  public String getUserViewString() {
    return userViewString;
  }

  /**
   * @since 1.0
   */
  public void setUserViewString(String string) {
    userViewString = string;
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
  public boolean onDelete(Session s) throws CallbackException {
    return persistenceState.onDelete(s);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session s, Serializable id) {
    persistenceState.onLoad(s, id);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session s) throws CallbackException {
    return persistenceState.onSave(s);
  }

  /**
   * @since 1.0
   */
  public boolean onUpdate(Session s) throws CallbackException {
    return persistenceState.onUpdate(s);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#evict()
   */
  public void evict() throws DataAccessException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
    return null;
  }

}
