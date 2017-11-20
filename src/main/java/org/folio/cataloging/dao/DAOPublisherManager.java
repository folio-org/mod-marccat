/*
 * (c) LibriCore
 * 
 * Created on Dec 22, 2004
 * 
 * DAOPublisherTag.java
 */
package org.folio.cataloging.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.business.cataloguing.bibliographic.PublisherAccessPoint;
import org.folio.cataloging.business.cataloguing.bibliographic.PublisherManager;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.dao.persistence.PUBL_TAG;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;

/**
 * Although PublisherManager implements Persistence, it is in fact not mapped to
 * a table through Hibernate. Instead it delegates persistence to its
 * constituent access point and PUBL_TAGs
 * 
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2009/12/14 22:24:41 $
 * @since 1.0
 */
public class DAOPublisherManager extends HibernateUtil {

	/*
	 * (non-Javadoc)
	 * 
	 * @see HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(Persistence po) throws DataAccessException {
		if (!(po instanceof PublisherManager)) {
			throw new IllegalArgumentException(
					"I can only persist PublisherManager objects");
		}
		PublisherManager aPub = (PublisherManager) po;
		PublisherAccessPoint apf = aPub.getApf();
		apf.markDeleted();
		persistByStatus(apf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see HibernateUtil#save(librisuite.business.common.Persistence)
	 */
	public void save(final Persistence po) throws DataAccessException {
		if (!(po instanceof PublisherManager)) {
			throw new IllegalArgumentException(
					"I can only persist PublisherManager objects");
		}
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException, DataAccessException {
				int tagNumber = getNextPublisherTagNumber();
				PublisherManager aPub = (PublisherManager) po;
				PublisherAccessPoint apf = aPub.getApf();
				
				List publTags =/*((PublisherTagDescriptor)apf.getDescriptor())*/aPub.getPublisherTagUnits();
				Iterator ite = publTags.iterator();  
				while(ite.hasNext()){
				    PUBL_TAG pu =(PUBL_TAG) ite.next();
					pu.evict();
					pu.markNew();
					pu.setPublisherTagNumber(tagNumber);
					persistByStatus(pu);
				}
				apf.evict();
				apf.markNew();
				apf.setHeadingNumber(new Integer(tagNumber));
				persistByStatus(apf);
			}
		}.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see HibernateUtil#update(librisuite.business.common.Persistence)
	 */
	public void update(final Persistence p) throws DataAccessException {
		if (!(p instanceof PublisherManager)) {
			throw new IllegalArgumentException(
					"Can only persist PublisherManager objects");
		}
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException, DataAccessException {
				PublisherManager aPub = (PublisherManager) p;
				PublisherAccessPoint apf = aPub.getApf();
				apf.markDeleted();
				persistByStatus(apf);
				s.flush();
				apf.evict();
				apf.markNew();
				save(p);
			}
		}.execute();
	}

	public int getNextPublisherTagNumber() throws DataAccessException {
		class Intwrapper {
			public int result;
		}
		final Intwrapper result = new Intwrapper();
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException, DataAccessException {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				
				try {
					 stmt = s.connection().prepareStatement(
							"select publ_tag_seq.nextval from dual");
					rs = stmt.executeQuery();
					rs.next();
					result.result = rs.getInt(1);
				} catch (SQLException e) {
					throw new DataAccessException(e);
				}
				finally {
					try {
							
						if (stmt != null)
							stmt.close();
						if (rs != null)
				             rs.close();
						} catch (SQLException e) {
							logAndWrap(e);
						}
					}
				
			}
		}.execute();
		return result.result;
	}
}
