/*
 * (c) LibriCore
 * 
 * Created on Dec 3, 2004
 * 
 * DAOThesaurusDescriptor.java
 */
package org.folio.cataloging.dao;

import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.ReferentialIntegrityException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.THS_HDG;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.Descriptor;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOThesaurusDescriptor extends DAODescriptor {

	static protected Class persistentClass = THS_HDG.class;

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.Descriptor#getPersistentClass()
	 */
	public Class getPersistentClass() {
		return DAOThesaurusDescriptor.persistentClass;
	}
	
	  public boolean supportsAuthorities() {
			return true;
		}
	
	public List getHeadingsBySortform(String operator, String direction, String term, String filter, int cataloguingView, int count) throws DataAccessException {
     	Session s = currentSession();
    	List l = null;
		try {
			String quetyString = "select distinct ths_hdg from "
                + getPersistentClass().getName()
                + " as ths_hdg where ths_hdg.sortForm "    
                + operator
                + " :term  and "
                + " SUBSTR(ths_hdg.key.userViewString, :view, 1) = '1' "
                + filter
                + " order by ths_hdg.sortForm "
                + direction;
//                logger.info(quetyString);
                
			
			Query q =
				s.createQuery(
					quetyString);
			q.setString("term", term);
			q.setInteger("view", cataloguingView);
			q.setMaxResults(count);
			l = q.list();
					
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		
		 return l;
	}
	
	/*Questa heading ha solo cross reference*/
	public void delete(Persistence p)
	throws ReferentialIntegrityException, DataAccessException {
	if (!(p instanceof Descriptor)) {
		throw new IllegalArgumentException("I can only delete Descriptor objects");
	}
	Descriptor d = ((Descriptor) p);

	// check for cross references
	/*if (supportsCrossReferences()) {
		if (getXrefCount(d, View.toIntView(d.getUserViewString())) > 0) {
			throw new ReferentialIntegrityException(
				d.getReferenceClass(d.getClass()).getName(),
				d.getClass().getName());
		}
	}

		/*if (supportsCrossReferences()) {
			if (d instanceof THS_HDG) {
				if (getXAtrCount(d, View.toIntView(d.getUserViewString())) > 0) {
					throw new ReferentialIntegrityException("Librisuite.hibernate.THS_ATRIB", d
							.getClass().getName());
				}
			}
		}*/
	// check for authorities
	if (supportsAuthorities()) {
		if (d.getAuthorityCount() > 0) {
			throw new ReferentialIntegrityException(
				d.getReferenceClass(d.getClass()).getName(),
				d.getClass().getName());
		}
	}

	// OK, go ahead and delete
	deleteDescriptor(p);
	
}
	
	/**
	 * Default implementation for delete with no cascade affects
	 * @since 1.0
	 */
	public void deleteDescriptor(final Persistence p) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				s.delete(p);
			}
		}
		.execute();
	}


}
