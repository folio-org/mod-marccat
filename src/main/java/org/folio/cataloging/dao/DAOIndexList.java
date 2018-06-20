package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.codetable.IndexListElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.IndexList;
import org.folio.cataloging.dao.persistence.IndexListKey;
import org.folio.cataloging.log.MessageCatalog;

import java.util.*;

/**
 * Provides data access to IDX_LIST table
 *
 * @author carment
 * @since 1.0
 */
public class DAOIndexList extends HibernateUtil {
	private static final Log logger = LogFactory.getLog(DAOIndexList.class);

	public List getBrowseIndex(Locale locale) throws DataAccessException {
		final String query =
			"from IndexList as a "
				+ "where SUBSTR(a.browseCode, 0, 1) = 'B' "
				+ "and a.key.language = '"
				+ locale.getISO3Language()
				/*modifica Barbara 26/04/2007 - nella lista degli indici solo indici LC*/
				+ "' and a.codeLibriCatMades = 'LC'" 
				+ " order by a.languageDescription";

		return getIndexByQuery(query);
	}

	public List getEditorBrowseIndex(Locale locale) throws DataAccessException {
		final String query =
			"from IndexList as a "
				+ "where SUBSTR(a.browseCode, 0, 1) = 'B' "
				+ "and a.key.language = '"
				+ locale.getISO3Language()
				/*modifica Barbara 26/04/2007 - nella lista degli indici solo indici LC*/
				+ "' and a.codeLibriCatMades = 'LC'"
				+ "  and a.key.keyNumber in (230, 243)"
				+ " order by a.languageDescription";

		return getIndexByQuery(query);
	}
	
	public List getBrowseIndexPublisher(Locale locale) throws DataAccessException {
		final String query =
			"from IndexList as a "
				+ "where SUBSTR(a.browseCode, 0, 1) = 'B' "
				+ " and (a.languageCode = 'PP'"
				+ " or a.languageCode = 'PU')"
				+ "and a.key.language = '"
				+ locale.getISO3Language()
				/*modifica Barbara 26/04/2007 - nella lista degli indici solo indici LC*/
				+ "' and a.codeLibriCatMades = 'LC'" 
				+ " order by a.languageDescription";

		return getIndexByQuery(query);
	}

	public List getPrimaryIndex(Locale locale) throws DataAccessException {
		final String query =
			"from IndexList as a "
				+ "where SUBSTR(a.key.typeCode, 0, 1) = 'P' "
				+ "and a.key.language = '"
				+ locale.getISO3Language()
				+ "' and a.codeLibriCatMades = 'LC'";
		return getIndexByQuery(query);
	}

	public List getSecondaryIndex(Locale locale) throws DataAccessException {
		final List result = new ArrayList();

		final String query =
			"from IndexList as a "
				+ "where SUBSTR(a.key.typeCode, 0, 1) = 'S' "
				+ " and a.key.language = '"
				+ locale.getISO3Language()
				+ "' and a.codeLibriCatMades = 'LC'";

		return getIndexByQuery(query);
	}

	public String getIndexBySortFormType(final int mainType, final int subType) throws DataAccessException {

		String query =
			"from IndexList as a "
				+ "where a.sortFormMainTypeCode = "
				+ mainType
				+ " and a.sortFormSubTypeCode = "
				+ subType
				+ " and a.key.language = '"
				+ Locale.ENGLISH.getISO3Language()
				+ "' and a.codeLibriCatMades = 'LC'";

		List l = getIndexByQuery(query);
		if (l.size() > 0) {
			return ((IndexListElement) l.get(0)).getKey();
		} else {
			return null;
		}
	}

	public String getIndexByEnglishAbreviation(String s)
		throws DataAccessException {

		String query =
			"from IndexList as a "
				+ "where a.languageCode = "
				+ "'" + s + "'"
				+ " and a.key.language = '"
				+ Locale.ENGLISH.getISO3Language()
				+ "' and a.codeLibriCatMades = 'LC'";

		List l = getIndexByQuery(query);
		if (l.size() > 0) {
			return ((IndexListElement) l.get(0)).getKey();
		} else {
			return null;
		}
	}

	public String getLocalizedIndexByKey(String key, Locale locale)
		throws DataAccessException {
		IndexListKey ilk = new IndexListKey(key);
		String query =
			"from IndexList as a "
				+ "where a.key.keyNumber = "
				+ ilk.getKeyNumber()
				+ " and a.key.typeCode = "
				+ "'" + ilk.getTypeCode() + "'"
				+ " and a.key.language = "
				+ "'" + locale.getISO3Language() + "'"
				+ " and a.codeLibriCatMades = 'LC'";

		List l = getIndexByQuery(query);
		if (l.size() > 0) {
			return ((IndexListElement) l.get(0)).getValue();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * Get the IndexElementList for a expecific query
	 * 
	 * @param query
	 * @throws DataAccessException
	 * @since 1.0
	 */
	@SuppressWarnings("unchecked")
	public List getIndexByQuery(String query) throws DataAccessException {
		List l = null;
		List result = new ArrayList();
		Session s = currentSession();

		if (logger.isDebugEnabled()) {
			logger.debug("Doing query: " + query);
		}
		try {
			l = s.find(query);
		} catch (HibernateException e) {
			logAndWrap(e);
		}

		Iterator iter = l.iterator();
		while (iter.hasNext()) {
			IndexList aRow = (IndexList) iter.next();

			result.add(
				new IndexListElement(
					aRow.getLanguageCode(),
					aRow.getLanguageDescription(),
					""
						+ aRow.getKey().getKeyNumber()
						+ aRow.getKey().getTypeCode().trim()));

		}
		return result;
	}
	/**
	 * Returns the name of the code table used by the index
	 *
	 * @param session the session of hibernate
	 * @param code the index name, used here as a filter criterion.
	 * @param locale the Locale, used here as a filter criterion.
	 * @return the name of the code table for index code associated with the requested language.
	 * @throws DataAccessException in case of data access failure.
	 */
	public String getCodeTableName(final Session session, final String code, final Locale locale)
			throws DataAccessException {
		try {
			String query =
					"select a.codeTableName from IndexList as a "
							+ "where a.languageCode = '" + code + "'"
							+ " and a.key.language = '" + locale.getISO3Language()
							+ "' and a.codeLibriCatMades = 'LC'";
			final List<String> tableNameList = session.find(query);
			final Optional<String> firstElement = tableNameList.stream().filter(Objects::nonNull).findFirst();
			return firstElement.isPresent() ? firstElement.get() : "";
		} catch (final HibernateException exception) {
			logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			return "";
		}
	}

	public String getCodeTable(String key) throws DataAccessException {
	List codeTableList = new ArrayList();
	String codeTable = "";
	String query = "";

	query =
		"select a.codeTableName from IndexList as a "
			+ "where a.languageCode = '"
			+ key
			+ "' and a.codeLibriCatMades = 'LC'";

	try {
		codeTableList = currentSession().find(query);

		if (codeTableList.size() > 0) {
			if (codeTableList.get(0) != null) {
				codeTable = codeTableList.get(0).toString();
			}
		}

	} catch (HibernateException e) {
		logAndWrap(e);
	} catch (DataAccessException ae) {
		logAndWrap(ae);
	}

	return codeTable;
}

	
	public SortFormParameters getSortFormParametersByKey(String indexKey)
		throws DataAccessException {
		SortFormParameters result = null;
		IndexListKey ilk = new IndexListKey(indexKey);
		List l =
			find(
				"from IndexList as t where t.key.keyNumber = ? "
					+ " and trim(t.key.typeCode) = ? "
					+ " and t.codeLibriCatMades = 'LC'"
					+ " and t.key.language = ? ",
				new Object[] {
					new Integer(ilk.getKeyNumber()),
					ilk.getTypeCode(),
					ilk.getLanguage()},
				new Type[] {
					Hibernate.INTEGER,
					Hibernate.STRING,
					Hibernate.STRING });
		if (l.size() > 0) {
			IndexList i = (IndexList) l.get(0);
			result =
				new SortFormParameters(
					i.getSortFormMainTypeCode(),
					i.getSortFormSubTypeCode(),
					i.getSortFormTypeCode(),
					i.getSortFormFunctionCode(),
					i.getSortFormSkipInFiling());
		}
		return result;
	}
	
	public IndexList getIndexByLocalAbbreviation(String s, Locale locale) throws DataAccessException {

		List l = find("from IndexList as a " 
				+ "where lower(a.languageCode) = '" + s.toLowerCase() + "'"
				+ " and a.key.language = '" + locale.getISO3Language() + "'"				
				+ " and a.codeLibriCatMades = 'LC'");
		if (l.size() > 0) {
			return (IndexList)l.get(0);
		}
		else {
			return null;
		}
    }

}