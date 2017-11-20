/*
 * (c) LibriCore
 * 
 * Created on Dec 20, 2005
 * 
 * AuthorityModel.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.Model;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.DAOAuthorityModel;

/**
 * Represents a Model/Template for initiating new Authority Items 
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class AuthorityModel extends Model {
	private static final AuthorityCatalog theCatalog = new AuthorityCatalog();
	private static final DAOAuthorityModel theDAO = new DAOAuthorityModel();
	
	private String wemiSecondGroup;
	private String wemiThirdGroup;
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityModel() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param catalogItem
	 * @since 1.0
	 */
	public AuthorityModel(CatalogItem catalogItem) {
		super(catalogItem);
	}

	/* (non-Javadoc)
	 * @see Model#getCatalog()
	 */
	public Catalog getCatalog() {
		return theCatalog;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return theDAO;
	}

	public String getWemiSecondGroup() {
		return wemiSecondGroup;
	}

	public void setWemiSecondGroup(String wemiSecondGroup) {
		this.wemiSecondGroup = wemiSecondGroup;
	}

	public String getWemiThirdGroup() {
		return wemiThirdGroup;
	}

	public void setWemiThirdGroup(String wemiThirdGroup) {
		this.wemiThirdGroup = wemiThirdGroup;
	}

}
