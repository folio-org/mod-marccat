package org.folio.marccat.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/09/27 08:24:29 $
 * @since 1.0
 */
public class SubjectSource extends T_SINGLE {
  private static final short USES_SECONDARY_SOURCE = 9;

  public static boolean isOtherSource(int code) {
    return code == USES_SECONDARY_SOURCE;
  }
}
