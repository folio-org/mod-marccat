/*
 * (c) LibriCore
 *
 * Created on Jan 9, 2006
 *
 * ValidationException.java
 */
package org.folio.marccat.exception;

/**
 * Base class for exceptions when validating tags.  The constructor
 *
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/11 13:36:23 $
 * @since 1.0
 */
public class ValidationException extends ModMarccatException {

  protected int tagIndex = 0;


  public ValidationException() {
    super();

  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public ValidationException(String message) {
    super(message);

  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public ValidationException(Throwable cause) {
    super(cause);

  }

  /**
   * includes index of the offending tag in the exception.  Needed
   * when thrown from the editing worksheet.
   * Class constructor
   *
   * @param index
   * @since 1.0
   */
  public ValidationException(int index) {
    setTagIndex(index);
  }



  public void setTagIndex(int i) {
    tagIndex = i;
  }

}
