/*
 * (c) LibriCore
 * 
 * Created on 2004/11/30
 * 
 * $Author: Paulm $
 * $Date: 2005/12/21 08:30:32 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.28 $
 * $Source: /source/LibriSuite/src/librisuite/business/cataloguing/bibliographic/BibliographicModel.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.cataloguing.common.Model;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.DAOBibliographicModel;

/**
 * @author wimc
 * @version $Revision: 1.28 $, $Date: 2005/12/21 08:30:32 $
 * @see
 * @since 1.0
 */
public class BibliographicModel extends Model {

	private static final Catalog theCatalog = new BibliographicCatalog();
	
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public BibliographicModel() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * @param catalogItem
	 * @since 1.0
	 */
	public BibliographicModel(CatalogItem catalogItem) {
		super(catalogItem);
		// TODO Auto-generated constructor stub
	}

	public HibernateUtil getDAO() {
		return new DAOBibliographicModel();
	}

	/* (non-Javadoc)
	 * @see Model#getCatalog()
	 */
	public Catalog getCatalog() {
		return theCatalog;
	}

}
