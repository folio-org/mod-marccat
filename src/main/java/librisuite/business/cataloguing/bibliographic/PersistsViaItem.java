/*
 * (c) LibriCore
 * 
 * Created on Oct 25, 2004
 * 
 * PersistsViaBibItem.java
 */
package librisuite.business.cataloguing.bibliographic;

import librisuite.business.cataloguing.common.ItemEntity;

/**
 * Interface for tags that get some/all of their data from BIB_ITM/AUT table
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public interface PersistsViaItem {

	public ItemEntity getItemEntity();

	public void setItemEntity(ItemEntity item);
	
}
