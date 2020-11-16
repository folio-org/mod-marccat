package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.AuthorityModelDAO;

/**
 * @author elena
 *
 */
public class AuthorityModel extends Model {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Gets the authority model DAO.
   *
   * @return the authority model DAO
   */
  public AuthorityModelDAO getAuthorityModelDAO() {
    return new AuthorityModelDAO();
  }

  /**
   * @deprecated
   */
  @Deprecated
  public AbstractDAO getDAO() {
    return null;
  }

}
