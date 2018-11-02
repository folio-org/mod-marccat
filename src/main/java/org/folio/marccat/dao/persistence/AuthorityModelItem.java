package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.AuthorityModelItemDAO;

/**
 * Represents a Model/Template for initiating new authority item
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class AuthorityModelItem extends ModelItem {
  /**
   * Gets the dao.
   *
   * @return the dao
   */

  public AbstractDAO getDAO() {
    return new AuthorityModelItemDAO();
  }


}
