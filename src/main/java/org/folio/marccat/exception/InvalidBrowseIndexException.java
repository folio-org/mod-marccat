/*
 * (c) LibriCore
 *
 * Created on Nov 24, 2004
 *
 * InvalidBrowseIndexException.java
 */
package org.folio.marccat.exception;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/03/02 14:54:07 $
 * @since 1.0
 */
public class InvalidBrowseIndexException extends ModMarccatException {


  public InvalidBrowseIndexException() {
    super();

  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public InvalidBrowseIndexException(String message) {
    super(message);

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

  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public InvalidBrowseIndexException(Throwable cause) {
    super(cause);

  }

  public InvalidBrowseIndexException(Integer o) {
    super(o.toString());
  }
}
