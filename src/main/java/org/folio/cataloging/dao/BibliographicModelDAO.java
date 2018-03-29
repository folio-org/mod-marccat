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

	/* (non-Javadoc)
	 * @see ModelDAO#getModelItemDAO()
	 */
	protected ModelItemDAO getModelItemDAO() {
		return new BibliographicModelItemDAO();
	}

	/* (non-Javadoc)
	 * @see ModelDAO#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return BibliographicModel.class;
	}

}
