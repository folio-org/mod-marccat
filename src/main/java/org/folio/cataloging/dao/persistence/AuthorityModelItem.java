package org.folio.cataloging.dao.persistence;
import org.folio.cataloging.dao.AuthorityModelItemDAO;
import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * Represents a Model/Template for initiating new authority item
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class AuthorityModelItem extends ModelItem {
	/**
	 *
	 * Gets the dao.
	 *
	 * @return the dao
	 */

	public HibernateUtil getDAO() {
		return new AuthorityModelItemDAO();
	}


}
