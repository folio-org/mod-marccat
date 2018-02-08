/*
 * (c) LibriCore
 * 
 * Created on Dec 21, 2005
 * 
 * DAOAuthorityModel.java
 */
package org.folio.cataloging.dao;

import org.folio.cataloging.business.cataloguing.authority.AuthorityModel;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityModelDAO extends ModelDAO {
	private static final DAOAuthorityModelItem theItemDAO = new DAOAuthorityModelItem();

	/* (non-Javadoc)
	 * @see ModelDAO#getModelItemDAO()
	 */
	protected DAOModelItem getModelItemDAO() {
		return theItemDAO;
	}

	/* (non-Javadoc)
	 * @see ModelDAO#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return AuthorityModel.class;
	}

}
