/*
 * (c) LibriCore
 * 
 * Created on Jun 21, 2004
 * 
 * NameDescriptor.java
 */
package org.folio.cataloging.dao;

import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.ReferentialIntegrityException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.TTL_HDG;
import org.folio.cataloging.dao.persistence.T_AUT_HDG_SRC;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.descriptor.Descriptor;

/**
 * Manages headings in the TTL_HDG table
 * @author paulm
 */
public class DAOTitleDescriptor extends DAODescriptor {
	static protected Class persistentClass = TTL_HDG.class;

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.Descriptor#getPersistentClass()
	 */
	public Class getPersistentClass() {
		return DAOTitleDescriptor.persistentClass;
	}
	/* (non-Javadoc)
	 * @see DAODescriptor#supportsAuthorities()
	 */
	public boolean supportsAuthorities() {
		return true;
	}

	/* (non-Javadoc)
	 * @see DAODescriptor#getXrefCount(Descriptor, int)
	 */
	public int getXrefCount(Descriptor source, int cataloguingView)
		throws DataAccessException {

		int result = super.getXrefCount(source, cataloguingView);
		List l =
			find(
				"select count(*) from TTL_NME_TTL_REF as ref "
					+ " where ref.titleHeadingNumber = ? "
					+ " and ref.sourceHeadingType = 'TH' "
					+ " and substr(ref.userViewString, ?, 1) = '1'",
				new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(cataloguingView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		result = result + ((Integer)l.get(0)).intValue();
		l =
			find(
				"select count(*) from NME_TO_TTL_REF as ref "
					+ " where ref.titleHeadingNumber = ? "
					+ " and ref.sourceHeadingType = 'TH' "
					+ " and substr(ref.userViewString, ?, 1) = '1'",
				new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(cataloguingView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		result = result + ((Integer) l.get(0)).intValue();
		return result;
	}

	/* (non-Javadoc)
	 * @see DAODescriptor#getCrossReferences(Descriptor, int)
	 */
	public List getCrossReferences(Descriptor source, int cataloguingView)
		throws DataAccessException {

		List l = super.getCrossReferences(source, cataloguingView);

		l.addAll(
			find(
				"from TTL_NME_TTL_REF as ref "
					+ " where ref.titleHeadingNumber = ? "
					+ " and ref.sourceHeadingType = 'TH' "
					+ " and substr(ref.userViewString, ?, 1) = '1'",
				new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(cataloguingView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER }));
		l.addAll(
				find(
					"from NME_TO_TTL_REF as ref "
						+ " where ref.titleHeadingNumber = ? "
						+ " and ref.sourceHeadingType = 'TH' "
						+ " and substr(ref.userViewString, ?, 1) = '1'",
					new Object[] {
						new Integer(source.getKey().getHeadingNumber()),
						new Integer(cataloguingView)},
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER }));
		return l;
	}

	/* (non-Javadoc)
	 * @see DAODescriptor#loadReference(Descriptor, Descriptor, short, int)
	 */
	public REF loadReference(
		Descriptor source,
		Descriptor target,
		short referenceType,
		int cataloguingView)
		throws DataAccessException {

		if (source.getClass() == target.getClass()) {
			return super.loadReference(
				source,
				target,
				referenceType,
				cataloguingView);
		}
		else {
			REF result = null;
			List l =
				find(
					"from TTL_NME_TTL_REF as ref "
						+ " where ref.titleHeadingNumber = ? AND "
						+ " ref.nameTitleHeadingNumber = ? AND "
						+ " ref.sourceHeadingType = 'TH' AND "
						+ " substr(ref.key.userViewString, ?, 1) = '1' AND "
						+ " ref.key.type = ?",
					new Object[] {
						new Integer(source.getKey().getHeadingNumber()),
						new Integer(target.getKey().getHeadingNumber()),
						new Integer(cataloguingView),
						new Short(referenceType)},
					new Type[] {
						Hibernate.INTEGER,
						Hibernate.INTEGER,
						Hibernate.INTEGER,
						Hibernate.SHORT });
			if (l.size() == 1) {
				result = (REF) l.get(0);
				result = (REF) isolateView(result, cataloguingView);
			}
			return result;
		}
	}
	
	/* (non-Javadoc)
	 * @see HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(Persistence p)
		throws ReferentialIntegrityException, DataAccessException {
		/*
		 * first check for name/title usage
		 */
		TTL_HDG t = (TTL_HDG) p;
		List l = find(
			"select count(*) from NME_TTL_HDG as d where "
				+ " d.nameHeadingNumber = ? and "
				+ " substr(d.key.userViewString, ?, 1) = '1'",
			new Object[] {
				new Integer(t.getKey().getHeadingNumber()),
				new Integer(View.toIntView(t.getUserViewString()))},
			new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		if (((Integer)l.get(0)).intValue() > 0) {
			throw new ReferentialIntegrityException("NME_TTL_HDG", "TTL_HDG");
		}
		super.delete(p);
	}
	
	public boolean isMatchingAnotherHeading(Descriptor desc) {
		TTL_HDG d = (TTL_HDG) desc;
		try {
			List l = find(" from "
							+ getPersistentClass().getName()
							+ " as c "
							+ " where c.stringText= ? " 
							+ " and c.indexingLanguage = ? "
							+ " and c.accessPointLanguage = ?" 
							+ " and c.key.userViewString = ?"
							+ " and c.key.headingNumber <> ?",
					new Object[] { 
							d.getStringText(),
							new Integer(d.getIndexingLanguage()),
							new Integer(d.getAccessPointLanguage()),
							d.getUserViewString(),
							new Integer(d.getKey().getHeadingNumber()) },
					new Type[] { Hibernate.STRING,
							Hibernate.INTEGER, 
							Hibernate.STRING,
							Hibernate.INTEGER });
			Iterator iter = l.iterator();
			while (iter.hasNext()) {
				Descriptor indb = (Descriptor)iter.next();
				if (d.getAuthoritySourceCode() == indb.getAuthoritySourceCode()) {
					if (d.getAuthoritySourceCode() == T_AUT_HDG_SRC.SOURCE_IN_SUBFIELD_2) {
						if (d.getAuthoritySourceText().equals(indb.getAuthoritySourceText())) {
							return true;
						}
					}
					else {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	public String getISSNString(int headingNumber) {
		String sortform = "";
		List result = null;
		try {
			Session s = null;
			try {
				s = currentSession();
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Query q = s.createQuery("select c.sortForm" + " from CNTL_NBR as c " + " where c.key.headingNumber =" + headingNumber);
			q.setMaxResults(1);
			result = q.list();
			if (result.size() > 0) {
				sortform = (String) result.get(0);
			}

		} catch (HibernateException e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return sortform;
	}


}
