/*
 * (c) LibriCore
 *
 * Created on Oct 21, 2005
 *
 * DescriptorHasEmptySubfieldsException.java
 */
package org.folio.marccat.exception;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/10/21 13:33:25 $
 * @since 1.0
 */
public class DescriptorHasEmptySubfieldsException
  extends InvalidDescriptorException {


  public DescriptorHasEmptySubfieldsException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param message
   * @since 1.0
   */
  public DescriptorHasEmptySubfieldsException(String message) {
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
  public DescriptorHasEmptySubfieldsException(
    String message,
    Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * Class constructor
   *
   * @param cause
   * @since 1.0
   */
  public DescriptorHasEmptySubfieldsException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

}
