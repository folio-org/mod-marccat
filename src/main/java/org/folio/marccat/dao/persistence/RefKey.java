package org.folio.marccat.dao.persistence;

import org.folio.marccat.config.log.Global;

import java.io.Serializable;


/**
 * Represents composite key for REF class.
 *
 * @author paulm
 * @author carment
 */
public class RefKey implements Serializable, Cloneable {

  /**
   * The source.
   */
  private Integer source = -1;

  /**
   * The target.
   */
  private Integer target = -1;

  /**
   * The type.
   */
  private Integer type = 2;

  /**
   * The user view string.
   */
  private String userViewString = Global.USER_VIEW_STRING;


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object anObject) {
    if (anObject instanceof RefKey) {
      RefKey aKey = (RefKey) anObject;
      return (
        source.equals(aKey.getSource())
          && target.equals(aKey.getTarget())
          && type.equals(aKey.getType()));
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return source + target + type;
  }

  /**
   * Getter for source.
   *
   * @return source
   */
  public Integer getSource() {
    return source;
  }

  /**
   * Setter for source.
   *
   * @param i source
   */
  public void setSource(Integer i) {
    source = i;
  }

  /**
   * Getter for target.
   *
   * @return target
   */
  public Integer getTarget() {
    return target;
  }

  /**
   * Setter for target.
   *
   * @param i target
   */
  public void setTarget(Integer i) {
    target = i;
  }

  /**
   * Getter for type.
   *
   * @return type
   */
  public Integer getType() {
    return type;
  }

  /**
   * Setter for type.
   *
   * @param s type
   */
  public void setType(int s) {
    type = s;
  }

  /**
   * Getter for view.
   *
   * @return view
   */
  public String getUserViewString() {
    return userViewString;
  }

  /**
   * Setter for view.
   *
   * @param s view
   */
  public void setUserViewString(String s) {
    userViewString = s;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#clone()
   */
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      return new Object();
    }
  }

}
