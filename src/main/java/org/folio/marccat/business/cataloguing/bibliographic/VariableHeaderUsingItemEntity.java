package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.dao.persistence.ItemEntity;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;


/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class VariableHeaderUsingItemEntity extends VariableHeader implements PersistsViaItem {

  private ItemEntity itemEntity;


  /**
   * Class constructor
   *
   * @since 1.0
   */
  public VariableHeaderUsingItemEntity(int itemNumber) {
    super(itemNumber);
  }

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public VariableHeaderUsingItemEntity() {
    super();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (obj.getClass() == this.getClass()) {
      return super.equals(obj);
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return super.hashCode();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
   */
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

  public boolean isAbleToBeDeleted() {
    return true;
  }

}
