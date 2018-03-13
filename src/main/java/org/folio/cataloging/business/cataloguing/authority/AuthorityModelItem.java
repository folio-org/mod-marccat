package org.folio.cataloging.business.cataloguing.authority;
import org.folio.cataloging.business.cataloguing.common.ModelItem;
import org.folio.cataloging.dao.AuthorityModelItemDAO;
import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @since 1.0
 */
public class AuthorityModelItem extends ModelItem {

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new AuthorityModelItemDAO();
	}



}
