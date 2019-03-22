package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.shared.CorrelationValues;

/**
 * Supertype layer for all fixed (control) fields.
 */
public abstract class FixedFieldUsingItemEntity extends FixedField implements PersistsViaItem {

  private ItemEntity itemEntity = null;


  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return (v.isValueDefined(1) && (v.getValue(1) != getHeaderType()));
  }

  public ItemEntity getItemEntity() {
    return itemEntity;
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#setItemEntity(ItemEntity)
   */
  public void setItemEntity(ItemEntity item) {
    itemEntity = item;
  }

}
