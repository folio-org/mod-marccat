/*
 * (c) LibriCore
 * 
 * Created on 2004/11/30
 * 
 * $Author: Paulm $
 * $Date: 2005/12/01 13:50:04 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.7 $
 * $Source: /source/LibriSuite/src/librisuite/business/cataloguing/bibliographic/BibliographicModelItem.java,v $
 * $State: Exp $
 */
package librisuite.business.cataloguing.bibliographic;

import java.io.Serializable;

import librisuite.business.cataloguing.common.Catalog;
import librisuite.business.cataloguing.common.ModelItem;
import librisuite.business.common.Persistence;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author wimc
 * @version $Revision: 1.7 $, $Date: 2005/12/01 13:50:04 $
 * @see
 * @since 1.0
 */
public class BibliographicModelItem extends ModelItem implements Persistence, Serializable {


	public HibernateUtil getDAO() {
		return new DAOBibliographicModelItem();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.common.ModelItem#getCatalog()
	 */
	public Catalog getCatalog() {
		return new BibliographicCatalog();
	}

}
