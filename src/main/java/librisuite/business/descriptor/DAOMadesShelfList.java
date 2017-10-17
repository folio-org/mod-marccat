/*
 * (c) LibriCore
 * 
 * Created on 23-jul-2004
 * 
 * DAOShelfList.java
 */
package librisuite.business.descriptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import librisuite.business.cataloguing.mades.MAD_SHLF_LIST_ACS_PNT;
import librisuite.business.common.DAOSystemNextNumber;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.RecordNotFoundException;
import librisuite.business.common.ReferentialIntegrityException;
import librisuite.hibernate.MAD_SHLF_LIST;
import librisuite.hibernate.SHLF_LIST;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author elena
 * @version $Revision: 1.2 $, $Date: 2006/04/06 09:00:38 $
 * @since 1.0
 */
public class DAOMadesShelfList extends DAODescriptor {

	public MAD_SHLF_LIST load(int shelfListKeyNumber)
		throws DataAccessException, RecordNotFoundException {
		MAD_SHLF_LIST sl = null;
		try {
			Session s = currentSession();

			sl =
				(MAD_SHLF_LIST) s.get(
						MAD_SHLF_LIST.class,
					new Integer(shelfListKeyNumber));
			if (sl == null) {
				throw new RecordNotFoundException();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return sl;
	}

	public MAD_SHLF_LIST_ACS_PNT loadAccessPoint(int shelfListKeyNumber)
		throws DataAccessException {
		MAD_SHLF_LIST_ACS_PNT slap = null;
		try {
			Session s = currentSession();

			slap =
				(MAD_SHLF_LIST_ACS_PNT) s.get(
					MAD_SHLF_LIST_ACS_PNT.class,
					new Integer(shelfListKeyNumber));
		} catch (HibernateException e) {
			logAndWrap(e);

		} catch (NullPointerException e) {
			//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
		}
		return slap;
	}
	public MAD_SHLF_LIST getShelfList(
		String shelfListText,
		char shelfListType,
		int orgNumber)
		throws DataAccessException {
		MAD_SHLF_LIST result = null;
		MAD_SHLF_LIST sl = new MAD_SHLF_LIST();
		sl.setStringText(shelfListText);
		sl.setTypeCode(shelfListType);
		sl.setMainLibraryNumber(orgNumber);
		String shelfListSortForm = calculateSortForm(sl);
		try {
			Session s = currentSession();
			List shelfList =
				(List) s.find(
					"from MAD_SHLF_LIST as sl where sl.sortForm = ? "
						+ "AND sl.typeCode = ? "
						+ "AND sl.mainLibraryNumber = ? ",
					new Object[] {
						shelfListSortForm,
						new Character(sl.getTypeCode()),
						new Integer(sl.getMainLibraryNumber())},
					new Type[] {
						Hibernate.STRING,
						Hibernate.CHARACTER,
						Hibernate.INTEGER });

			if (shelfList.size() > 0) {
				sl = (MAD_SHLF_LIST) shelfList.get(0);
				sl.setSortForm(shelfListSortForm);
				result = sl;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (NullPointerException e) {
			//			TODO e.printStackTrace() is evil. If you catch, handle the exception.
			e.printStackTrace();
			throw e;
		}

		return result;
	}
	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#getPersistentClass()
	 */
	public Class getPersistentClass() {
		return MAD_SHLF_LIST.class;
	}
	
	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#getDocCount(librisuite.hibernate.Descriptor, int)
	 */
	public int getDocCount(Descriptor d, int cataloguingView)
		throws DataAccessException {
		
		
		Session s = currentSession();
		List l = null;
		int result = 0;
		try {
			l =
				s.find(
					" select count(*) from "
						+ d.getAccessPointClass().getName()
						+ " as apf "
						+ " where apf.shelfListKeyNumber = ?",
					new Object[] {
						 new Integer(((MAD_SHLF_LIST) d).getShelfListKeyNumber())},
					new Type[] { Hibernate.INTEGER });
			if (l.size() > 0) {
				result = ((Integer) l.get(0)).intValue();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#getHeadingsBySortform(java.lang.String, java.lang.String, int, int)
	 */
	public List getHeadingsBySortform(
		String operator,
		String direction,
		String term,
		String filter,
		int cataloguingView,
		int count)
		throws DataAccessException {
		Session s = currentSession();
		//TODO should shelf list browse restrict to the users main library?
		List l = null;
		try {
			Query q =
				s.createQuery(
					"from "
						+ getPersistentClass().getName()
						+ " as hdg where hdg.sortForm "
						+ operator
						+ " :term "
						+ filter
						+ " order by hdg.sortForm "
						+ direction);
			q.setString("term", term);
			q.setMaxResults(count);
			if (logger.isDebugEnabled()) {
				logger.debug("About to query:" + q.getQueryString());
			}

			l = q.list();
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	}

	
	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#supportsCrossReferences()
	 */
	public boolean supportsCrossReferences() {
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#load(int, int)
	 */
	/*
	 * override to provide editHeading access to shelflists ignoring userview
	 */
	public Descriptor load(int headingNumber, int cataloguingView)
		throws DataAccessException {
		return load(headingNumber);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.DAODescriptor#persist(librisuite.business.descriptor.Descriptor)
	 */
	public void persist(Descriptor descriptor) throws DataAccessException {
		if (descriptor.isNew()) {
			((MAD_SHLF_LIST) descriptor).setShelfListKeyNumber(
				new DAOSystemNextNumber().getNextNumber(
					descriptor.getNextNumberKeyFieldCode()));
		}
		persistByStatus(descriptor);
	}

	/* (non-Javadoc)
	 * @see com.libricore.librisuite.common.HibernateUtil#delete(librisuite.business.common.Persistence)
	 */
	public void delete(final Persistence p)
		throws ReferentialIntegrityException, DataAccessException {
		if (!(p instanceof MAD_SHLF_LIST)) {
			throw new IllegalArgumentException("I can only delete MAD_SHLF_LIST objects");
		}
		MAD_SHLF_LIST d = ((MAD_SHLF_LIST) p);
		// check for access point references
		List l =
			find(
				"select count(*) from "
					+ d.getAccessPointClass().getName()
					+ " as a where a.shelfListKeyNumber = ?",
				new Object[] { new Integer(d.getShelfListKeyNumber())},
				new Type[] { Hibernate.INTEGER });
		if (((Integer) l.get(0)).intValue() > 0) {
			throw new ReferentialIntegrityException(
				d.getAccessPointClass().getName(),
				d.getClass().getName());
		}
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
				throws HibernateException {
				s.delete(p);
			}
		}
		.execute();
	}
	public Descriptor getMatchingHeading(Descriptor d)
			throws DataAccessException {
		if (!(d instanceof MAD_SHLF_LIST)) {
			throw new IllegalArgumentException(
					"I can only match MAD_SHLF_LIST objects");
		}
		MAD_SHLF_LIST shelf = (MAD_SHLF_LIST) d;

		try {
			List l = currentSession()
					.find(
							"from "
									+ getPersistentClass().getName()
									+ " as c "
									+ " where c.stringText = ? and c.mainLibraryNumber = ? "
									+ " and c.typeCode = ? ",
							new Object[] {
									shelf.getStringText(),
									new Integer(shelf.getMainLibraryNumber()),
									new Character(shelf.getTypeCode()) },
							new Type[] { Hibernate.STRING, Hibernate.INTEGER,
									Hibernate.CHARACTER });
			if (l.size() == 1) {
				return (Descriptor) l.get(0);
			} else {
				return null;
			}
		} catch (HibernateException e) {
			logAndWrap(e);
			return null;
		}
	}

	public boolean isMatchingAnotherHeading(Descriptor d) {
		if (!(d instanceof MAD_SHLF_LIST)) {
			throw new IllegalArgumentException(
					"MAD_SHLF_LIST objects required but found " + d);
		}
		MAD_SHLF_LIST shelf = (MAD_SHLF_LIST) d;
		try {
			List l = currentSession()
					.find(
							"select count(*) from "
									+ getPersistentClass().getName()
									+ " as c"
									+ " where c.stringText = ? and c.mainLibraryNumber = ?"
									+ " and c.typeCode = ?"
									+ " and c.shelfListKeyNumber <> ?",
							new Object[] {
									shelf.getStringText(),
									new Integer(shelf.getMainLibraryNumber()),
									new Character(shelf.getTypeCode()),
									new Integer(((MAD_SHLF_LIST) d)
											.getShelfListKeyNumber()) },
							new Type[] { Hibernate.STRING, Hibernate.INTEGER,
									Hibernate.CHARACTER, Hibernate.INTEGER });
			return ((Integer) l.get(0)).intValue() > 0;
		} catch (Exception e) {
			return false;
		}
	}

}
