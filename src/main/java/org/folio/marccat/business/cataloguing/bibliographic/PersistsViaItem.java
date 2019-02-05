package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.dao.persistence.ItemEntity;

/**
 * Interface for tags that get some/all of their data from BIB_ITM/AUT table.
 */
public interface PersistsViaItem {

  ItemEntity getItemEntity();

  void setItemEntity(ItemEntity item);

}
