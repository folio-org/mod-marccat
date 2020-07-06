package org.folio.marccat.dao;


import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.codetable.IndexListElement;
import org.folio.marccat.dao.persistence.IndexList;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Provides data access to IDX_LIST table
 *
 * @author carment
 * @since 1.0
 */
public class IndexListDAO extends AbstractDAO {
  private static final String FROM_INDEX_LIST_AS_A = "from IndexList as a ";
  private static final String CODE_LIBRICAT = "' and a.codeLibriCatMades = 'LC'";
  private static final String LANGUAGE = " and a.key.language = '";


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
   * Gets the index by local abbreviation.
   *
   * @param session the session
   * @param s       the s
   * @param locale  the locale
   * @return the index by local abbreviation
   */
  @SuppressWarnings("unchecked")
  public IndexList getIndexByLocalAbbreviation(final Session session, String s, Locale locale) throws HibernateException{

    List<IndexList> l = session.find(FROM_INDEX_LIST_AS_A
      + "where lower(a.languageCode) = '" + s.toLowerCase() + "'"
      + LANGUAGE + locale.getISO3Language() + "'"
      + " and a.codeLibriCatMades = 'LC'");
    if (!l.isEmpty()) {
      return  l.get(0);
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
