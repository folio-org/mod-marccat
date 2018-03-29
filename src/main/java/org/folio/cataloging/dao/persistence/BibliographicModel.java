package org.folio.cataloging.dao.persistence;
import org.folio.cataloging.dao.BibliographicModelDAO;
import org.folio.cataloging.dao.common.HibernateUtil;

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
	 * Gets the bibliographic model DAO.
	 *
	 * @return the bibliographic model DAO
	 */
	public BibliographicModelDAO getBibliographicModelDAO() {
		return new BibliographicModelDAO();
	}


	/**
	 * Gets the dao.
	 *
	 * @return the dao
	 */
	@Deprecated
	public HibernateUtil getDAO() {
		return null;
	}
}
