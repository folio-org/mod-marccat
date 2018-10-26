/*
 * (c) LibriCore
 *
 * Created on May 10, 2006
 *
 * MandatoryTagException.java
 */
package org.folio.cataloging.exception;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/05/11 10:51:29 $
 * @since 1.0
 */
public class MandatoryTagException extends ValidationException {
  private String tagNumber;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public MandatoryTagException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public MandatoryTagException(String message) {
    super(message);
    setTagNumber(message);
  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public MandatoryTagException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public MandatoryTagException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param index
   * @since 1.0
   */
  public MandatoryTagException(int index) {
    super(index);
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public String getTagNumber() {
    return tagNumber;
  }

  /**
   * @since 1.0
   */
  public void setTagNumber(String string) {
    tagNumber = string;
  }

}
