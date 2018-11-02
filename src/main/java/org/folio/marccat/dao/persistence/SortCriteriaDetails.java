/*
 * (c) LibriCore
 *
 * Created on Jul 28, 2005
 *
 * S_SRT_CRTRIA_DTL.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/07/28 11:41:23 $
 * @since 1.0
 */
public class SortCriteriaDetails implements Serializable {

  private short code;
  private short sequence;
  private short attribute;
  private short direction;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public SortCriteriaDetails() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public short getAttribute() {
    return attribute;
  }

  /**
   * @since 1.0
   */
  public void setAttribute(short s) {
    attribute = s;
  }

  /**
   * @since 1.0
   */
  public short getCode() {
    return code;
  }

  /**
   * @since 1.0
   */
  public void setCode(short s) {
    code = s;
  }

  /**
   * @since 1.0
   */
  public short getDirection() {
    return direction;
  }

  /**
   * @since 1.0
   */
  public void setDirection(short s) {
    direction = s;
  }

  /**
   * @since 1.0
   */
  public short getSequence() {
    return sequence;
  }

  /**
   * @since 1.0
   */
  public void setSequence(short s) {
    sequence = s;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof SortCriteriaDetails) {
      SortCriteriaDetails obj = (SortCriteriaDetails) arg0;
      return obj.getCode() == this.getCode() &&
        obj.getSequence() == this.getSequence();
    }
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getCode();
  }

}
