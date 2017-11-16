/*
 * (c) LibriCore
 * 
 * Created on Dec 21, 2005
 * 
 * DAOAuthorityModelItem.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.cataloguing.common.DAOModelItem;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOAuthorityModelItem extends DAOModelItem {

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.DAOModelItem#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return AuthorityModelItem.class;
	}

}
