package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;

/**
 * Supertype layer for all leader fields.
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public abstract class Leader extends FixedFieldUsingItemEntity {

  /**
   * Always returns false, as leaders cannot be deleted.
   *
   * @return returns false.
   */
  @Override
  public boolean isAbleToBeDeleted() {
    return false;
  }
}
