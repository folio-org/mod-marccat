package org.folio.marccat.dao.persistence;

/**
 * The Class NameFunction.
 *
 * @author paulm
 */
public class NameFunction extends T_SINGLE {

  /**
   * Checks if is main entry.
   *
   * @param code the code
   * @return true, if is main entry
   */
  public static boolean isMainEntry(int code) {
    return code == 2 || code == 16;
  }

}
