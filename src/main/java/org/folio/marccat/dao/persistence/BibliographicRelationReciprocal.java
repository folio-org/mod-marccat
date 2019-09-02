package org.folio.marccat.dao.persistence;


/**
 * Class comment
 *
 * @author paulm
 */
public class BibliographicRelationReciprocal extends T_SINGLE {
  public static final short RECIPROCAL = 1;
  public static final short ONE_WAY = 2;
  public static final short BLIND = 3;

  public static boolean isBlind(int code) {
    return code == BLIND;
  }

  public static boolean isReciprocal(int code) {
    return code == RECIPROCAL;
  }

  public static boolean isOneWay(int code) {
    return code == ONE_WAY;
  }

}
