package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @since 1.0
 */
public class T_DUAL_REF extends T_SINGLE {
  public static final short YES = 1;
  public static final short NO = 0;

  public static boolean isDual(short code) {
    return code == 1;
  }

}
