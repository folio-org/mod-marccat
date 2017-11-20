/*
 * (c) LibriCore
 * 
 * Created on 2005/01/04 08:28:23
 * 
 * $Author: Paulm $
 * $Date: 2005/12/21 08:30:32 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.3 $
 * $Source: /source/LibriSuite/src/librisuite/business/cataloguing/bibliographic/DAOBibliographicModelItem.java,v $
 * $State: Exp $
 */
package org.folio.cataloging.dao;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicModelItem;
import org.folio.cataloging.business.common.DataAccessException;

/**
 * @author Wim Crols
 * @version $Revision: 1.3 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOBibliographicModelItem extends DAOModelItem {

	/* (non-Javadoc)
	 * @see DAOModelItem#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return BibliographicModelItem.class;
	}
	
	/**
	 * @return true if the given model is used by an item
	 * 
	 * @since 1.0
	 */
public boolean getModelUsageByItem(int bibItem)
	throws DataAccessException {
	List list =
		find(
			"select count(*) from "
				+ getPersistentClass().getName()
				+ " as b"
				+ " where b.item = ?",
			new Object[] { new Integer(bibItem)},
			new Type[] { Hibernate.INTEGER });
	return ((Integer) (list.get(0))).intValue() > 0;
}

}
