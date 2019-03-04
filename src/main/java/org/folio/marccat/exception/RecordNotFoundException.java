/*
 * (c) LibriCore
 *
 * Created on Oct 13, 2004
 *
 * RecordNotFoundException.java
 */
package org.folio.marccat.exception;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class RecordNotFoundException extends DataAccessException {


  public RecordNotFoundException() {
    super();

  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public RecordNotFoundException(String message) {
    super(message);
  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public RecordNotFoundException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public RecordNotFoundException(Throwable cause) {
    super(cause);

  }

}
