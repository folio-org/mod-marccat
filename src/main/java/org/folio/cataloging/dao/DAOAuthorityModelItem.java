/*
 * (c) LibriCore
 * 
 * Created on Dec 21, 2005
 * 
 * DAOAuthorityModelItem.java
 */
package org.folio.cataloging.dao;

import org.folio.cataloging.business.cataloguing.authority.AuthorityModelItem;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOAuthorityModelItem extends DAOModelItem {

	/* (non-Javadoc)
	 * @see DAOModelItem#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return AuthorityModelItem.class;
	}

}
