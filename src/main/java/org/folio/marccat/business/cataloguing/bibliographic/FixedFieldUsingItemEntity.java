package org.folio.marccat.business.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.shared.CorrelationValues;

/**
 * Supertype layer for all fixed (control) fields.
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public abstract class FixedFieldUsingItemEntity extends FixedField implements PersistsViaItem {

  private ItemEntity itemEntity = null;


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
