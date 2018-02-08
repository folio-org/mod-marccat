/*
 * (c) LibriCore
 * 
 * Created on 2005/01/04 08:28:23
 * 
 * $Author: Paulm $
 * $Date: 2006/01/05 13:25:58 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.6 $
 * $Source: /source/LibriSuite/src/librisuite/business/cataloguing/bibliographic/DAOBibliographicModel.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.dao;

import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicModel;

/**
 * @author Wim Crols
 * @version $Revision: 1.6 $, $Date: 2006/01/05 13:25:58 $
 * @since 1.0
 */
public class BibliographicModelDAO extends ModelDAO {
	private static final DAOBibliographicModelItem itemDAO =
		new DAOBibliographicModelItem();

	/* (non-Javadoc)
	 * @see ModelDAO#getModelItemDAO()
	 */
	protected DAOModelItem getModelItemDAO() {
		return itemDAO;
	}

	/* (non-Javadoc)
	 * @see ModelDAO#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return BibliographicModel.class;
	}

}
