/*
 * (c) LibriCore
 *
 * Created on Dec 21, 2005
 *
 * DAOAuthorityModelItem.java
 */
package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.AuthorityModelItem;

/**
 * The Class AuthorityModelItemDAO.
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
