package org.folio.marccat.business.common;

import net.sf.hibernate.Lifecycle;
import org.folio.marccat.dao.AbstractDAO;

import java.io.Serializable;

/**
 * Implementing this interface indicates that the implementing object is known to the persistency layer (Hibernate).
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public interface Persistence extends Lifecycle, Serializable {


  /**
   * Removes the object from the persistence session
   *
   * @since 1.0
   */
  @Deprecated
  void evict() throws DataAccessException;

  /**
   *
   */
  int getUpdateStatus();

  void setUpdateStatus(int i);

  boolean isChanged();

  boolean isDeleted();

  boolean isNew();

  /**
   * If object is now UNCHANGED make it CHANGED (otherwise leave it alone)
   */
  void markChanged();

  void markNew();

  void markUnchanged();

  void markDeleted();

  /**
   * causes the object to generate new key values
   */
  //commented by nbianchini
  //void generateNewKey() throws DataAccessException;


  AbstractDAO getDAO();

}
