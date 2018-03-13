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
 * @since 1.0
 */
public class AuthorityModelItemDAO extends ModelItemDAO {

	/* (non-Javadoc)
	 * @see DAOModelItem#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return AuthorityModelItem.class;
	}

}
