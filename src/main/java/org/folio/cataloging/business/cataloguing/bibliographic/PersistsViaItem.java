package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.dao.persistence.ItemEntity;

/**
 * Interface for tags that get some/all of their data from BIB_ITM/AUT table.
 *
 * @author paulm
 * @since 1.0
 */
public interface PersistsViaItem {

	ItemEntity getItemEntity();

	void setItemEntity(ItemEntity item);
	
}
