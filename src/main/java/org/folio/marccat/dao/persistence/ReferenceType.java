package org.folio.marccat.dao.persistence;

/**
 * The reference types for the cross references.
 *
 * @author paulm
 * @author carment
 */
public class ReferenceType extends T_SINGLE {

  /**
   * The Constant SEEN_FROM.
   */
  public static final short SEEN_FROM = 2;

  /**
   * Checks if is see also.
   *
   * @param type the type
   * @return true, if is see also
   */
  public static boolean isSeeAlso(int type) {
    return type == 3;
  }

  /**
   * Checks if is see also from.
   *
   * @param type the type
   * @return true, if is see also from
   */
  public static boolean isSeeAlsoFrom(int type) {
    return type == 4;
  }

  /**
   * Checks if is equivalence.
   *
   * @param type the type
   * @return true, if is equivalence
   */
  public static boolean isEquivalence(int type) {
    return type == 5;
  }

  /**
   * Checks if is see.
   *
   * @param type the type
   * @return true, if is see
   */
  public static boolean isSee(int type) {
    return type == 1;
  }

  /**
   * Checks if is seen from.
   *
   * @param type the type
   * @return true, if is seen from
   */
  public static boolean isSeenFrom(int type) {
    return type == 2;
  }

  /**
   * Checks if is authority tag.
   *
   * @param type the type
   * @return true, if is authority tag
   */
  public static boolean isAuthorityTag(int type) {
    return type == 2 || type == 4 || type == 5;
  }

  /**
   * Gets the reciprocal reference types
   *
   * @param type the type
   * @return the reciprocal
   */
  public static int getReciprocal(int type) {
    switch (type) {
      case 1:
        return 2;
      case 2:
        return 1;
      case 3:
        return 4;
      case 4:
        return 3;
      case 6:
        return 7;
      case 7:
        return 6;
      default:
        return type;
    }
  }
}
