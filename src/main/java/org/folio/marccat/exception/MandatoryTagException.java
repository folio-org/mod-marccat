/*
 * (c) LibriCore
 *
 * Created on May 10, 2006
 *
 * MandatoryTagException.java
 */
package org.folio.marccat.exception;

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
   * @param message
   * @since 1.0
   */
  public MandatoryTagException(String message) {
    super(message);
    setTagNumber(message);
  }


  public void setTagNumber(String string) {
    tagNumber = string;
  }

}
