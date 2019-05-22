package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

public abstract class VariableHeaderUsingItemEntity extends VariableHeader implements PersistsViaItem {

  private ItemEntity itemEntity;


  public VariableHeaderUsingItemEntity(int itemNumber) {
    super(itemNumber);
  }


  public VariableHeaderUsingItemEntity() {
    super();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
  @Override
  public boolean correlationChangeAffectsKey(CorrelationValues v) {
    return (v.isValueDefined(1) && (v.getValue(1) != getHeaderType()));
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#getItemEntity()
   */
  public ItemEntity getItemEntity() {
    return itemEntity;
  }

  /* (non-Javadoc)
   * @see PersistsViaItem#setItemEntity(java.lang.Object)
   */
  public void setItemEntity(ItemEntity item) {
    itemEntity = item;
  }

  public void deleteFromItem() {
    if (isAbleToBeDeleted()) {
      setStringText(new StringText(Subfield.SUBFIELD_DELIMITER + "a"));
    }
  }

  @Override
  public boolean isAbleToBeDeleted() {
    return true;
  }

}
