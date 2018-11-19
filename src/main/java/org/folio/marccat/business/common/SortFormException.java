/*
 * (c) LibriCore
 *
 * Created on Nov 30, 2004
 *
 * SortFormException.java
 */
package org.folio.marccat.business.common;


import org.folio.marccat.exception.DataAccessException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/02 17:20:52 $
 * @since 1.0
 */
public class SortFormException extends DataAccessException {


  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public SortFormException(String message) {
    super(message);
  }


}
