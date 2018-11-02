package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.codetable.IndexListElement;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.persistence.IndexList;
import org.folio.marccat.dao.persistence.IndexListKey;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Provides data access to IDX_LIST table
 *
 * @author carment
 * @since 1.0
 */
public class DAOIndexList extends HibernateUtil {
  private static final Log logger = LogFactory.getLog(DAOIndexList.class);

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
      "from IndexList as a "
        + "where SUBSTR(a.browseCode, 1, 1) = 'B' "
        + "and a.key.language = '"
        + locale.getISO3Language()
        + "' and a.codeLibriCatMades = 'LC'"
        + " order by a.languageDescription";

    return getIndexBrowseByQuery(query, session);
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
      "from IndexList as a "
        + "where a.sortFormMainTypeCode = "
        + mainType
        + " and a.sortFormSubTypeCode = "
        + subType
        + " and a.key.language = '"
        + Locale.ENGLISH.getISO3Language()
        + "' and a.codeLibriCatMades = 'LC'";

    final List <IndexListElement> l = getIndexByQuery(query, session);
    if (l.size() > 0) {
      return (l.get(0)).getKey();
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
   * Returns the name of the code table used by the index
   *
   * @param session the session of hibernate
   * @param code    the index name, used here as a filter criterion.
   * @param locale  the Locale, used here as a filter criterion.
   * @return the name of the code table for index code associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public String getCodeTableName(final Session session, final String code, final Locale locale)
    throws HibernateException {
    String query =
      "select a.codeTableName from IndexList as a "
        + "where a.languageCode = '" + code + "'"
        + " and a.key.language = '" + locale.getISO3Language()
        + "' and a.codeLibriCatMades = 'LC'";
    final List <String> tableNameList = session.find(query);
    final Optional <String> firstElement = tableNameList.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? firstElement.get() : "";

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
          new Integer(ilk.getKeyNumber()),
          ilk.getTypeCode(),
          ilk.getLanguage()},
        new Type[]{
          Hibernate.INTEGER,
          Hibernate.STRING,
          Hibernate.STRING});
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

  public IndexList getIndexByLocalAbbreviation(final Session session, String s, Locale locale) throws DataAccessException {

    List l = find(session, "from IndexList as a "
      + "where lower(a.languageCode) = '" + s.toLowerCase() + "'"
      + " and a.key.language = '" + locale.getISO3Language() + "'"
      + " and a.codeLibriCatMades = 'LC'");
    if (l.size() > 0) {
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
  public List <IndexListElement> getIndexByQuery(final String query, final Session session) throws HibernateException {
    final List <IndexList> indexesList = session.find(query);
    return indexesList.stream().map(index -> {
      return new IndexListElement(
        index.getLanguageCode(),
        index.getLanguageDescription(),
        "" + index.getKey().getKeyNumber() + index.getKey().getTypeCode().trim());

    }).collect(Collectors.toList());
  }


  /**
   * Get the index browse e for a expecific query
   *
   * @param query
   * @param session
   * @throws HibernateException
   */
  @SuppressWarnings("unchecked")
  public List <Avp <String>> getIndexBrowseByQuery(final String query, final Session session) throws HibernateException {
    final List <IndexList> indexesList = session.find(query);
    return indexesList
      .stream()
      .map(index -> (Avp <String>) new Avp(index.getLanguageCode(), index.getLanguageDescription()))
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
  @Deprecated
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
      "from IndexList as a "
        + "where a.languageCode = "
        + "'" + indexAbbreviation + "'"
        + " and a.key.language = '"
        + locale.getISO3Language()
        + "' and a.codeLibriCatMades = 'LC'";

    List <IndexListElement> indexListElement = getIndexByQuery(query, session);
    final Optional <IndexListElement> firstElement = indexListElement.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? (firstElement.get()).getKey() : null;

  }
}
