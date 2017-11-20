/*
 * (c) LibriCore
 * 
 * Created on Nov 8, 2005
 * 
 * AuthoirtyModelItem.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.ModelItem;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.DAOAuthorityModelItem;

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
	 * @see ModelItem#getCatalog()
	 */
	public Catalog getCatalog() {
		return new AuthorityCatalog();
	}

}
