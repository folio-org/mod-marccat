package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.AuthorityModelItem;

/**
 * @author elena
 *
 */
public class AuthorityModelItemDAO extends ModelItemDAO {

  /*
   * (non-Javadoc)
   * 
   * @see DAOModelItem#getPersistentClass()
   */
  protected Class getPersistentClass() {
    return AuthorityModelItem.class;
  }

}
