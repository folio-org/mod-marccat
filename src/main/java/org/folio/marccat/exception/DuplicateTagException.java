/*
 * (c) LibriCore
 *
 * Created on Jul 15, 2005
 *
 * DuplicateTagException.java
 */
package org.folio.marccat.exception;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/11 13:36:23 $
 * @since 1.0
 */
public class DuplicateTagException extends ValidationException {


  public DuplicateTagException() {
    super();

  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public DuplicateTagException(int index) {
    super(index);
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public DuplicateTagException(String message) {
    super(message);

  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public DuplicateTagException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public DuplicateTagException(Throwable cause) {
    super(cause);

  }

}
