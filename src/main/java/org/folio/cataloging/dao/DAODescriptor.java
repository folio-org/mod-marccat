package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.cataloguing.authority.AuthorityCatalog;
import org.folio.cataloging.business.common.*;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.business.searching.BrowseManager;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;
import org.folio.cataloging.dao.persistence.*;

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static org.folio.cataloging.F.deepCopy;

/**
 * An abstract class representing separately indexed tables that access
 * bibliographic items. (e.g. Name, Title, Subject, ...)
 * 
 * Descriptor objects contain members to identify unique keys for "headings" and
 * also contain a reference to the associated persistence class.
 * 
 * @author paulm
 * @since 1.0
 */
public abstract class DAODescriptor extends HibernateUtil {


	public static final String BLANK_SORTFORM = " ";

	protected static Log logger = LogFactory.getLog(DAODescriptor.class);

	/**
	 * Gets the name of the associated Persistent class
	 * 
	 * @return the name
	 */
	public abstract Class getPersistentClass();

	/**
	 * default implemenation indicating whether the Descriptor class may have
	 * cross-references (default true)
	 */
	public boolean supportsCrossReferences() {
		return true;
	}

	/**
	 * default implemenation indicating whether the Descriptor class may have
	 * authorities (default false)
	 */
	public boolean supportsAuthorities() {
		return true;
	}

	/**
	 * Calculates the sort form of a string (search term) when the type of
	 * descriptor is not known, or not available. Note that this method is
	 * overloaded. The (Descriptor) version should be used for calculating
	 * sortforms when a Descriptor is available
	 */
	public String calculateSortForm(String in) throws DataAccessException {
		String result = "";
		int bufSize = 300;

		if ("".equals(in)) {
			return BLANK_SORTFORM;
		}

		Session s = currentSession();
		CallableStatement proc = null;
		try {
			Connection connection = s.connection();
			proc = connection
					.prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_NORMALIZE(?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, in);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.setInt(4, bufSize);
			proc.execute();
			result = proc.getString(3);
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				if (proc != null)
					proc.close();
			} catch (SQLException e) {
				// do nothing
				e.printStackTrace();
			}

		}
		return result;
	}

	/**
	 * Calculates the sortform of a descriptor. The method is overloaded. The
	 * (String) version can be used to normalize a string when no Descriptor is
	 * available.
	 * 
	 * @since 1.0
	 */
	public String calculateSortForm(Descriptor d) throws DataAccessException {

		if ("".equals(d.getStringText())) {
			return BLANK_SORTFORM;
		}

		SortFormParameters parms = d.getSortFormParameters();
		return calculateSortForm(d.getStringText(), parms);
	}

	/**
	 * Calculates the sortform of a String with known search index. The method
	 * is overloaded. (String) and (Descriptor) versions exist.
	 * 
	 * @since 1.0
	 */
	public String calculateSortForm(String s, String browseIndexKey)
			throws DataAccessException {
		if ("".equals(s)) {
			return " ";
		}

		SortFormParameters parms = new DAOIndexList()
				.getSortFormParametersByKey(browseIndexKey);
		return calculateSortForm(s, parms);
	}

	// protected String calculateSortForm(String text, SortFormParameters parms)
	public String calculateSortForm(String text, SortFormParameters parms)
			throws DataAccessException {
		String result = "";
		int bufSize = 300;

		Session s = currentSession();
		CallableStatement proc = null;
		Connection connection = null;
		int rc;
		try {
			connection = s.connection();
			proc = connection
					.prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_PREPROCESS(?, ?, ?, ?, ?, ?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, text);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.setInt(4, bufSize);
			proc.setInt(5, parms.getSortFormMainType());
			proc.setInt(6, parms.getSortFormSubType());
			proc.setInt(7, parms.getNameTitleOrSubjectType());
			proc.setInt(8, parms.getNameSubtype());
			proc.setInt(9, parms.getSkipInFiling());
			proc.execute();

			rc = proc.getInt(1);

			if (rc != 0) {
				throw new SortFormException(String.valueOf(rc));
			}
			result = proc.getString(3);

			// MIKE: closing previous CallableStatement
			proc.close();

			proc = connection
					.prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_BUILDSRTFRM(?, ?, ?, ?, ?, ?, ?, ?) }");
			proc.registerOutParameter(1, Types.INTEGER);
			proc.setString(2, result);
			proc.registerOutParameter(3, Types.VARCHAR);
			proc.setInt(4, bufSize);
			proc.setInt(5, parms.getSortFormMainType());
			proc.setInt(6, parms.getSortFormSubType());
			proc.setInt(7, parms.getNameTitleOrSubjectType());
			proc.setInt(8, parms.getNameSubtype());
			proc.setInt(9, parms.getSkipInFiling());
			proc.execute();

			rc = proc.getInt(1);

			if (rc != 0) {
				throw new SortFormException(String.valueOf(rc));
			}
			result = proc.getString(3);
		} catch (HibernateException e) {
			logAndWrap(e);
		} catch (SQLException e) {
			logAndWrap(e);
		} finally {
			try {
				if (proc != null) {
					proc.close();
				}
			} catch (SQLException e) {
				// do nothing
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * loads the heading member from the database based on the settings of the
	 * key values
	 * 
	 */
	public Descriptor load(int headingNumber, int cataloguingView)
			throws DataAccessException {
		return load(headingNumber, cataloguingView, getPersistentClass());
	}

	/**
	 * loads the heading member from the database based on the settings of the
	 * key values
	 * 
	 */
	public Descriptor load(int headingNumber) throws DataAccessException {
		return load(headingNumber, getPersistentClass());
	}

	public Descriptor load(int headingNumber, int cataloguingView,
			Class persistentClass) throws DataAccessException {

		List l = find("from " + persistentClass.getName()
				+ " as hdg where hdg.key.headingNumber = ? "
				+ " AND substr(hdg.key.userViewString, ?, 1) = '1'",
				new Object[] { new Integer(headingNumber),
						new Integer(cataloguingView) }, new Type[] {
						Hibernate.INTEGER, Hibernate.INTEGER });

		Descriptor result = null;
		if (!l.isEmpty()) {
			result = (Descriptor) l.get(0);
			result = (Descriptor) isolateView(result, cataloguingView);
		} else {
			logger.warn("No descriptor of type '" + persistentClass.getName()
					+ "' found for heading number '" + headingNumber
					+ "' and user view '" + cataloguingView + "'");
		}

		return result;
	}

	/**
	 * loads the heading member from the database based on the settings of the
	 * key values
	 * 
	 */
	public Descriptor loadDescriptorForAutocomplete(int headingNumber,
			int cataloguingView) throws DataAccessException {
		return loadDescriptorForAutocomplete(headingNumber, cataloguingView,
				getPersistentClass());
	}

	public Descriptor loadDescriptorForAutocomplete(int headingNumber,
			int cataloguingView, Class persistentClass)
			throws DataAccessException {

		List l = find("from " + persistentClass.getName()
				+ " as hdg where hdg.key.headingNumber = ? "
				+ " AND substr(hdg.key.userViewString, ?, 1) = '1'",
				new Object[] { new Integer(headingNumber),
						new Integer(cataloguingView) }, new Type[] {
						Hibernate.INTEGER, Hibernate.INTEGER });

		Descriptor result = null;
		if (!l.isEmpty()) {
			result = (Descriptor) l.get(0);
			result = (Descriptor) isolateView(result, cataloguingView);
		} else {
			logger.warn("No descriptor of type '" + persistentClass.getName()
					+ "' found for heading number '" + headingNumber
					+ "' and user view '" + cataloguingView + "'");
			result = load(headingNumber);
			result.evict();
			result.getKey().setUserViewString(
					HibernateUtil.makeSingleViewString(cataloguingView));
			result.markNew();
			persistByStatus(result);
		}

		return result;
	}

	public Descriptor load(int headingNumber, Class persistentClass)
			throws DataAccessException {

		List l = find("from " + persistentClass.getName()
				+ " as hdg where hdg.key.headingNumber = ? ",
				new Object[] { new Integer(headingNumber) },
				new Type[] { Hibernate.INTEGER });

		Descriptor result = null;
		if (!l.isEmpty()) {
			result = (Descriptor) l.get(0);
		} else {
			logger.warn("No descriptor of type '" + persistentClass.getName()
					+ "' found for heading number '" + headingNumber);
		}

		return result;
	}

	/**
	 * Returns the first n rows having sortform > term
	 * 
	 */
	public List getHeadingsBySortform(String operator, String direction,
			String term, String filter, int searchingView, int count)
			throws DataAccessException {
		Session s = currentSession();

		List l = null;
		String viewClause;
		if (searchingView == View.AUTHORITY) {
			searchingView = 1;
		}
		if (searchingView == View.ANY) {
			viewClause = "";
		} else {
			viewClause = " and SUBSTR(hdg.key.userViewString, " + searchingView
					+ ", 1) = '1' ";
		}

		try {
			Query q = s.createQuery("from " + getPersistentClass().getName()
					+ " as hdg where hdg.sortForm " + operator + " :term "
					+ viewClause + filter + " order by hdg.sortForm "
					+ direction);
			q.setString("term", term);
			q.setMaxResults(count);

			l = q.list();
			l = isolateViewForList(l, searchingView);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	}

	/**
	 * Questo metodo è stato creato per gestire l'assenza della sortform. Il
	 * problema si presenta quando n intestazioni hanno la stessa sortform e si
	 * vogliono elencare meno di n elementi (maxResults<n, count<n) gli
	 * elementi tra maxResults ed n non verrebbero mai visulaizzati. Siccome
	 * questo evento si verifica soprattutto quando si cerca per searchTerm = " "
	 * è stato realizzata la ricerca ad hoc. Il searchTerm = " " viene creato
	 * nel salvataggio di una heading ogni qualvolta la creazione della sortform
	 * fallisce (vedi triggers sulle tabelle AMICUS.XXX_HDG" TODO _MIKE:
	 * Riportare in MADES TODO _MIKE: Gestire tutte le condizioni di sortform
	 * con n>maxResults e searchTerm <> " "
	 */
	public List getHeadingsByBlankSortform(String operator, String direction,
			String term, String filter, int cataloguingView, int count)
			throws DataAccessException {
		Session s = currentSession();

		List l = null;
		try {
			Query q = s.createQuery("from " + getPersistentClass().getName()
					+ " as hdg where hdg.sortForm = " + " :term  and "
					+ " SUBSTR(hdg.key.userViewString, :view, 1) = '1' "
					+ filter + " order by hdg.sortForm, hdg.stringText "
					+ direction);
			q.setString("term", " ");
			q.setInteger("view", cataloguingView);

			l = q.list();
			l = isolateViewForList(l, cataloguingView);
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return l;
	}


	public int getDocCount(Descriptor d, int searchingView)
			throws DataAccessException {
		List l;
		int result = 0;

		if (searchingView == View.ANY) { // pm 2011
			/*
			 * l = find( " select count(distinct apf.bibItemNumber) from " +
			 * d.getAccessPointClass().getName() + " as apf " + " where
			 * apf.headingNumber = ?", new Object[] { new
			 * Integer(d.getKey().getHeadingNumber()) }, new Type[] {
			 * Hibernate.INTEGER });
			 */
			/*
			 * pm - the above select count(distinct ...) doesn't seem to work in
			 * Hibernate. Replace with JDBC select.
			 */
			
			Connection con;
			PreparedStatement stmt = null;
			ResultSet rs = null;

			try {
				con = currentSession().connection();
				String cmd = " select count(distinct bib_itm_nbr) from  "
						+ getHibernateTableName(d.getAccessPointClass())
						+ " where "
						+ getHibernateColumnName(d.getAccessPointClass(),
								"headingNumber") + " = ? ";
				stmt = con.prepareStatement(cmd);
				logger.debug(cmd);
				stmt.setInt(1, d.getHeadingNumber());
				rs = stmt.executeQuery();
				rs.next();

				return rs.getInt(1);

			} catch (Exception e) {
				logAndWrap(e);
				return 0;
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
					}
				}
			}

		} else {
			// override of DAODescriptor subclasses
			l = find(" select count(distinct apf.bibItemNumber) from "
					+ d.getAccessPointClass().getName() + " as apf "
					+ " where apf.headingNumber = ? and "
					+ " substr(apf.userViewString, ?, 1) = '1'", new Object[] {
					new Integer(d.getKey().getHeadingNumber()),
					new Integer(searchingView) }, new Type[] {
					Hibernate.INTEGER, Hibernate.INTEGER });

			if (l.size() > 0) {
				result = ((Integer) l.get(0)).intValue();
			}
			return result;
		}
	}



	public List getDocList(Descriptor d, int searchingView)
			throws DataAccessException {
		List l = null;
		if (searchingView == View.ANY) { // pm 2011
			l = find(
					" select apf.bibItemNumber from "
							+ d.getAccessPointClass().getName() + " as apf "
							+ " where apf.headingNumber = ?",
					new Object[] { new Integer(d.getKey().getHeadingNumber()) },
					new Type[] { Hibernate.INTEGER });
		} else {
			l = find(" select apf.bibItemNumber from "
					+ d.getAccessPointClass().getName() + " as apf "
					+ " where apf.headingNumber = ? and "
					+ " substr(apf.userViewString, ?, 1) = '1'", new Object[] {
					new Integer(d.getKey().getHeadingNumber()),
					new Integer(searchingView) }, new Type[] {
					Hibernate.INTEGER, Hibernate.INTEGER });
		}
		return l;
	}

	public int getAuthCount(Descriptor d) throws DataAccessException {
		/*
		 * Currently, descriptors that support Authorities also contain a column
		 * containing the count of authorities. This column is redundant and was
		 * added for performance considerations. It is believed that the AMICUS
		 * client did not maintain this column correctly. Nevertheless, we will
		 * report the stringValue from this column rather than re-calculating from the
		 * database.
		 * 
		 * This method is implemented in the DAO because the other Browse counts
		 * are here and also because future implementations may wish to access
		 * the db to obtain this count.
		 */

		// paulm aut implement db based count
		if (supportsAuthorities()) {
			logger.debug("looking for aut count of hdg:"
					+ AuthorityCatalog.getAutTypeByDescriptorType(d
							.getCategory()) + " " + d.getHeadingNumber());
			List l = find("select count(*) from AUT as aut "
					+ " where aut.headingNumber = ? and "
					+ " aut.headingType = ?", new Object[] {
					new Integer(d.getHeadingNumber()),
					AuthorityCatalog
							.getAutTypeByDescriptorType(d.getCategory()) },
					new Type[] { Hibernate.INTEGER, Hibernate.STRING });
			logger.debug("result is: " + ((Integer) l.get(0)).intValue());
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	public void save(final Descriptor descriptor, int headingView)
			throws DataAccessException {
		int headingNumber = (new DAOSystemNextNumber())
				.getNextNumber(descriptor.getNextNumberKeyFieldCode());
		save(descriptor, headingNumber, headingView);
	}

	private void save(final Descriptor descriptor, int headingNumber,
			int headingView) throws DataAccessException {
		String myView = makeSingleViewString(headingView);
		descriptor.setKey(new DescriptorKey(headingNumber, myView));
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException {
				// logger.info("saving: " + descriptor);
				s.save(descriptor);
			}
		}.execute();
	}

	/*
	 * TODO _MIKE: this method is never called. Use headingView when it will be
	 * linked
	 */
	public void delete(int headingNumber, int headingView)
			throws DataAccessException {
		delete(load(headingNumber, headingView));
	}

	public void delete(final Descriptor descriptor) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException {
				// TODO check it because duplicate the heading (don't delete the
				// previous titleHeading)
				// TODO check if it is in TTL_ACS_PNT
				// logger.info("deleting: " + descriptor);
				s.delete(descriptor);
			}
		}.execute();
	}

	public void edit(final Descriptor descriptor) throws DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session s)
					throws HibernateException {
				// logger.info("updating: " + descriptor);
				s.update(descriptor);
			}
		}.execute();
	}

	public void persist(Descriptor descriptor) throws DataAccessException {
		if (descriptor.isNew()) {
			int headingNumber = (new DAOSystemNextNumber())
					.getNextNumber(descriptor.getNextNumberKeyFieldCode());
			descriptor.setKey(new DescriptorKey(headingNumber, descriptor
					.getKey().getUserViewString()));
			descriptor.setHeadingNumber(headingNumber);
		}
		if (descriptor.isChanged() && descriptor.changeAffectsCacheTable()) {
			persistByStatus(descriptor);
			updateCacheTable(descriptor);
		} else {
			persistByStatus(descriptor);
		}
	}

	/**
	 * Updates the cache table for each of the documents attached to the
	 * descriptor
	 * 
	 * @since 1.0
	 */
	public void updateCacheTable(Descriptor descriptor)
			throws DataAccessException {
		BibliographicCatalogDAO dao = new BibliographicCatalogDAO();
		int cataloguingView = View.toIntView(descriptor.getUserViewString());
		Iterator iter = getDocList(descriptor, cataloguingView).iterator();
		while (iter.hasNext()) {
			dao.updateCacheTable(((Integer) iter.next()).intValue(),
					cataloguingView);
		}
	}

	/**
	 * Allows individual dao's (especially publisher) to override the descriptor
	 * handling of sortform
	 * 
	 * @since 1.0
	 */
	public String getBrowsingSortForm(Descriptor d) {
		return d.getSortForm();
	}

	/**
	 * pm 2011 finds any existing Descriptor with the same key as the one
	 * selected but with the user view isolated to the given cataloguing view.
	 * If no such object exists, a new one is created by copying the original
	 * Descriptor object.
	 * 
	 * @param headingNumber
	 * @param recordViewString
	 * @param cataloguingView
	 * @throws DataAccessException
	 */
	public Descriptor findOrCreateMyView(int headingNumber,
			String recordViewString, int cataloguingView)
			throws DataAccessException {
		logger.debug("findOrCreateMyView(" + headingNumber + ", "
				+ recordViewString + ", " + cataloguingView + ")");
		// TODO PAUL Should this handle MADES as well?
		short onFileView = View.toIntView(recordViewString);
		Descriptor existing = load(headingNumber, cataloguingView);
		if (existing != null) {
			logger.debug("found heading in cataloguing view");
			return existing;
		} else {
			Descriptor onFile = load(headingNumber, onFileView);
			Descriptor newDesc = (Descriptor) deepCopy(onFile);
			newDesc.setUserViewString(View
					.makeSingleViewString(cataloguingView));
			logger.debug("creating new heading with view: "
					+ newDesc.getUserViewString());
			save(newDesc);
			return newDesc;
		}
	}

	/**
	 * Notice: In the past some sortforms included "extra" information to help
	 * in identifying uniqueness (some headings added a code for "language of
	 * access point" so that otherwise identical headings could be
	 * differentiated if they had different languages). Now, the only example I
	 * can think of is the Dewey Decimal classification where the sortform
	 * includes the Dewey Edition Number so that identical numbers from
	 * different editions will have different sortforms.
	 * 
	 * TODO _MIKE: differentiate/override for Dewey Decimal classification
	 * 
	 * @param d
	 * @return
	 * @throws DataAccessException
	 * @throws SortFormException
	 */
	public boolean isMatchingAnotherHeading(Descriptor d) {
		try {
			String sortForm = calculateSortForm(d);
			List l = currentSession().find(
					"select count(*) from " + getPersistentClass().getName()
							+ " as c "
							+ " where c.sortForm = ? and c.stringText = ? "
							+ " and c.key.userViewString = ?"
							+ " and c.key.headingNumber <> ?",
					new Object[] { sortForm, d.getStringText(),
							d.getUserViewString(),
							new Integer(d.getKey().getHeadingNumber()) },
					new Type[] { Hibernate.STRING, Hibernate.STRING,
							Hibernate.STRING, Hibernate.INTEGER });
			return ((Integer) l.get(0)).intValue() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public Descriptor getMatchingHeading(Descriptor d)
			throws DataAccessException {
		// regenerate sort form

		d.setSortForm(calculateSortForm(d));
		try {
			List l = currentSession().find(
					"from " + getPersistentClass().getName() + " as c "
							+ " where c.sortForm = ? and c.stringText = ? "
							+ " and c.key.userViewString = ? ",
					new Object[] { d.getSortForm(), d.getStringText(),
							d.getUserViewString() },
					new Type[] { Hibernate.STRING, Hibernate.STRING,
							Hibernate.STRING });
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see HibernateUtil#delete(librisuite.business
	 *      .common.Persistence)
	 */
	public void delete(Persistence p) throws
            DataAccessException {
		if (!(p instanceof Descriptor)) {
			throw new IllegalArgumentException(
					"I can only delete Descriptor objects");
		}
		Descriptor d = ((Descriptor) p);
		// check for access point references
		// pm aut -- not taking care of user views correctly (call getDocCount
		// instead)
		if (getDocCount(d, View.toIntView(d.getUserViewString())) > 0) {
			throw new ReferentialIntegrityException(d.getAccessPointClass()
					.getName(), d.getClass().getName());
		}

		// check for cross references
		if (supportsCrossReferences()) {
			if (getXrefCount(d, View.toIntView(d.getUserViewString())) > 0) {
				throw new ReferentialIntegrityException(d.getReferenceClass(
						d.getClass()).getName(), d.getClass().getName());
			}
		}
		// check for authorities
		if (supportsAuthorities()) {
			if (d.getAuthorityCount() > 0) {
				throw new ReferentialIntegrityException(d.getReferenceClass(
						d.getClass()).getName(), d.getClass().getName());
			}
		}

		// check for authority access point references
		if (getAuthorityApfReferenceCount(d) > 0) {
			throw new ReferentialIntegrityException("AUT_X_ACS_PNT", d
					.getClass().getName());
		}

		super.delete(p);
	}
	/**
	 * Counts the number of references to the given descriptor in the corresponding 
	 * Authority Access Point table
	 * @param d
	 * @return
	 * @throws DataAccessException
	 */
	public int getAuthorityApfReferenceCount(Descriptor d) throws DataAccessException {
		if (View.toIntView(d.getUserViewString()) != 1) {
			return 0;   //only view 1 has auth apf references
		}
		Class apf = d.getAuthorityAccessPointClass();
		if (apf == null) {
			return 0;
		}
		List l = find("from " + apf.getName() + " as apf where apf.headingNumber = ?",
				new Object[]{new Integer(d.getHeadingNumber())}, new Type[]{Hibernate.INTEGER});
		return l.size();
	}
	
	/**
	 * returns the number of cross references for the given descriptor in the
	 * given view
	 * 
	 * @param source --
	 *            The descriptor whose xref count is to be calculated
	 * @param cataloguingView --
	 *            the view to use for counting
	 * @return the count of cross references
	 */
	public int getXrefCount(Descriptor source, int cataloguingView)
			throws DataAccessException {
		List l = find("select count(*) from "
				+ source.getReferenceClass(source.getClass()).getName()
				+ " as ref where ref.key.source = ? and "
				+ " substr(ref.key.userViewString, ?, 1) = '1' ", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });
		if (l != null) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * gets the cross references for the given source and view.
	 * 
	 * @param source -
	 *            the descriptor whose references are to be retrieved
	 * @param cataloguingView -
	 *            the view to be used
	 * @return a list of cross references.
	 */
	public List getCrossReferences(Descriptor source, int cataloguingView)
			throws DataAccessException {

		return find("from "
				+ source.getReferenceClass(source.getClass()).getName()
				+ " as ref " + " where ref.key.source = ? "
				+ " AND substr(ref.key.userViewString, ?, 1) = '1' "
				+ " order by ref.key.target, ref.key.type", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });

	}

	/**
	 * gets the cross references for the given source and view.
	 * 
	 * @param source -
	 *            the descriptor whose references are to be retrieved
	 *            the view to be used
	 * @return a list of cross references.
	 */
	public List getCrossReferences(Descriptor source)
			throws DataAccessException {

		return find(
				"from " + source.getReferenceClass(source.getClass()).getName()
						+ " as ref " + " where ref.key.source = ? "
						+ " order by ref.key.target, ref.key.type",
				new Object[] { new Integer(source.getKey().getHeadingNumber()) },
				new Type[] { Hibernate.INTEGER });

	}

	public REF loadReference(Descriptor source, Descriptor target,
			short referenceType, int cataloguingView)
			throws DataAccessException {

		REF result = null;

		if (source.getClass() == target.getClass()) {
			List l = find("from "
					+ source.getReferenceClass(target.getClass()).getName()
					+ " as ref " + " where ref.key.source = ? AND "
					+ " ref.key.target = ? AND "
					+ " substr(ref.key.userViewString, ?, 1) = '1' AND "
					+ " ref.key.type = ?", new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(target.getKey().getHeadingNumber()),
					new Integer(cataloguingView), new Short(referenceType) },
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER,
							Hibernate.INTEGER, Hibernate.SHORT });
			if (l.size() == 1) {
				result = (REF) l.get(0);
				result = (REF) isolateView(result, cataloguingView);
			}
		}

		return result;
	}

	public Descriptor getSeeReference(Descriptor d, int cataloguingView)
			throws DataAccessException {

		if (supportsCrossReferences()) {

			List xRefs = getCrossReferences(d, cataloguingView);

			Iterator iter = xRefs.iterator();
			while (iter.hasNext()) {
				REF ref = (REF) iter.next();
				if (ReferenceType.isSee(ref.getType())) {
					return ref.getTargetDAO().load(ref.getTarget(),
							cataloguingView);
				}
			}
		}
		return d;
	}

	public String calculateSearchTerm(String term, String browseIndex)
			throws DataAccessException {
		String searchTerm;
		try {
			searchTerm = calculateSortForm(term, browseIndex);
		} catch (SortFormException e) {
			int lterm = term.getBytes().length;
			String newTerm = lterm > BrowseManager.SORTFORM_LENGTH ? term
					.substring(0, BrowseManager.MAX_SORTFORM_LENGTH) : term;
			try {
				searchTerm = calculateSortForm(newTerm, browseIndex);
			} catch (SortFormException e1) {
				searchTerm = " ";
			}
		}
		return searchTerm;
	}

	public REF getCrossReferencesWithLanguage(Descriptor source,
			int cataloguingView, short indexingLanguage)

	throws DataAccessException {

		REF result = null;
		List l = null;
		// int sourceHeadingNumber=0;
		// RICERCA PRIMA DEL SOURCE NEL TARGET
		// sourceHeadingNumber = getSourceHeadingNumberByTarget(source,
		// cataloguingView);
		if (source instanceof SBJCT_HDG) {
			l = find("select ref from "
					+ source.getReferenceClass(source.getClass()).getName()
					+ " as ref, " + source.getClass().getName() + " as hdg "
					+ " where ref.key.source = ? "
					+ " AND substr(ref.key.userViewString, ?, 1) = '1' "
					+ " AND ref.key.type=5 "
					+ " AND ref.key.target=hdg.key.headingNumber "
					+ " AND hdg.accessPointLanguage=? ",
					new Object[] {
							new Integer(source.getKey().getHeadingNumber()),
							new Integer(cataloguingView),
							new Short(indexingLanguage) }, new Type[] {
							Hibernate.INTEGER, Hibernate.INTEGER,
							Hibernate.SHORT });

		} else if (source instanceof NME_TTL_HDG) {
			l = find("select ref from "
					+ source.getReferenceClass(source.getClass()).getName()
					+ " as ref, " + "" + source.getClass().getName()
					+ " as hdg, " + " NME_HDG as nme, " + " TTL_HDG as ttl "
					+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
					+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
					+ " and ref.key.source = ? "
					+ " AND substr(ref.key.userViewString, ?, 1) = '1' "
					+ " AND ref.key.type=5 "
					+ " AND ref.key.target=hdg.key.headingNumber "
					+ " AND nme.indexingLanguage=? "
					+ " AND ttl.indexingLanguage=? ", new Object[] {
					new Integer(source.getKey().getHeadingNumber()),
					new Integer(cataloguingView), new Short(indexingLanguage),
					new Short(indexingLanguage) }, new Type[] {
					Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.SHORT,
					Hibernate.SHORT });
		} else {
			l = find("select ref from "
					+ source.getReferenceClass(source.getClass()).getName()
					+ " as ref, " + source.getClass().getName() + " as hdg "
					+ " where ref.key.source = ? "
					+ " AND substr(ref.key.userViewString, ?, 1) = '1' "
					+ " AND ref.key.type=5 "
					+ " AND ref.key.target=hdg.key.headingNumber "
					+ " AND hdg.accessPointLanguage=? ",
					new Object[] {
							new Integer(source.getKey().getHeadingNumber()),
							new Integer(cataloguingView),
							new Short(indexingLanguage) }, new Type[] {
							Hibernate.INTEGER, Hibernate.INTEGER,
							Hibernate.SHORT });

		}
		if (l.size() == 1) {
			result = (REF) l.get(0);
			result = (REF) isolateView(result, cataloguingView);
		} else {
			result = getSourceHeadingNumberByTarget(source, cataloguingView,
					indexingLanguage);
		}
		return result;

	}

	private REF getSourceHeadingNumberByTarget(Descriptor source,
			int cataloguingView, short indexingLanguage)
			throws DataAccessException {
		List l = null;
		List l2 = null;
		REF result = null;
		int targetHeadingNumber = 0;

		l2 = find("select ref from "
				+ source.getReferenceClass(source.getClass()).getName()
				+ " as ref, " + source.getClass().getName() + " as hdg "
				+ " where ref.key.source = ? "
				+ " AND substr(ref.key.userViewString, ?, 1) = '1' "
				+ " AND ref.key.target=hdg.key.headingNumber "
				+ " AND ref.key.type=5 ", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });

		if (l2.size() == 1) {
			targetHeadingNumber = ((REF) l2.get(0)).getTarget();
			l = find("select ref from "
					+ source.getReferenceClass(source.getClass()).getName()
					+ " as ref, " + source.getClass().getName() + " as hdg "
					+ " where ref.key.source = ? "
					+ " AND substr(ref.key.userViewString, ?, 1) = '1' "
					+ " AND ref.key.type=5 "
					+ " AND ref.key.target=hdg.key.headingNumber "
					+ " AND hdg.accessPointLanguage=? ",

					new Object[] { new Integer(targetHeadingNumber),
							new Integer(cataloguingView),
							new Short(indexingLanguage) }, new Type[] {
							Hibernate.INTEGER, Hibernate.INTEGER,
							Hibernate.SHORT });
			if (l.size() == 1) {
				result = (REF) l.get(0);
				result = (REF) isolateView(result, cataloguingView);
			}
		}
		return result;

	}

	/**
	 * gets the cross references for attribute the given source and view.
	 * 
	 * @param source -
	 *            the descriptor whose references are to be retrieved
	 * @param cataloguingView -
	 *            the view to be used
	 * @return a list of cross references.
	 */
	public List getCrossReferencesAttrib(Descriptor source, int cataloguingView)
			throws DataAccessException {

		return find("from THS_ATRIB " + " as ref "
				+ " where ref.key.source = ? "
				+ " AND substr(ref.key.userViewString, ?, 1) = '1' "
				+ " order by ref.key.target, ref.key.type", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });

	}

	/**
	 * gets the cross references for attribute the given source and view.
	 * 
	 * @param source -
	 *            the descriptor whose references are to be retrieved
	 *            the view to be used
	 * @return a list of cross references.
	 */
	public List getCrossReferencesAttrib(Descriptor source)
			throws DataAccessException {

		return find(
				"from THS_ATRIB " + " as ref " + " where ref.key.source = ? "
						+ " order by ref.key.target, ref.key.type",
				new Object[] { new Integer(source.getKey().getHeadingNumber()) },
				new Type[] { Hibernate.INTEGER });

	}

	public int getXAtrCount(Descriptor source, int cataloguingView)
			throws DataAccessException {
		List l = find("select count(*) from THS_ATRIB"

		+ " as ref where ref.key.source = ? and "
				+ " substr(ref.key.userViewString, ?, 1) = '1' ", new Object[] {
				new Integer(source.getKey().getHeadingNumber()),
				new Integer(cataloguingView) }, new Type[] { Hibernate.INTEGER,
				Hibernate.INTEGER });
		if (l != null) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * @return the count of the notes from this headingNumber access to THS_NTE
	 *         table
	 * @since 1.0
	 */

	public int getThesaurusNotesCount(Descriptor source, int cataloguingView)
			throws DataAccessException {
		List l = find("select count(*) from THS_NTE"

		+ " as ref where ref.key.headingNumber = ? ",

		new Object[] { new Integer(source.getKey().getHeadingNumber()) },
				new Type[] { Hibernate.INTEGER });
		if (l != null) {
			return ((Integer) l.get(0)).intValue();
		} else {
			return 0;
		}

	}

	public int getDocCountNT(Descriptor d, int searchingView)
			throws DataAccessException {

		int result = 0;
		List l = null;
		// discriminare count a seconda della heading se è per nomi o per
		// titoli
		if (d instanceof NME_HDG) {
			if (searchingView == View.ANY) {
				l = find(" select count(*) from  NME_TTL_HDG as hdg"
						+ " where hdg.nameHeadingNumber = ?",
						// +" and apf.headingNumber = hdg.key.headingNumber",
						new Object[] { new Integer(d.getKey()
								.getHeadingNumber()) },
						new Type[] { Hibernate.INTEGER });
			} else {
				l = find(" select count(*) from  NME_TTL_HDG as hdg"
						+ " where hdg.nameHeadingNumber = ? and "
						+ " substr(hdg.key.userViewString, ?, 1) = '1'",
				// +" and apf.headingNumber = hdg.key.headingNumber",
						new Object[] {
								new Integer(d.getKey().getHeadingNumber()),
								new Integer(searchingView) }, new Type[] {
								Hibernate.INTEGER, Hibernate.INTEGER });
			}
		} else if (d instanceof TTL_HDG) {
			if (searchingView == View.ANY) {
				l = find(" select count(*)from NME_TTL_HDG as hdg"
						+ " where hdg.titleHeadingNumber = ?",
						// +" and apf.headingNumber = hdg.key.headingNumber",
						new Object[] { new Integer(d.getKey()
								.getHeadingNumber()) },
						new Type[] { Hibernate.INTEGER });
			} else {
				l = find(" select count(*)from NME_TTL_HDG as hdg"
						+ " where hdg.titleHeadingNumber = ? and "
						+ " substr(hdg.key.userViewString, ?, 1) = '1'",
				// +" and apf.headingNumber = hdg.key.headingNumber",
						new Object[] {
								new Integer(d.getKey().getHeadingNumber()),
								new Integer(searchingView) }, new Type[] {
								Hibernate.INTEGER, Hibernate.INTEGER });
			}
		}
		if (l.size() > 0) {
			result = ((Integer) l.get(0)).intValue();
		}

		// }
		return result;
	}

	/**
	 * Whether the given descriptor duplicates the sortform of another
	 * descriptor in a different view
	 * 
	 * @param d
	 * @return
	 */
	public boolean hasMatchingSortformInAnotherView(Descriptor d) {
		try {
			String sortForm = calculateSortForm(d);
			List l = currentSession().find(
					"select count(*) from " + getPersistentClass().getName()
							+ " as c " + " where c.sortForm = ? "
							+ " and c.key.userViewString <> ?",
					new Object[] { sortForm, d.getUserViewString() },
					new Type[] { Hibernate.STRING, Hibernate.STRING });
			return ((Integer) l.get(0)).intValue() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param d
	 * @param searchingView
	 * @return returns an array of record numbers present in the table of the
	 *         control number and the titles in the search for ISSN numbers
	 * @throws DataAccessException
	 */
	public int[] getISSNAmicusNumbers(Descriptor d, int searchingView)
			throws DataAccessException {

		Connection con;
		PreparedStatement stmt = null;
		int amicusnumbers[] = null;
		ResultSet rs = null;
		try {

			String cmd = "select DISTINCT bib_itm_nbr l_bib_itm_nbr from cntl_nbr_acs_pnt where cntl_key_nbr = ? AND SUBSTR(usr_vw_ind,1 ,1) = '1' "
					+ " UNION select DISTINCT bib_itm_nbr from ttl_acs_pnt where ttl_srs_issn_nbr = ? AND SUBSTR(usr_vw_ind,1,1) = '1' order by 1 DESC";

			if (searchingView != View.ANY) {
				cmd = "select DISTINCT bib_itm_nbr l_bib_itm_nbr from cntl_nbr_acs_pnt where cntl_key_nbr = ? and substr(USR_VW_IND, '"
						+ searchingView
						+ "', 1) = '1' "
						+ " UNION select DISTINCT bib_itm_nbr from ttl_acs_pnt where ttl_srs_issn_nbr = ? and substr(USR_VW_IND, '"
						+ searchingView + "', 1) = '1' order by 1 DESC";
			}

			con = currentSession().connection();
			stmt = con.prepareStatement(cmd);
			logger.debug(cmd);
			stmt.setInt(1, d.getHeadingNumber());
			stmt.setInt(2, d.getHeadingNumber());
			rs = stmt.executeQuery();
			Vector<String> v = new Vector();
			while (rs.next()) {
				int bib_itm_nbr = rs.getInt(1);
				v.add("" + bib_itm_nbr);
			}
			amicusnumbers = new int[v.size()];
			for (int i = 0; i < v.size(); i++) {
				amicusnumbers[i] = Integer.parseInt(v.elementAt(i));
			}

		} catch (Exception e) {
			logAndWrap(e);

		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();

			} catch (SQLException e) {
				// do nothing
			}

		}
		return amicusnumbers;

	}

	public void saveDescriptor(Descriptor descriptor) {
		descriptor.markNew();
		try {
			persistByStatus(descriptor);
		} catch (DataAccessException e) {
			try {
				logAndWrap(e);
			} catch (DataAccessException e1) {
			}
		}
	}
	
	
	final static String SELECT_URI_BY_ALL_VIEW = "SELECT COUNT(*) FROM HDG_URI AS HDG WHERE HDG.headingNumber = ? AND HDG.headingTypeCode = ? ";
	final static String SELECT_URI_BY_ONE_VIEW = "SELECT COUNT(*) FROM HDG_URI AS HDG WHERE HDG.headingNumber = ? AND HDG.headingTypeCode = ? AND SUBSTR(HDG.userView, ?, 1) = '1'";
	
	/* Bug 5424 */
	@SuppressWarnings("unchecked")
	public int getDocCountURI(Descriptor d, int searchingView) throws DataAccessException 
	{
		int result = 0;
		List l = null;
		Integer headingType = Global.HEADING_TYPE_MAP.get(d.getCategory()+"");
		
		if (searchingView == View.ANY) {
			l = find(SELECT_URI_BY_ALL_VIEW,
					new Object[] {new Integer(d.getKey().getHeadingNumber()), headingType},
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER});
		} else {
			l = find(SELECT_URI_BY_ONE_VIEW,
					new Object[] {new Integer(d.getKey().getHeadingNumber()), headingType ,new Integer(searchingView) }, 
					new Type[] {Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER });
		}
		if (l.size() > 0) {
			result = ((Integer) l.get(0)).intValue();
		}
		return result;
	}
	
	/* Bug 5424 */
	@SuppressWarnings("unchecked")
	public List<HDG_URI> getDocUriList(Descriptor d, int searchingView) throws DataAccessException 
	{
		List<HDG_URI> list = null;
		
		Integer headingType = Global.HEADING_TYPE_MAP.get(d.getCategory()+"");
		
		if (searchingView == View.ANY) {
			list = find(SELECT_URI_BY_ALL_VIEW.replace("SELECT COUNT(*)", ""),
					new Object[] {new Integer(d.getKey().getHeadingNumber()), headingType},
					new Type[] { Hibernate.INTEGER, Hibernate.INTEGER});
		} else {
			list = find(SELECT_URI_BY_ONE_VIEW.replace("SELECT COUNT(*)", ""),
					new Object[] {new Integer(d.getKey().getHeadingNumber()), headingType,new Integer(searchingView) }, 
					new Type[] {Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER });
		}		
		return list;
	}
}