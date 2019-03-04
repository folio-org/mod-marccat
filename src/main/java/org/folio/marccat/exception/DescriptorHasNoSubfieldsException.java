/*
 * (c) LibriCore
 *
 * Created on Oct 21, 2005
 *
 * DescriptorHasNoSubfieldsException.java
 */
package org.folio.marccat.exception;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/10/21 13:33:25 $
 * @since 1.0
 */
public class DescriptorHasNoSubfieldsException
  extends InvalidDescriptorException {


  public DescriptorHasNoSubfieldsException() {
    super();

  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public DescriptorHasNoSubfieldsException(String message) {
    super(message);

  }

  /**
   * Class constructor
   *
   * @param message
   * @param cause
   * @since 1.0
   */
  public DescriptorHasNoSubfieldsException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public DescriptorHasNoSubfieldsException(Throwable cause) {
    super(cause);

  }

}
