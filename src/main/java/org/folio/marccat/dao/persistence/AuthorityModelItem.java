package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.AuthorityModelItemDAO;

/**
 * @author elena
 *
 */
public class AuthorityModelItem extends ModelItem {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Gets the dao.
   *
   * @return the dao
   */

  public AbstractDAO getDAO() {
    return new AuthorityModelItemDAO();
  }

}
