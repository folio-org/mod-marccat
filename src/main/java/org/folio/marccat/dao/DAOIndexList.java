package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.codetable.IndexListElement;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.dao.persistence.IndexList;
import org.folio.marccat.dao.persistence.IndexListKey;
import org.folio.marccat.exception.DataAccessException;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Provides data access to IDX_LIST table
 *
 * @author carment
 * @since 1.0
 */
public class DAOIndexList extends AbstractDAO {
  private static final Log logger = LogFactory.getLog(DAOIndexList.class);
  private static final String FROM_INDEX_LIST_AS_A = "from IndexList as a ";
  private static final String CODE_LIBRICAT = "' and a.codeLibriCatMades = 'LC'";
  private static final String LANGUAGE = " and a.key.language = '";

  /**
   * Returns the browse indexes types associated to the given language.
   *
   * @param session the session of hibernate
   * @param locale  the Locale, used here as a filter criterion.
   * @return the browse indexes
   * @throws HibernateException
   */
  public List getBrowseIndex(final Locale locale, final Session session) throws HibernateException {
    final String query =
      FROM_INDEX_LIST_AS_A
        + "where SUBSTR(a.browseCode, 1, 1) = 'B' "
        + "and a.key.language = '"
        + locale.getISO3Language()
        + CODE_LIBRICAT
        + " order by a.languageDescription";

    return getIndexBrowseByQuery(query, session);
  }

  /**
   * Return the language independent (key) index value to be used when
   * browsing for entries of this type of Descriptor (e.g. Names ==
   * "2P0"). The value returned should correspond to the value of
   * IDX_LIST.IDX_LIST_KEY_NBR + IDX_LIST_TYPE_CDE
   *
   * @param mainType the main type, used here as a filter criterion.
   * @param subType  the sub type, used here as a filter criterion.
   * @param session  the session of hibernate
   * @return the index
   * @throws HibernateException
   */
  public String getIndexBySortFormType(final int mainType, final int subType, final Session session) throws HibernateException {
    final String query =
      FROM_INDEX_LIST_AS_A
        + "where a.sortFormMainTypeCode = "
        + mainType
        + " and a.sortFormSubTypeCode = "
        + subType
        + LANGUAGE
        + Locale.ENGLISH.getISO3Language()
        + CODE_LIBRICAT;

    final List<IndexListElement> l = getIndexByQuery(query, session);
    if (!l.isEmpty()) {
      return (l.get(0)).getKey();
    } else {
      return null;
    }
  }

  /**
   * Gets the index by english abreviation.
   *
   * @param s the s
   * @return the index by english abreviation
   */
  public String getIndexByEnglishAbreviation(String s) {

    String query =
      FROM_INDEX_LIST_AS_A
        + "where a.languageCode = "
        + "'" + s + "'"
        + LANGUAGE
        + Locale.ENGLISH.getISO3Language()
        + CODE_LIBRICAT;

    List l = getIndexByQuery(query);
    if (!l.isEmpty()) {
      return ((IndexListElement) l.get(0)).getKey();
    } else {
      return null;
    }
  }

  /**
   * Gets the sort form parameters by key.
   *
   * @param indexKey the index key
   * @param session  the session
   * @return the sort form parameters by key
   * @throws HibernateException the hibernate exception
   */
  public SortFormParameters getSortFormParametersByKey(final String indexKey, final Session session)
    throws HibernateException {
    SortFormParameters result = null;
    IndexListKey ilk = new IndexListKey(indexKey);
    List l =
      session.find(
        "from IndexList as t where t.key.keyNumber = ? "
          + " and trim(t.key.typeCode) = ? "
          + " and t.codeLibriCatMades = 'LC'"
          + " and t.key.language = ? ",
        new Object[]{
          ilk.getKeyNumber(),
          ilk.getTypeCode(),
          ilk.getLanguage()},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.STRING,
          Hibernate.STRING});
    if (!l.isEmpty()) {
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

  /**
   * Gets the index by local abbreviation.
   *
   * @param session the session
   * @param s       the s
   * @param locale  the locale
   * @return the index by local abbreviation
   */
  public IndexList getIndexByLocalAbbreviation(final Session session, String s, Locale locale) {

    List l = find(session, FROM_INDEX_LIST_AS_A
      + "where lower(a.languageCode) = '" + s.toLowerCase() + "'"
      + LANGUAGE + locale.getISO3Language() + "'"
      + " and a.codeLibriCatMades = 'LC'");
    if (!l.isEmpty()) {
      return (IndexList) l.get(0);
    } else {
      return null;
    }
  }

  /**
   * Get the IndexElementList for a expecific query
   *
   * @param query
   * @param session
   * @throws HibernateException
   */
  @SuppressWarnings("unchecked")
  public List<IndexListElement> getIndexByQuery(final String query, final Session session) throws HibernateException {
    final List<IndexList> indexesList = session.find(query);
    return indexesList.stream().map(index -> new IndexListElement(
      index.getLanguageCode(),
      index.getLanguageDescription(),
      "" + index.getKey().getKeyNumber() + index.getKey().getTypeCode().trim())
    ).collect(Collectors.toList());
  }


  /**
   * Get the index browse e for a expecific query
   *
   * @param query
   * @param session
   * @throws HibernateException
   */
  @SuppressWarnings("unchecked")
  public List<Avp<String>> getIndexBrowseByQuery(final String query, final Session session) throws HibernateException {
    final List<IndexList> indexesList = session.find(query);
    return indexesList
      .stream()
      .map(index -> (Avp<String>) new Avp(index.getLanguageCode(), index.getLanguageDescription()))
      .collect(toList());
  }

  /**
   * Get the IndexElementList for a expecific query
   *
   * @param query
   * @throws DataAccessException
   * @since 1.0
   */
  @SuppressWarnings("unchecked")

  public List getIndexByQuery(String query) {
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

    if (l != null) {
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
    }
    return result;
  }

  /**
   * Get the key for a specific index
   *
   * @param indexAbbreviation the abbreviation of index
   * @param session           the session of hibernate
   * @param locale            the Locale, used here as a filter criterion.
   * @return the key for index code associated with the requested language.
   * @throws HibernateException
   */
  public String getIndexByAbreviation(final String indexAbbreviation, final Session session, final Locale locale)
    throws HibernateException {
    String query =
      FROM_INDEX_LIST_AS_A
        + "where a.languageCode = "
        + "'" + indexAbbreviation + "'"
        + LANGUAGE
        + locale.getISO3Language()
        + CODE_LIBRICAT;

    List<IndexListElement> indexListElement = getIndexByQuery(query, session);
    final Optional<IndexListElement> firstElement = indexListElement.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? (firstElement.get()).getKey() : null;

  }
}
