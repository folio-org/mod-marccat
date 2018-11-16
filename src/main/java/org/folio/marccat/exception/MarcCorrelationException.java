/*
 * Created on Sep 3, 2004
 *
 */
package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.business.common.DataAccessException;

/**
 * @author paulm
 */
public class MarcCorrelationException extends DataAccessException {

  /**
   *
   */
  public MarcCorrelationException() {
    super();
  }

  /**
   * @param message
   */
  public MarcCorrelationException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public MarcCorrelationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param cause
   */
  public MarcCorrelationException(Throwable cause) {
    super(cause);
  }

}
