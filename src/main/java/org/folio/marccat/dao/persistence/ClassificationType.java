/*
 * (c) LibriCore
 *
 * Created on Jan 14, 2005
 *
 * ClassificationType.java
 */
package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/01/18 15:49:24 $
 * @since 1.0
 */
public class ClassificationType extends T_SINGLE {

  public static boolean isDewey(int s) {
    return s == 12;
  }

  public static boolean isLC(short s) {
    return s == 1;
  }

  public static boolean isNLM(short s) {
    return s == 6;
  }

  public static boolean isNAL(short s) {
    return s == 8;
  }
}
