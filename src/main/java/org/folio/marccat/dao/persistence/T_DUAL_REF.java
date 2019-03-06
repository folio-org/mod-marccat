/*
 * (c) LibriCore
 *
 * Created on Jul 13, 2004
 *
 * T_DUAL_REF.java
 */
package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @version %I%, %Global%
 * @since 1.0
 */
public class T_DUAL_REF extends T_SINGLE {
  static public final short YES = 1;
  static public final short NO = 0;

  static public boolean isDual(short code) {
    return code == 1;
  }

}
