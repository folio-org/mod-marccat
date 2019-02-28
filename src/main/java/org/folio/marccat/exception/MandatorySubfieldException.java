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
public class MandatorySubfieldException extends RuntimeException {
  private String tagNumber;
  private String subfieldCode;

  public MandatorySubfieldException(String tagNumber, String subfieldCode) {
    super();
    this.tagNumber = tagNumber;
    this.subfieldCode = subfieldCode;
  }


  public MandatorySubfieldException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public MandatorySubfieldException(String subfieldCode) {
    super(subfieldCode);
    setSubfieldCode(subfieldCode);
  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public MandatorySubfieldException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public MandatorySubfieldException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }


  public String getTagNumber() {
    return tagNumber;
  }


  public void setTagNumber(String string) {
    tagNumber = string;
  }

  /**
   * @return Returns the subfieldCode.
   */
  public String getSubfieldCode() {
    return subfieldCode;
  }

  /**
   * @param subfieldCode The subfieldCode to set.
   */
  public void setSubfieldCode(String subfieldCode) {
    this.subfieldCode = subfieldCode;
  }

}
