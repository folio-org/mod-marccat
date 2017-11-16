/*
 * (c) LibriCore
 * 
 * Created on Nov 8, 2005
 * 
 * AuthoirtyModelItem.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.cataloguing.common.Catalog;
import librisuite.business.cataloguing.common.ModelItem;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityModelItem extends ModelItem {

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAOAuthorityModelItem();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.ModelItem#getCatalog()
	 */
	public Catalog getCatalog() {
		return new AuthorityCatalog();
	}

}
