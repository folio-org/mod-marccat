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
   * @param index
   * @since 1.0
   */
  public NoHeadingSetException(int index) {
    super(index);
  }

}
