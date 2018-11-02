/*
 * (c) LibriCore
 *
 * Created on Jan 9, 2006
 *
 * NoReferenceHeadingSetException.java
 */
package org.folio.marccat.exception;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/11 13:36:23 $
 * @since 1.0
 */
public class NoHeadingSetException extends ValidationException {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public NoHeadingSetException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public NoHeadingSetException(String message) {
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
  public NoHeadingSetException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public NoHeadingSetException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param index
   * @since 1.0
   */
  public NoHeadingSetException(int index) {
    super(index);
  }

}
