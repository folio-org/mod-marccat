package org.folio.marccat.business.common;

import net.sf.hibernate.Lifecycle;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;


/**
 * Implementing this interface indicates that the implementing object is known to the persistency layer (Hibernate).
 */
public interface Persistence extends Lifecycle, Serializable {



  /**
   * Gets the update status.
   *
   * @return the update status
   */
  int getUpdateStatus();

  /**
   * Sets the update status.
   *
   * @param i the new update status
   */
  void setUpdateStatus(int i);

  /**
   * Checks if is changed.
   *
   * @return true, if is changed
   */
  boolean isChanged();

  /**
   * Checks if is deleted.
   *
   * @return true, if is deleted
   */
  boolean isDeleted();

  /**
   * Checks if is new.
   *
   * @return true, if is new
   */
  boolean isNew();

  /**
   * Mark changed.
   */
  void markChanged();

  /**
   * Mark new.
   */
  void markNew();

  /**
   * Mark unchanged.
   */
  void markUnchanged();

  /**
   * Mark deleted.
   */
  void markDeleted();

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  AbstractDAO getDAO();

}
