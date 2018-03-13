
package org.folio.cataloging.dao;

import org.folio.cataloging.business.cataloguing.authority.AuthorityModel;

/**
 * @author paulm
 * @since 1.0
 */
public class AuthorityModelDAO extends ModelDAO {

	/* (non-Javadoc)
	 * @see ModelDAO#getModelItemDAO()
	 */
	protected ModelItemDAO getModelItemDAO() {
		return  new AuthorityModelItemDAO();
	}

	/* (non-Javadoc)
	 * @see ModelDAO#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return AuthorityModel.class;
	}

}
