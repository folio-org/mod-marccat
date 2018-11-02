package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.AuthorityModel;

/**
 * The Class AuthorityModelDAO.
 *
 * @author paulm
 * @author carment
 * @since 1.0
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
