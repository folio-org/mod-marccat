package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.ItemEntity;
import org.folio.cataloging.business.common.CorrelationValues;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class FixedFieldUsingItemEntity extends FixedField implements PersistsViaItem 
{
	private static final Log logger = LogFactory.getLog(FixedFieldUsingItemEntity.class);

	private ItemEntity itemEntity = null;

	public FixedFieldUsingItemEntity() {
		super();
	}
//TODO when cloning these, keep the original bibItemData

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
	 * @see PersistsViaItem#setItemEntity(ItemEntity)
	 */
	public void setItemEntity(ItemEntity item) {
		itemEntity = item;
	}

}
