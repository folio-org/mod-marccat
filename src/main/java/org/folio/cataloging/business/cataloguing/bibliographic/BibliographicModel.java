package org.folio.cataloging.business.cataloguing.bibliographic;
import org.folio.cataloging.business.cataloguing.common.Model;
import org.folio.cataloging.dao.AuthorityModelDAO;
import org.folio.cataloging.dao.BibliographicModelDAO;
import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @since 1.0
 */
public class BibliographicModel extends Model {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public BibliographicModel() {
		super();
	}


	public BibliographicModelDAO getBibliographicModelDAO() {
		return new BibliographicModelDAO();
	}


	@Override
	public HibernateUtil getDAO() {
		return null;
	}
}
