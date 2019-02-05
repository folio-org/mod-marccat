package org.folio.marccat.business.common;


/**
 * The Interface PersistentObjectWithView.
 */
public interface PersistentObjectWithView extends Persistence {

  /**
   * Gets the user view string.
   *
   * @return the user view string
   */
  String getUserViewString();

  /**
   * Sets the user view string.
   *
   * @param s the new user view string
   */
  void setUserViewString(String s);

}
