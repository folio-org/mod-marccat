package org.folio.marccat.integration;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.GlobalStorage;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.dao.*;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.FULL_CACHE;
import org.folio.marccat.dao.persistence.T_SKP_IN_FLNG_CNT;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.integration.search.Parser;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.search.SearchResponse;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static org.folio.marccat.util.F.locale;

/**
 * Storage layer service.
 * This is the interface towards our storage service. Any R/W access to the persistence layer needs to pass through
 * this interface.
 *
 * @author cchiama
 * @author carment
 * @author nbianchini
 * @since 1.0
 */
public class StorageService implements Closeable {

  private static final Log logger = new Log(StorageService.class);


  private final Session session;

  /**
   * Builds a new {@link StorageService} with the given session.
   *
   * @param session the Hibernate session, which will be used for gathering a connection to the RDBMS.
   */
  StorageService(final Session session) {
    this.session = session;
  }

  /**
   * Returns the skip in filing associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the skip in filing associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List<Avp<String>> getSkipInFiling(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_SKP_IN_FLNG_CNT.class, locale(lang));
  }


  @Override
  public void close() throws IOException {
    try {
      session.close();
    } catch (final HibernateException exception) {
      throw new IOException(exception);
    }
  }


  /**
   * Returns the preferred view associated with the input data.
   *
   * @param itemNumber              the record identifier.
   * @param databasePreferenceOrder the database preference order (for choosing among views).
   * @return the preferred view associated with the input data.
   * @throws DataAccessException in case of data access failure.
   */
  public int getPreferredView(final int itemNumber, final int databasePreferenceOrder) throws DataAccessException {
    return new DAOCache().getPreferredView(session, itemNumber, databasePreferenceOrder);
  }

  /**
   * Sorts a given {@link SearchResponse} instance.
   * The sort actually happens at docids level, if some record has been already fetched in the input response, it will
   * be removed.
   *
   * @param rs         the search response.
   * @param attributes the sort attributes.
   * @param directions the sort orders.
   * @return a search response wrapping a docid array ordered according with the given criteria.
   * @throws DataAccessException in case of data access failure.
   */
  public SearchResponse sortResults(final SearchResponse rs, final String[] attributes, final String[] directions) throws DataAccessException {
    new DAOSortResultSets().sort(session, rs, attributes, directions);
    rs.clearRecords();
    return rs;
  }

  /**
   * Returns the content of a record associated with the given data.
   *
   * @param itemNumber    the record identifier.
   * @param searchingView the view.
   * @return the content of a record associated with the given data.
   * @throws RecordNotFoundException in case nothing is found.
   */
  public String getRecordData(final int itemNumber, final int searchingView) throws RecordNotFoundException {
    final FULL_CACHE cache = new DAOFullCache().load(session, itemNumber, searchingView);
    return cache.getRecordData();
  }

  /**
   * Find the {@link CatalogItem} associated with the given data.
   *
   * @param itemNumber    the record identifier.
   * @param searchingView the search view.
   * @return the {@link CatalogItem} associated with the given data.
   */
  public CatalogItem getCatalogItemByKey(final int itemNumber, final int searchingView) {
    switch (searchingView) {
      case View.AUTHORITY:
        return new AuthorityCatalogDAO().getCatalogItemByKey(session, itemNumber, searchingView);
      default:
        return new BibliographicCatalogDAO().getCatalogItemByKey(session, itemNumber, searchingView);
    }
  }

  /**
   * Generate a new keyNumber for keyFieldCodeValue specified.
   *
   * @param keyCodeValue -- the key code of field value.
   * @return nextNumber
   * @throws DataAccessException in case of data access exception.
   */
  public Integer generateNewKey(final String keyCodeValue) throws DataAccessException {
    try {
      SystemNextNumberDAO dao = new SystemNextNumberDAO();
      return dao.getNextNumber(keyCodeValue, session);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

  /**
   * Updates the full record cache table with the given item.
   *
   * @param item the catalog item.
   * @param view the related view.
   */
  public void updateFullRecordCacheTable(final CatalogItem item, final int view) {
    switch (view) {
      case View.AUTHORITY:
        new AuthorityCatalogDAO().updateFullRecordCacheTable(session, item);
        break;
      default:
        try {
          new BibliographicCatalogDAO().updateFullRecordCacheTable(session, item);
        } catch (final HibernateException exception) {
          throw new DataAccessException(exception);
        }
    }
  }

  /**
   * Executes a CCL query using the given data.
   *
   * @param cclQuery      the CCL query.
   * @param mainLibraryId the main library identifier.
   * @param locale        the current locale.
   * @param searchingView the target search view.
   * @return a list of docid matching the input query.
   */
  public List<Integer> executeQuery(final String cclQuery, final int mainLibraryId, final Locale locale, final int searchingView) {
    final Parser parser = new Parser(locale, mainLibraryId, searchingView, session);
    try (final Statement sql = stmt(connection());
         final ResultSet rs = executeQuery(sql, parser.parse(cclQuery))) {
      final ArrayList<Integer> results = new ArrayList<>();
      while (rs.next()) {
        results.add(rs.getInt(1));
      }

      logger.info(MessageCatalog._00023_SE_REQRES, cclQuery, results.size());

      return results;
    } catch (final HibernateException | SQLException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      return emptyList();
    }
  }

  /**
   * Returns a valid database connection associated with this service.
   *
   * @return a valid database connection associated with this service.
   * @throws HibernateException in case of data access failure.
   */
  public Connection connection() throws HibernateException {
    return session.connection();
  }

  /**
   * Creates a valid statement from the given connection.
   *
   * @param connection the database connection.
   * @return a valid statement from the given connection.
   */
  private Statement stmt(final Connection connection) {
    try {
      return connection.createStatement();
    } catch (final Exception exception) {
      throw new ModMarccatException(exception);
    }
  }

  /**
   * Internal method fo executing a SQL query.
   *
   * @param stmt  the statement.
   * @param query the SQL command.
   * @return the result of the query execution.
   */
  private ResultSet executeQuery(final Statement stmt, final String query) {
    try {
      return stmt.executeQuery(query);
    } catch (final Exception exception) {
      throw new ModMarccatException(exception);
    }
  }


  /**
   * returns the number of bibliographic records linked to an authority record
   *
   * @param id   the authority number, used here as a filter criterion.
   * @param view the view used here as filter criterion
   * @return the count of bibliographic records
   * @throws HibernateException
   */
  public CountDocument getCountDocumentByAutNumber(final int id, final int view) throws HibernateException {
    final CountDocument countDocument = new CountDocument();
    final AutDAO dao = new AutDAO();
    final AUT aut = dao.load(session, id);
    final Class accessPoint = GlobalStorage.BIBLIOGRAPHIC_ACCESS_POINT_CLASS_MAP.get(aut.getHeadingType());
    countDocument.setCountDocuments(dao.getDocCountByAutNumber(aut.getHeadingNumber(), accessPoint, view, session));
    countDocument.setQuery(GlobalStorage.INDEX_AUTHORITY_TYPE_MAP.get(aut.getHeadingType()) + " " + aut.getHeadingNumber());
    return countDocument;
  }

}
