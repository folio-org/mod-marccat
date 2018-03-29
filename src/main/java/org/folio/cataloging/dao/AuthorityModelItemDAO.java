/*
 * (c) LibriCore
 * 
 * Created on Dec 21, 2005
 * 
 * DAOAuthorityModelItem.java
 */
package org.folio.cataloging.dao;

import org.folio.cataloging.dao.persistence.AuthorityModelItem;

/**
 * The Class AuthorityModelItemDAO.
 *
 * @author paulm
 * @author carment
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
