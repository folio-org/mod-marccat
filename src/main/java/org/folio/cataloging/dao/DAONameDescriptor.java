package org.folio.cataloging.dao;

import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.ReferentialIntegrityException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.NME_HDG;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.TTL_HDG;
import org.folio.cataloging.dao.persistence.T_AUT_HDG_SRC;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.descriptor.Descriptor;

/**
 * Manages headings in the NME_HDG table
 * @author paulm
 */
@SuppressWarnings("unchecked")
public class DAONameDescriptor extends DAODescriptor 
{
	static protected Class persistentClass = NME_HDG.class;

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.business.Descriptor#getPersistentClass()
	 */
	public Class getPersistentClass() {
		return DAONameDescriptor.persistentClass;
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
				"select count(*) from NME_NME_TTL_REF as ref "
					+ " where ref.nameHeadingNumber = ? "
					+ " and ref.sourceHeadingType = 'NH' "
					+ " and substr(ref.userViewString, ?, 1) = '1'",
				new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(cataloguingView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		result = result + ((Integer) l.get(0)).intValue();
		l =
			find(
				"select count(*) from NME_TO_TTL_REF as ref "
					+ " where ref.nameHeadingNumber = ? "
					+ " and ref.sourceHeadingType = 'NH' "
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
					"from NME_NME_TTL_REF as ref "
						+ " where ref.nameHeadingNumber = ? "
						+ " and ref.sourceHeadingType = 'NH' "
						+ " and substr(ref.userViewString, ?, 1) = '1'",
					new Object[] {
						new Integer(source.getKey().getHeadingNumber()),
						new Integer(cataloguingView)},
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER }));
		l.addAll(
				find(
					"from NME_TO_TTL_REF as ref "
						+ " where ref.nameHeadingNumber = ? "
						+ " and ref.sourceHeadingType = 'NH' "
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
		} else if (target.getClass() == TTL_HDG.class) {
			REF result = null;
			List l =
				find(
					"from NME_TO_TTL_REF as ref "
						+ " where ref.nameHeadingNumber = ? AND "
						+ " ref.titleHeadingNumber = ? AND "
						+ " ref.sourceHeadingType = 'NH' AND "
						+ " substr(ref.userViewString, ?, 1) = '1' AND "
						+ " ref.type = ?",
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
		else {
			REF result = null;
			List l =
				find(
					"from NME_NME_TTL_REF as ref "
						+ " where ref.nameHeadingNumber = ? AND "
						+ " ref.nameTitleHeadingNumber = ? AND "
						+ " ref.sourceHeadingType = 'NH' AND "
						+ " substr(ref.userViewString, ?, 1) = '1' AND "
						+ " ref.type = ?",
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
		NME_HDG n = (NME_HDG) p;
		List l =
			find(
				"select count(*) from NME_TTL_HDG as t where "
					+ " t.nameHeadingNumber = ? and "
					+ " substr(t.key.userViewString, ?, 1) = '1'",
				new Object[] {
					new Integer(n.getKey().getHeadingNumber()),
					new Integer(View.toIntView(n.getUserViewString()))},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		if (((Integer) l.get(0)).intValue() > 0) {
			throw new ReferentialIntegrityException("NME_TTL_HDG", "NME_HDG");
		}
		super.delete(p);
	}
	
	public boolean isMatchingAnotherHeading(Descriptor desc) 
	{
		NME_HDG d = (NME_HDG) desc;
		try {
			List l = find(  " from "
							+ getPersistentClass().getName()
							+ " as c "
							+ " where c.stringText= ? "
							+ " and c.indexingLanguage = ? "
							+ " and c.accessPointLanguage = ?" 
							+ " and c.typeCode =? "
							+ " and c.subTypeCode =? "
							+ " and c.key.userViewString = ?"
							+ " and c.key.headingNumber <> ?",
					new Object[] { 
							d.getStringText(),
							new Integer(d.getIndexingLanguage()),
							new Integer(d.getAccessPointLanguage()),
							new Integer(d.getTypeCode()),
							new Integer(d.getSubTypeCode()),
							d.getUserViewString(),
							new Integer(d.getKey().getHeadingNumber()) },
					new Type[] { Hibernate.STRING,
							Hibernate.INTEGER, 
							Hibernate.INTEGER,
							Hibernate.INTEGER, 
							Hibernate.INTEGER,
							Hibernate.STRING,
							Hibernate.INTEGER});
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
}