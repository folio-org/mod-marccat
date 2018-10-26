/*
 * (c) LibriCore
 *
 *
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

public class RelationReciprocal implements Serializable {

  private int relationshipTypeCode;
  private int relationshipReciprocalTypeCode;

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getRelationshipReciprocalTypeCode() {
    return relationshipReciprocalTypeCode;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setRelationshipReciprocalTypeCode(int i) {
    relationshipReciprocalTypeCode = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getRelationshipTypeCode() {
    return relationshipTypeCode;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setRelationshipTypeCode(int i) {
    relationshipTypeCode = i;
  }

  public boolean equals(Object other) {
    if (!(other instanceof RelationReciprocal)) return false;
    RelationReciprocal castOther = (RelationReciprocal) other;
    return ((castOther.getRelationshipTypeCode() == getRelationshipTypeCode())
      && (castOther.getRelationshipReciprocalTypeCode() == getRelationshipReciprocalTypeCode()));
  }

  public int hashCode() {
    return super.hashCode() + relationshipTypeCode + relationshipReciprocalTypeCode;
  }
}
