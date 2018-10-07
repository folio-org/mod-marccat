package org.folio.cataloging.business.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.dao.persistence.ItemEntity;
import org.folio.cataloging.shared.CorrelationValues;

/**
 * Supertype layer for all fixed (control) fields.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public abstract class FixedFieldUsingItemEntity extends FixedField implements PersistsViaItem {
  private static final Log logger = LogFactory.getLog (FixedFieldUsingItemEntity.class);

  private ItemEntity itemEntity = null;


  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return (v.isValueDefined (1) && (v.getValue (1) != getHeaderType ( )));
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
