package org.folio.marccat.dao.persistence;

/**
 * The Class NameType.
 *
 * @author paulm
 */
public class NameType extends T_SINGLE {

  /**
   * Checks if is personal.
   *
   * @param c the c
   * @return true, if is personal
   */
  public static boolean isPersonal(int c) {
    return c == 2;
  }

  /**
   * Checks if is corporate.
   *
   * @param c the c
   * @return true, if is corporate
   */
  public static boolean isCorporate(int c) {
    return c == 3;
  }

  /**
   * Checks if is conference.
   *
   * @param c the c
   * @return true, if is conference
   */
  public static boolean isConference(int c) {
    return c == 4;
  }

  /**
   * Checks if is personal.
   *
   * @return true, if is personal
   */
  public boolean isPersonal() {
    return NameType.isPersonal(getCode());
  }

  /**
   * Checks if is corporate.
   *
   * @return true, if is corporate
   */
  public boolean isCorporate() {
    return NameType.isCorporate(getCode());
  }

  /**
   * Checks if is conference.
   *
   * @return true, if is conference
   */
  public boolean isConference() {
    return NameType.isPersonal(getCode());
  }

}
