package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.AuthorityModelItemDAO;

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
    return new AuthorityModelItemDAO ( );
  }


}
