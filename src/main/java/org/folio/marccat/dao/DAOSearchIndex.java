package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.searching.SearchIndexElement;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.persistence.IndexMain;
import org.folio.marccat.dao.persistence.IndexSub;
import org.folio.marccat.log.MessageCatalog;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author carment
 * @since 1.0
 */
public class DAOSearchIndex extends HibernateUtil {
  private static final Log logger = LogFactory.getLog(DAOSearchIndex.class);

  /**
   * Returns a list of all categories belonging to the requested type.
   *
   * @param session   the session of hibernate.
   * @param indexType the index type, used here as a filter criterion.
   * @param locale    the Locale, used here as a filter criterion.
   * @return a list of code and description for index code associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <Integer>> getIndexCategories(final Session session, final String indexType, final Locale locale) throws DataAccessException {

    try {
      final List <IndexMain> indices =
        session.find(
          "from IndexMain as a " +
            "where a.language = ? and a.indexType = '" + indexType + "' order by a.indexKey",
          new Object[]{locale.getISO3Language()}, new Type[]{Hibernate.STRING});
      return indices
        .stream()
        .map(index -> (Avp <Integer>) new Avp(index.getIndexValueCode(), index.getIndexMainName()))
        .collect(toList());

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }

  }

  /**
   * Returns returns a list of all indexes belonging to such category.
   *
   * @param session      the session of hibernate
   * @param indexType    the index type, used here as a filter criterion.
   * @param categoryCode the category code, used here as a filter criterion.
   * @param locale       the Locale, used here as a filter criterion.
   * @return a list of categories and descriptions for index type associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getIndexes(final Session session, final String indexType, final int categoryCode, final Locale locale) throws DataAccessException {
    try {
      final List <IndexSub> indexCategories =
        session.find(
          "from IndexSub as a " +
            "where a.language = ? and a.indexType = '" + indexType + "'" +
            "and a.indexValueCode = '" + categoryCode + "'" + " order by a.indexSubName",
          new Object[]{locale.getISO3Language()}, new Type[]{Hibernate.STRING});
      return indexCategories
        .stream()
        .map(indexCategory -> (Avp <String>) new Avp(indexCategory.getIndexSearchCode(), indexCategory.getIndexSubName()))
        .collect(toList());

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return Collections.emptyList();
    }

  }

  /**
   * Returns the description for index code.
   *
   * @param session the session of hibernate
   * @param code    the index, used here as a filter criterion.
   * @param locale  the Locale, used here as a filter criterion.
   * @return the description for index code associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public String getIndexDescription(final Session session, final String code, final Locale locale) throws DataAccessException {
    try {
      final List <IndexSub> index =
        session.find(
          "from IndexSub as a " +
            "where a.language = ? " +
            "and a.indexSearchCode = '" + code + "'" + " order by a.indexSubName",
          new Object[]{locale.getISO3Language()}, new Type[]{Hibernate.STRING});
      return index.stream().findFirst().get().getIndexSubName();

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return "";
    }

  }


  public List getSubIndex(Locale locale, char indexType) throws DataAccessException {
    List l = null;
    List result = new ArrayList();
    Session s = currentSession();

    try {
      l =
        s.find(
          "from IndexSub as a " +
            "where a.language = ? and a.indexType = '" + indexType + "'" +
            /*modifica barbara ordinamento indici*/
            " order by a.indexSubName",
          new Object[]{
            locale.getISO3Language()},
          new Type[]{Hibernate.STRING});
    } catch (HibernateException e) {
      logAndWrap(e);
    }

    Iterator iter = l.iterator();
    while (iter.hasNext()) {
      IndexSub aRow = (IndexSub) iter.next();
      result.add(
        new SearchIndexElement(
          aRow.getIndexValueCode(),
          aRow.getIndexSubValueCode(),
          aRow.getIndexSearchCode(),
          aRow.getIndexSubName()
        ));
    }
    return result;
  }
}
