package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.AuthorityModel;

/**
 * @author elena
 *
 */
public class AuthorityModelDAO extends ModelDAO {

	  @Override
	  protected ModelItemDAO getModelItemDAO() {
	    return new AuthorityModelItemDAO();
	  }

	  @Override
	  protected Class getPersistentClass() {
	    return AuthorityModel.class;
	  }

}
