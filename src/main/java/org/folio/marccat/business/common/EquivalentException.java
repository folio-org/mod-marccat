/*
 * (c) LibriCore
 *
 * Created on Jul 2, 2004
 *
 * DataAccessException.java
 */
package org.folio.marccat.business.common;

import org.folio.marccat.exception.ModCatalogingException;


/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class EquivalentException extends ModCatalogingException {

  /**
   * @see ModCatalogingException#ModCatalogingException()
   */
  public EquivalentException() {
    super();
  }

  /**
   * @see ModCatalogingException#ModCatalogingException(String)
   */
  public EquivalentException(String message) {
    super(message);
  }

  /**
   * @see ModCatalogingException#ModCatalogingException(String, Throwable)
   */
  public EquivalentException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @see ModCatalogingException#ModCatalogingException(Throwable)
   */
  public EquivalentException(Throwable cause) {
    super(cause);
  }

}
