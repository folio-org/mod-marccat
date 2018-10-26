/*
 * (c) LibriCore
 *
 * Created on Nov 24, 2004
 *
 * InvalidBrowseIndexException.java
 */
package org.folio.cataloging.business.searching;

import org.folio.cataloging.exception.ModCatalogingException;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/03/02 14:54:07 $
 * @since 1.0
 */
public class InvalidBrowseIndexException extends ModCatalogingException {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public InvalidBrowseIndexException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public InvalidBrowseIndexException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public InvalidBrowseIndexException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public InvalidBrowseIndexException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  public InvalidBrowseIndexException(Integer o) {
    super(o.toString());
  }
}
