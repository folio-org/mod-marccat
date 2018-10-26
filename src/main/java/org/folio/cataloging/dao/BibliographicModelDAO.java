package org.folio.cataloging.dao;

import org.folio.cataloging.dao.persistence.BibliographicModel;

/**
 * The Class BibliographicModelDAO.
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class BibliographicModelDAO extends ModelDAO {

  @Override
  protected ModelItemDAO getModelItemDAO() {
    return new BibliographicModelItemDAO();
  }

  @Override
  protected Class getPersistentClass() {
    return BibliographicModel.class;
  }
}
