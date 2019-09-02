package org.folio.marccat.dao.persistence;


/**
 * Class comment
 *
 * @author paulm
 */

public class ClassificationType extends T_SINGLE {

  public static boolean isDewey(int s) {
    return s == 12;
  }

  public static boolean isLC(int s) {
    return s == 1;
  }

  public static boolean isNLM(int s) {
    return s == 6;
  }

  public static boolean isNAL(int s) {
    return s == 8;
  }
}
