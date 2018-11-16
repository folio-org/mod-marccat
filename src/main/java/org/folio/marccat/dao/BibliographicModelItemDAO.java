package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.BibliographicModelItem;

/**
 * The Class BibliographicModelItemDAO.
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class BibliographicModelItemDAO extends ModelItemDAO {

  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  protected Class getPersistentClass() {
    return BibliographicModelItem.class;
  }


}
