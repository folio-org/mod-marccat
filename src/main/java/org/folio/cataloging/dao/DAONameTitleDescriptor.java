/*
 * (c) LibriCore
 * 
 * Created on Dec 13, 2005
 * 
 * DAONameTitleDescriptor.java
 */
package org.folio.cataloging.dao;

import java.util.Iterator;
import java.util.List;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.SortFormException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.NME_HDG;
import org.folio.cataloging.dao.persistence.NME_TTL_HDG;
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
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/01/11 13:36:23 $
 * @since 1.0
 */
public class DAONameTitleDescriptor extends DAODescriptor {
	private static final DAONameDescriptor daoName = new DAONameDescriptor();
	private static final DAOTitleDescriptor daoTitle = new DAOTitleDescriptor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see DAODescriptor#getPersistentClass()
	 */
	public Class getPersistentClass() {
		return NME_TTL_HDG.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DAODescriptor#supportsAuthorities()
	 */
	public boolean supportsAuthorities() {
		return true;
	}

	protected void loadHeadings(List l, int cataloguingView)
			throws DataAccessException {
		for (int i = 0; i < l.size(); i++) {
			NME_TTL_HDG aHdg = (NME_TTL_HDG) l.get(i);
			loadHeadings(aHdg, cataloguingView);
		}
	}

	protected void loadHeadings(NME_TTL_HDG d, int cataloguingView)
			throws DataAccessException {
		d.setNameHeading((NME_HDG) daoName.load(d.getNameHeadingNumber(),
				cataloguingView));
		d.setTitleHeading((TTL_HDG) daoTitle.load(d.getTitleHeadingNumber(),
				cataloguingView));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DAODescriptor#load(int, int)
	 */
	public Descriptor load(int headingNumber, int cataloguingView)
			throws DataAccessException {
		NME_TTL_HDG d = (NME_TTL_HDG) super
				.load(headingNumber, cataloguingView);
		loadHeadings(d, cataloguingView);
		return d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DAODescriptor#getXrefCount(Descriptor,
	 *      int)
	 */
	public int getXrefCount(Descriptor source, int cataloguingView)
			throws DataAccessException {

		int result = super.getXrefCount(source, cataloguingView);
		List l = find("select count(*) from NME_NME_TTL_REF as ref "
				+ " where ref.nameTitleHeadingNumber = ? "
				+ " and ref.sourceHeadingType = 'MH' "
				+ " and substr(ref.userViewString, ?, 1) = '1'", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });
		result = result + ((Integer) l.get(0)).intValue();

		l = find("select count(*) from TTL_NME_TTL_REF as ref "
				+ " where ref.nameTitleHeadingNumber = ? "
				+ " and ref.sourceHeadingType = 'MH' "
				+ " and substr(ref.userViewString, ?, 1) = '1'", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });
		result = result + ((Integer) l.get(0)).intValue();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DAODescriptor#getCrossReferences(Descriptor,
	 *      int)
	 */
	public List getCrossReferences(Descriptor source, int cataloguingView)
			throws DataAccessException {

		List l = super.getCrossReferences(source, cataloguingView);

		l.addAll(find("from NME_NME_TTL_REF as ref "
				+ " where ref.nameTitleHeadingNumber = ? "
				+ " and ref.sourceHeadingType = 'MH' "
				+ " and substr(ref.userViewString, ?, 1) = '1'", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER }));

		l.addAll(find("from TTL_NME_TTL_REF as ref "
				+ " where ref.nameTitleHeadingNumber = ? "
				+ " and ref.sourceHeadingType = 'MH' "
				+ " and substr(ref.userViewString, ?, 1) = '1'", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER }));
		return l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see DAODescriptor#loadReference(Descriptor,
	 *      Descriptor, short, int)
	 */
	public REF loadReference(Descriptor source, Descriptor target,
			short referenceType, int cataloguingView)
			throws DataAccessException {

		REF result = null;
		if (source.getClass() == target.getClass()) {
			return super.loadReference(source, target, referenceType,
					cataloguingView);
		} else if (target.getClass() == NME_HDG.class) {
			List l = find("from NME_NME_TTL_REF as ref "
					+ " where ref.nameTitleHeadingNumber = ? AND "
					+ " ref.nameHeadingNumber = ? AND "
					+ " ref.sourceHeadingType = 'MH' AND "
					+ " substr(ref.userViewString, ?, 1) = '1' AND "
					+ " ref.type = ?", new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(target.getKey().getHeadingNumber()),
					new Integer(cataloguingView), new Short(referenceType) },
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER,
							Hibernate.INTEGER, Hibernate.SHORT });
			if (l.size() == 1) {
				result = (REF) l.get(0);
				result = (REF) isolateView(result, cataloguingView);
			}
			return result;
		} else if (target.getClass() == TTL_HDG.class) {
			List l = find("from TTL_NME_TTL_REF as ref "
					+ " where ref.nameTitleHeadingNumber = ? AND "
					+ " ref.titleHeadingNumber = ? AND "
					+ " ref.sourceHeadingType = 'MH' AND "
					+ " substr(ref.userViewString, ?, 1) = '1' AND "
					+ " ref.type = ?", new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(target.getKey().getHeadingNumber()),
					new Integer(cataloguingView), new Short(referenceType) },
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER,
							Hibernate.INTEGER, Hibernate.SHORT });
			if (l.size() == 1) {
				result = (REF) l.get(0);
				result = (REF) isolateView(result, cataloguingView);
			}
			return result;
		} else {
			throw new RuntimeException("Unsupported cross-reference type");
		}
	}

	/**
	 * MIKE: get matching for both name and title
	 */
	// public Descriptor getMatchingHeading(Descriptor d) throws
	// DataAccessException, SortFormException {
	// NME_TTL_HDG ntd = (NME_TTL_HDG) d;
	// Descriptor nameDescriptor = ntd.getNameHeading();
	// Descriptor nameHdg =
	// ((DAODescriptor)nameDescriptor.getDAO()).getMatchingHeading(nameDescriptor);
	// if(nameHdg==null) return null;
	// Descriptor titleDescriptor = ntd.getTitleHeading();
	// Descriptor titleHdg =
	// ((DAODescriptor)titleDescriptor.getDAO()).getMatchingHeading(titleDescriptor);
	// if(titleHdg==null) return null;
	// return d;
	// }
	/**
	 * MIKE: get matching for both name and title If the heading is found, the
	 * NME_HDG and the TTL_HDG are the same of the descriptor passed as
	 * parameter, the the result is populated with this headings without
	 * accessing to the DB
	 */
	public Descriptor getMatchingHeading(Descriptor d)
			throws DataAccessException, SortFormException {
		NME_TTL_HDG ntd = (NME_TTL_HDG) d;
		List l = loadHeadings(ntd.getNameHeading(), ntd.getTitleHeading(), ntd
				.getKey().getUserViewString());
		if (l != null && l.size() > 0) {
			NME_TTL_HDG hdg = (NME_TTL_HDG) l.get(0);
			hdg.setNameHeading(ntd.getNameHeading());
			hdg.setTitleHeading(ntd.getTitleHeading());
			return hdg;
		}
		return null;
	}

	public boolean isMatchingAnotherHeading(Descriptor d) {
		try {
			NME_TTL_HDG ntd = (NME_TTL_HDG) d;
			// MIKE: verificare che il load non crei un conflitto di chiavi
			// Hibernate (Existent object ecc....)
				int view = View.toIntView(ntd.getUserViewString());

				Session s = currentSession();

				List l = null;
				try {
					Query q = s.createQuery("select distinct hdg from "
							+ "NME_TTL_HDG as hdg, "
							+ " where hdg.nameHeadingNumber = :nameKey "
							+ " and hdg.titleHeadingNumber = :titleKey "
							+ " and c.key.headingNumber <> :currHdgNbr "
							+ " and SUBSTR(hdg.key.userViewString, :view, 1) = '1' ");
					q.setInteger("nameKey", ntd.getNameHeadingNumber());
					q.setInteger("titleKey", ntd.getTitleHeadingNumber());
					q.setInteger("currHdgNbr", ntd.getHeadingNumber());
					q.setInteger("view", view);

					l = q.list();
					l = isolateViewForList(l, view);
				} catch (HibernateException e) {
					logAndWrap(e);
				}
				Iterator iter = l.iterator();
				while (iter.hasNext()) {
					Descriptor indb = (Descriptor)iter.next();
					if (ntd.getAuthoritySourceCode() == indb.getAuthoritySourceCode()) {
						if (ntd.getAuthoritySourceCode() == T_AUT_HDG_SRC.SOURCE_IN_SUBFIELD_2) {
							if (ntd.getAuthoritySourceText().equals(indb.getAuthoritySourceText())) {
								return true;
							}
						}
						else {
							return true;
						}
					}
				}
			return false;
		} catch (DataAccessException e) {
			return false;
		}
	}

	public List getHeadingsByBlankSortform(String operator, String direction,
			String term, String filter, int cataloguingView, int count)
			throws DataAccessException {
		// MIKE: standard feauture for Name/Title blank sortform because they
		// haven't sortforms
		return super.getHeadingsBySortform(operator, direction, term, filter,
				cataloguingView, count);
	}

	@Override
	public String calculateSearchTerm(String term, String browseIndex)
			throws DataAccessException {
		// calculate the sortform of the search term

		String searchTerm = super.calculateSearchTerm(term, browseIndex);
		if (term.indexOf(":") > 0) {
			String[] parsedTerm = term.split(":");
			// searchTerm = term.toUpperCase();
			String name = parsedTerm[0].trim();
			String title = parsedTerm[1].trim();
			searchTerm = calculateSearchTerm(name, "2P0") + " : "
					+ calculateSearchTerm(title, "7P0");
		}
		return searchTerm;
	}

	public List loadHeadings(NME_HDG nameHdg, TTL_HDG titleHdg,
			String cataloguingViewString) throws DataAccessException {
		int view = View.toIntView(cataloguingViewString);

		Session s = currentSession();

		List l = null;
		try {
			Query q = s.createQuery("select distinct hdg from "
					+ "NME_TTL_HDG as hdg, "
					+ " where hdg.nameHeadingNumber = :nameKey "
					+ " and hdg.titleHeadingNumber = :titleKey " + "  and "
					+ " SUBSTR(hdg.key.userViewString, :view, 1) = '1' ");
			q.setInteger("nameKey", nameHdg.getKey().getHeadingNumber());
			q.setInteger("titleKey", titleHdg.getKey().getHeadingNumber());
			q.setInteger("view", view);

			l = q.list();
			l = isolateViewForList(l, view);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	}


}
