/*
 * Created on Apr 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;

import java.io.Serializable;

/**
 * @author Carmen
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CLCTN_MST_HRCY implements Persistence {
  private int collectionCode;
  private int parentCollectionCode;
  private PersistenceState persistentState = new PersistenceState ( );


  /**
   * Class constructor
   *
   * @since 1.0
   */
  public CLCTN_MST_HRCY() {
    super ( );
  }

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public CLCTN_MST_HRCY(int collectionCode) {
    super ( );
    this.collectionCode = collectionCode;

  }


  /**
   * Class constructor
   *
   * @since 1.0
   */
  public CLCTN_MST_HRCY(int collectionCode, int parentCollectionCode) {
    super ( );
    this.collectionCode = collectionCode;
    this.parentCollectionCode = parentCollectionCode;
  }

  public int getParentCollectionCode() {
    return parentCollectionCode;
  }

  public void setParentCollectionCode(int parentCollectionCode) {
    this.parentCollectionCode = parentCollectionCode;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + collectionCode;
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass ( ) != obj.getClass ( ))
      return false;
    CLCTN_MST_HRCY other = (CLCTN_MST_HRCY) obj;
    return collectionCode == other.collectionCode;
  }

  public int getCollectionCode() {
    return collectionCode;
  }

  public void setCollectionCode(int collectionCode) {
    this.collectionCode = collectionCode;
  }

  /**
   * @since 1.0
   */
  public void evict(Object obj) throws DataAccessException {
    persistentState.evict (obj);
  }

  /**
   * @since 1.0
   */
  public AbstractDAO getDAO() {
    return persistentState.getDAO ( );
  }

  /**
   * @since 1.0
   */
  public int getUpdateStatus() {
    return persistentState.getUpdateStatus ( );
  }

  /**
   * @since 1.0
   */
  public void setUpdateStatus(int i) {
    persistentState.setUpdateStatus (i);
  }

  /**
   * @since 1.0
   */
  public boolean isChanged() {
    return persistentState.isChanged ( );
  }

  /**
   * @since 1.0
   */
  public boolean isDeleted() {
    return persistentState.isDeleted ( );
  }

  /**
   * @since 1.0
   */
  public boolean isNew() {
    return persistentState.isNew ( );
  }

  /**
   * @since 1.0
   */
  public boolean isRemoved() {
    return persistentState.isRemoved ( );
  }

  /**
   * @since 1.0
   */
  public void markChanged() {
    persistentState.markChanged ( );
  }

  /**
   * @since 1.0
   */
  public void markDeleted() {
    persistentState.markDeleted ( );
  }

  /**
   * @since 1.0
   */
  public void markNew() {
    persistentState.markNew ( );
  }

  /**
   * @since 1.0
   */
  public void markUnchanged() {
    persistentState.markUnchanged ( );
  }

  /**
   * @since 1.0
   */
  public boolean onDelete(Session arg0) throws CallbackException {
    return persistentState.onDelete (arg0);
  }

  /**
   * @since 1.0
   */
  public void onLoad(Session arg0, Serializable arg1) {
    persistentState.onLoad (arg0, arg1);
  }

  /**
   * @since 1.0
   */
  public boolean onSave(Session arg0) throws CallbackException {
    return persistentState.onSave (arg0);
  }

  /**
   * @since 1.0
   */
  public boolean onUpdate(Session arg0) throws CallbackException {
    return persistentState.onUpdate (arg0);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#evict()
   */
  public void evict() throws DataAccessException {
    evict (this);
  }

  /* (non-Javadoc)
   * @see librisuite.business.common.Persistence#generateNewKey()
   */
  public void generateNewKey() throws DataAccessException {
    // not applicable for this class
  }


}
