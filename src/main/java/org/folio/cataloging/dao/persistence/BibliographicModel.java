package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.dao.AbstractDAO;

/**
 * Represents a Model/Template for of bibliographic type
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class BibliographicModel extends Model {

	/**
	 * Class constructor.
	 *
	 * @since 1.0
	 */
	public BibliographicModel() {
		super();
	}

	/**
	 * Gets the dao.
	 *
	 * @return the dao
	 */
	@Deprecated
	public AbstractDAO getDAO() {
		return null;
	}
}
