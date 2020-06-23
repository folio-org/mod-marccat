
package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.AuthorityModelItem;

/**
 * The class manages the authority model item.
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
