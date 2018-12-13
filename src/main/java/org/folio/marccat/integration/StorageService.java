package org.folio.marccat.integration;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.folio.marccat.util.F.isNotNullOrEmpty;
import static org.folio.marccat.util.F.locale;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.folio.marccat.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicCatalog;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicItem;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.FixedField;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.cataloguing.common.CataloguingSourceTag;
import org.folio.marccat.business.cataloguing.common.ControlNumberTag;
import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.dao.AutDAO;
import org.folio.marccat.dao.AuthorityCatalogDAO;
import org.folio.marccat.dao.AuthorityModelDAO;
import org.folio.marccat.dao.BibliographicCatalogDAO;
import org.folio.marccat.dao.BibliographicCorrelationDAO;
import org.folio.marccat.dao.BibliographicModelDAO;
import org.folio.marccat.dao.BibliographicModelItemDAO;
import org.folio.marccat.dao.BibliographicValidationDAO;
import org.folio.marccat.dao.DAOCache;
import org.folio.marccat.dao.DAOCodeTable;
import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.DAOFullCache;
import org.folio.marccat.dao.DAOIndexList;
import org.folio.marccat.dao.DAOSortResultSets;
import org.folio.marccat.dao.NameTitleNameDescriptorDAO;
import org.folio.marccat.dao.PublisherDescriptorDAO;
import org.folio.marccat.dao.RecordTypeMaterialDAO;
import org.folio.marccat.dao.ShelfListDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.AccessPoint;
import org.folio.marccat.dao.persistence.AuthorityModel;
import org.folio.marccat.dao.persistence.BibliographicCorrelation;
import org.folio.marccat.dao.persistence.BibliographicLeader;
import org.folio.marccat.dao.persistence.BibliographicModel;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;
import org.folio.marccat.dao.persistence.BibliographicNoteType;
import org.folio.marccat.dao.persistence.BibliographicRelationType;
import org.folio.marccat.dao.persistence.BibliographicRelationshipTag;
import org.folio.marccat.dao.persistence.CasCache;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.ClassificationFunction;
import org.folio.marccat.dao.persistence.ClassificationType;
import org.folio.marccat.dao.persistence.ControlNumberFunction;
import org.folio.marccat.dao.persistence.ControlNumberType;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.FULL_CACHE;
import org.folio.marccat.dao.persistence.LDG_STATS;
import org.folio.marccat.dao.persistence.LOADING_MARC_RECORDS;
import org.folio.marccat.dao.persistence.MaterialDescription;
import org.folio.marccat.dao.persistence.Model;
import org.folio.marccat.dao.persistence.NameFunction;
import org.folio.marccat.dao.persistence.NameSubType;
import org.folio.marccat.dao.persistence.NameType;
import org.folio.marccat.dao.persistence.PhysicalDescription;
import org.folio.marccat.dao.persistence.PublisherManager;
import org.folio.marccat.dao.persistence.RecordTypeMaterial;
import org.folio.marccat.dao.persistence.SubjectFunction;
import org.folio.marccat.dao.persistence.SubjectSource;
import org.folio.marccat.dao.persistence.SubjectType;
import org.folio.marccat.dao.persistence.T_BIB_HDR;
import org.folio.marccat.dao.persistence.T_ITM_BIB_LVL;
import org.folio.marccat.dao.persistence.T_ITM_CCS;
import org.folio.marccat.dao.persistence.T_ITM_CNTL_TYP;
import org.folio.marccat.dao.persistence.T_ITM_DSCTV_CTLG;
import org.folio.marccat.dao.persistence.T_ITM_ENCDG_LVL;
import org.folio.marccat.dao.persistence.T_ITM_LNK_REC;
import org.folio.marccat.dao.persistence.T_ITM_REC_STUS;
import org.folio.marccat.dao.persistence.T_ITM_REC_TYP;
import org.folio.marccat.dao.persistence.T_NME_TTL_FNCTN;
import org.folio.marccat.dao.persistence.T_SKP_IN_FLNG_CNT;
import org.folio.marccat.dao.persistence.TitleAccessPoint;
import org.folio.marccat.dao.persistence.TitleFunction;
import org.folio.marccat.dao.persistence.TitleSecondaryFunction;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.InvalidBrowseIndexException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.exception.RecordInUseException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.integration.record.BibliographicInputFile;
import org.folio.marccat.integration.record.RecordParser;
import org.folio.marccat.integration.search.Parser;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.resources.domain.BibliographicRecord;
import org.folio.marccat.resources.domain.ContainerRecordTemplate;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.shared.CodeListsType;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.shared.MapHeading;
import org.folio.marccat.shared.Validation;
import org.folio.marccat.util.F;
import org.folio.marccat.util.StringText;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;


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
  private final static Map <Integer, Class> FIRST_CORRELATION_HEADING_CLASS_MAP = new HashMap <>();

  static {
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(1, T_BIB_HDR.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(2, NameType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(17, NameType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(3, TitleFunction.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(22, TitleFunction.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(4, SubjectType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(18, SubjectType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(5, ControlNumberType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(19, ControlNumberType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(6, ClassificationType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(20, ClassificationType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(7, BibliographicNoteType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(8, BibliographicRelationType.class);
    FIRST_CORRELATION_HEADING_CLASS_MAP.put(11, T_NME_TTL_FNCTN.class);
  }

  private final static Map <Integer, Class> SECOND_CORRELATION_CLASS_MAP = new HashMap <>();
  static {
    SECOND_CORRELATION_CLASS_MAP.put(2, NameSubType.class);
    SECOND_CORRELATION_CLASS_MAP.put(3, TitleSecondaryFunction.class);
    SECOND_CORRELATION_CLASS_MAP.put(4, SubjectFunction.class);
    SECOND_CORRELATION_CLASS_MAP.put(5, ControlNumberFunction.class);
    SECOND_CORRELATION_CLASS_MAP.put(6, ClassificationFunction.class);
    SECOND_CORRELATION_CLASS_MAP.put(11, NameType.class);
  }

  private final static Map <Integer, Class> THIRD_CORRELATION_HEADING_CLASS_MAP = new HashMap <>();
  static {
    THIRD_CORRELATION_HEADING_CLASS_MAP.put(2, NameFunction.class);
    THIRD_CORRELATION_HEADING_CLASS_MAP.put(4, SubjectSource.class);
    THIRD_CORRELATION_HEADING_CLASS_MAP.put(11, NameSubType.class);
  }

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
   * Returns a list of {@link Avp} which represents a short version of the available bibliographic templates.
   *
   * @return a list of {@link Avp} which represents a short version of the available bibliographic templates.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <Integer>> getBibliographicRecordTemplates() throws DataAccessException {
    final BibliographicModelDAO dao = new BibliographicModelDAO();
    try {
      return dao.getBibliographicModelList(session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Return a Authority Record Template by id
   *
   * @param id the record template id.
   * @throws DataAccessException in case of data access failure.
   */
  public RecordTemplate getAuthorityRecordRecordTemplatesById(final Integer id) throws DataAccessException {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(
        new AuthorityModelDAO().load(id, session).getRecordFields(),
        RecordTemplate.class);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IOException exception) {
      logger.error(MessageCatalog._00013_IO_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Save the new Authority record template.
   *
   * @param template the record template.
   * @throws DataAccessException in case of data access failure.
   */
  //todo: add second and third value wemi flag: consider if use record template also for authority
  public void saveAuthorityRecordTemplate(final RecordTemplate template) throws DataAccessException {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final AuthorityModelDAO dao = new AuthorityModelDAO();
      final AuthorityModel model = new AuthorityModel();
      model.setLabel(template.getName());
      model.setFrbrFirstGroup(template.getGroup());
      model.setRecordFields(mapper.writeValueAsString(template));
      dao.save(model, session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final JsonProcessingException exception) {
      logger.error(MessageCatalog._00013_IO_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Deletes a Bibliographic record template.
   *
   * @param id the record template id.
   * @throws DataAccessException in case of data access failure.
   */
  public void deleteBibliographicRecordTemplate(final String id) throws DataAccessException {
    try {
      final BibliographicModelDAO dao = new BibliographicModelDAO();
      final Model model = dao.load(Integer.valueOf(id), session);
      dao.delete(model, session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Delete an Authority record template.
   *
   * @param id the record template id.
   * @throws DataAccessException in case of data access failure.
   */
  public void deleteAuthorityRecordTemplate(final String id) throws DataAccessException {
    try {
      final AuthorityModelDAO dao = new AuthorityModelDAO();
      final Model model = dao.load(Integer.valueOf(id), session);
      dao.delete(model, session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Update the Bibliographic Record Template.
   *
   * @param template the record template.
   * @throws DataAccessException in case of data access failure.
   */
  public void updateBibliographicRecordTemplate(final RecordTemplate template) throws DataAccessException {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final BibliographicModelDAO dao = new BibliographicModelDAO();
      final BibliographicModel model = new BibliographicModel();
      model.setId(template.getId());
      model.setLabel(template.getName());
      model.setFrbrFirstGroup(template.getGroup());
      model.setRecordFields(mapper.writeValueAsString(template));
      dao.update(model, session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final JsonProcessingException exception) {
      logger.error(MessageCatalog._00013_IO_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Update the Authority Record Template.
   *
   * @param template the record template.
   * @throws DataAccessException in case of data access failure.
   */
  public void updateAuthorityRecordTemplate(final RecordTemplate template) throws DataAccessException {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final AuthorityModelDAO dao = new AuthorityModelDAO();
      final AuthorityModel model = new AuthorityModel();
      model.setId(template.getId());
      model.setLabel(template.getName());
      model.setFrbrFirstGroup(template.getGroup());
      model.setRecordFields(mapper.writeValueAsString(template));
      dao.update(model, session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final JsonProcessingException exception) {
      logger.error(MessageCatalog._00013_IO_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Save the new Bibliographic Record Template.
   *
   * @param template the record template.
   * @throws DataAccessException in case of data access failure.
   */
  public void saveBibliographicRecordTemplate(final RecordTemplate template) throws DataAccessException {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final BibliographicModelDAO dao = new BibliographicModelDAO();
      final BibliographicModel model = new BibliographicModel();
      model.setLabel(template.getName());
      model.setFrbrFirstGroup(template.getGroup());
      model.setRecordFields(mapper.writeValueAsString(template));
      dao.save(model, session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final JsonProcessingException exception) {
      logger.error(MessageCatalog._00013_IO_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Return a Bibliographic Record Template by id.
   *
   * @param id the record template id.
   * @return the bibliographic record template associated with the given id.
   * @throws DataAccessException in case of data access failure.
   */
  public RecordTemplate getBibliographicRecordRecordTemplatesById(final Integer id) throws DataAccessException {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(
        new BibliographicModelDAO().load(id, session).getRecordFields(),
        RecordTemplate.class);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IOException exception) {
      logger.error(MessageCatalog._00013_IO_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Load records from files uploaded.
   *
   * @param file            -- the current file.
   * @param startRecord     -- the number start record.
   * @param numberOfRecords -- the number of records to load.
   * @param view            -- the cataloguing view associated.
   * @return map with loading result.
   */
  public Map <String, Object> loadRecords(final MultipartFile file, final int startRecord, final int numberOfRecords,
                                          final int view, final Map <String, String> configuration) {
    final Map <String, Object> result = new HashMap <>();
    List <Integer> ids = new ArrayList <>();
    try {
      if (!file.isEmpty()) {
        final InputStream input = file.getInputStream();
        final BibliographicInputFile bf = new BibliographicInputFile();
        bf.loadFile(input, file.getOriginalFilename(), view, startRecord, numberOfRecords, session, configuration);

        final DAOCodeTable dao = new DAOCodeTable();
        final LDG_STATS stats = dao.getStats(session, bf.getLoadingStatisticsNumber());
        if (stats.getRecordsAdded() > 0) {
          final List <LOADING_MARC_RECORDS> lmr = (dao.getResults(session, bf.getLoadingStatisticsNumber()));
          ids = lmr.stream().map(l -> l.getBibItemNumber()).collect(Collectors.toList());
        }
        result.put(Global.LOADING_FILE_FILENAME, file.getName());
        result.put(Global.LOADING_FILE_IDS, ids);
        result.put(Global.LOADING_FILE_REJECTED, stats.getRecordsRejected());
        result.put(Global.LOADING_FILE_ADDED, stats.getRecordsAdded());
        result.put(Global.LOADING_FILE_ERRORS, stats.getErrorCount());

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return result;
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
   * Returns a list of {@link Avp} which represents a short version of the available bibliographic templates.
   *
   * @return a list of {@link Avp} which represents a short version of the available bibliographic templates.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <Integer>> getAuthorityRecordTemplates() throws DataAccessException {
    final AuthorityModelDAO dao = new AuthorityModelDAO();
    try {
      return dao.getAuthorityModelList(session);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
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
    final Class accessPoint = Global.BIBLIOGRAPHIC_ACCESS_POINT_CLASS_MAP.get(aut.getHeadingType());
    countDocument.setCountDocuments(dao.getDocCountByAutNumber(aut.getHeadingNumber(), accessPoint, view, session));
    countDocument.setQuery(Global.INDEX_AUTHORITY_TYPE_MAP.get(aut.getHeadingType()) + " " + aut.getHeadingNumber());
    return countDocument;
  }

  /**
   * Return a list of headings for a specific a search query in the first browse
   *
   * @param query       the query used here as filter criterion
   * @param view        the view used here as filter criterion
   * @param mainLibrary the main library used here as filter criterion
   * @param pageSize    the page size used here as filter criterion
   * @param lang        the lang used here as filter criterion
   * @return a list of headings
   * @throws DataAccessException
   * @throws InvalidBrowseIndexException
   */
  public List<MapHeading> getFirstPage(final String query, final int view, final int mainLibrary, final int pageSize, final String lang) throws DataAccessException, InvalidBrowseIndexException {
    String key = null;
    try {
      String index = null;
      String browseTerm = null;
      final List<Descriptor> descriptorsList;
      final DAOIndexList daoIndex = new DAOIndexList();
      if (query != null) {
        index = query.substring(0, query.indexOf((" ")));
        index = F.fixedCharPadding(index, 9).toUpperCase();
        browseTerm = query.substring(query.indexOf((" "))).trim();
      }
      key = daoIndex.getIndexByAbreviation(index, session, locale(lang));
      final Class c = Global.DAO_CLASS_MAP.get(key);
      if (c == null) {
        logger.error(MessageCatalog._00119_DAO_CLASS_MAP_NOT_FOUND, key);
        return Collections.emptyList();
      }
      final DAODescriptor dao = (DAODescriptor) c.newInstance();
      String filter = Global.FILTER_MAP.get(key);
      if (dao instanceof ShelfListDAO) {
        filter += " and hdg.mainLibraryNumber = " + mainLibrary;
      }
      browseTerm = dao.calculateSearchTerm(browseTerm, key, session);

      descriptorsList = dao.getHeadingsBySortform("<", "desc", browseTerm, filter, view, 1, session);
      if (!(dao instanceof PublisherDescriptorDAO)) {
        if (descriptorsList.size() > 0) {
          browseTerm = dao.getBrowsingSortForm(descriptorsList.get(0));
          descriptorsList.clear();
        }
      }
      descriptorsList.addAll(dao.getHeadingsBySortform(">=", "", browseTerm, filter, view, pageSize, session));
      return getMapHeadings(view, descriptorsList, dao);

    } catch (final SQLException | HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IllegalAccessException | InstantiationException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new InvalidBrowseIndexException(key);
    }
  }

  /**
   * Return a list of headings for a specific a search query in the next browse
   *
   * @param query       the query used here as filter criterion
   * @param view        the view used here as filter criterion
   * @param mainLibrary the main library used here as filter criterion
   * @param pageSize    the page size used here as filter criterion
   * @param lang        the lang used here as filter criterion
   * @return a list of headings
   * @throws DataAccessException
   * @throws InvalidBrowseIndexException
   */
  public List<MapHeading> getNextPage(final String query, final int view, final int mainLibrary, final int pageSize, final String lang) {
    String key = null;
    try {
      String index = null;
      String browseTerm = null;
      final List<Descriptor> descriptorsList;
      final DAOIndexList daoIndex = new DAOIndexList();
      String operator = ">";
      if (query != null) {
        index = query.substring(0, query.indexOf((" ")));
        index = F.fixedCharPadding(index, 9).toUpperCase();
        browseTerm = query.substring(query.indexOf((" "))).trim();
      }

      key = daoIndex.getIndexByAbreviation(index, session, locale(lang));
      final Class c = Global.DAO_CLASS_MAP.get(key);
      if (c == null) {
        logger.error(MessageCatalog._00119_DAO_CLASS_MAP_NOT_FOUND, key);
        return Collections.emptyList();
      }
      final DAODescriptor dao = (DAODescriptor) c.newInstance();
      String filter = Global.FILTER_MAP.get(key);
      if (dao instanceof ShelfListDAO) {
        filter = filter + " and hdg.mainLibraryNumber = " + mainLibrary;
      }
      browseTerm = dao.calculateSearchTerm(browseTerm, key, session);
      if (dao instanceof PublisherDescriptorDAO || dao instanceof NameTitleNameDescriptorDAO)
        operator = ">=";
      descriptorsList = dao.getHeadingsBySortform(operator, "", browseTerm, filter, view, pageSize, session);
      return getMapHeadings(view, descriptorsList, dao);


    } catch (final HibernateException | SQLException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IllegalAccessException | InstantiationException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new InvalidBrowseIndexException(key);
    }
  }

  /**
   * Return a list of headings for a specific a search query in the previous browse
   *
   * @param query       the query used here as filter criterion
   * @param view        the view used here as filter criterion
   * @param mainLibrary the main library used here as filter criterion
   * @param pageSize    the page size used here as filter criterion
   * @param lang        the lang used here as filter criterion
   * @return a list of headings
   * @throws DataAccessException
   * @throws InvalidBrowseIndexException
   */
  public List<MapHeading> getPreviousPage(final String query, final int view, final int mainLibrary, final int pageSize, final String lang) {
    String key = null;
    try {
      String index = null;
      String browseTerm = null;
      final List<Descriptor> descriptorsList;
      final DAOIndexList daoIndex = new DAOIndexList();
      String operator = "<";
      if (query != null) {
        index = query.substring(0, query.indexOf((" ")));
        index = F.fixedCharPadding(index, 9).toUpperCase();
        browseTerm = query.substring(query.indexOf((" ")), query.length()).trim();
      }

      key = daoIndex.getIndexByAbreviation(index, session, locale(lang));
      final Class c = Global.DAO_CLASS_MAP.get(key);
      if (c == null) {
        logger.error(MessageCatalog._00119_DAO_CLASS_MAP_NOT_FOUND, key);
        return Collections.emptyList();
      }
      final DAODescriptor dao = (DAODescriptor) c.newInstance();
      String filter = Global.FILTER_MAP.get(key);
      if (dao instanceof ShelfListDAO) {
        filter = filter + " and hdg.mainLibraryNumber = " + mainLibrary;
      }
      browseTerm = dao.calculateSearchTerm(browseTerm, key, session);
      if (dao instanceof PublisherDescriptorDAO || dao instanceof NameTitleNameDescriptorDAO)
        operator = "<=";
      descriptorsList = dao.getHeadingsBySortform(operator, "desc", browseTerm, filter, view, pageSize, session);
      List<MapHeading> mapHeading = getMapHeadings(view, descriptorsList, dao);
      Collections.reverse(mapHeading);
      return mapHeading;

    } catch (final SQLException | HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IllegalAccessException | InstantiationException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new InvalidBrowseIndexException(key);
    }
  }
  
  /**
   * Returns the codes list associated with the given language and key.
   *
   * @param lang         the language code, used here as a filter criterion.
   * @param codeListType the code list type key.
   * @return a list of code / description tuples representing the date type associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getCodesList(final String lang, final CodeListsType codeListType) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, Global.MAP_CODE_LISTS.get(codeListType.toString()), locale(lang));
  }

  /**
   * Return a complete heading map with the data of the heding number, the text to display, the authority count,
   * the count of documents, the count of name titles
   *
   * @param view
   * @param descriptorsList
   * @param dao
   * @return a map headings
   */

  private List<MapHeading> getMapHeadings(int view, List<Descriptor> descriptorsList, DAODescriptor dao) throws DataAccessException {
    return descriptorsList.stream().map(heading -> {
      final MapHeading headingObject = new MapHeading();
      try {
        headingObject.setHeadingNumber(heading.getHeadingNumber());
        headingObject.setStringText(heading.getDisplayText());
        headingObject.setCountAuthorities(heading.getAuthorityCount());
        headingObject.setCountDocuments(dao.getDocCount(heading, view, session));
        headingObject.setCountTitleNameDocuments(dao.getDocCountNT(heading, view, session));
      } catch (HibernateException exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        throw new DataAccessException(exception);
      }
      return headingObject;
    }).collect(Collectors.toList());
  }


  /**
   * Gets the material type information.
   * Used for 006 field.
   *
   * @param headerCode the header code used here as filter criterion.
   * @param code       the tag number code used here as filter criterion.
   * @return a string representing form of material.
   * @throws DataAccessException in case of data access failure.
   */
  public Map <String, Object> getMaterialTypeInfosByHeaderCode(final int headerCode, final String code) throws DataAccessException {

    final Map <String, Object> mapRecordTypeMaterial = new HashMap <>();
    final RecordTypeMaterialDAO dao = new RecordTypeMaterialDAO();
    try {
      return ofNullable(dao.getDefaultTypeByHeaderCode(session, headerCode, code))
        .map(rtm -> {
          mapRecordTypeMaterial.put(Global.FORM_OF_MATERIAL_LABEL, rtm.getAmicusMaterialTypeCode());
          mapRecordTypeMaterial.put(Global.MATERIAL_TYPE_CODE_LABEL, rtm.getRecordTypeCode());

          return mapRecordTypeMaterial;
        }).orElse(null);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Return a correlation values for a specific tag number.
   *
   * @param category   the category code used here as filter criterion.
   * @param indicator1 the first indicator used here as filter criterion.
   * @param indicator2 the second indicator used here as filter criterion.
   * @param code       the tag number code used here as filter criterion.
   * @return correlation values
   * @throws DataAccessException in case of data access failure.
   */
  public CorrelationValues getCorrelationVariableField(final Integer category,
                                                       final String indicator1,
                                                       final String indicator2,
                                                       final String code) throws DataAccessException {
    final BibliographicCorrelationDAO bibliographicCorrelationDAO = new BibliographicCorrelationDAO();
    try {
      return ofNullable(
        bibliographicCorrelationDAO.getBibliographicCorrelation(
          session, code, indicator1.charAt(0), indicator2.charAt(0), category))
        .map(BibliographicCorrelation::getValues).orElse(null);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Gets Validation for variable field.
   * Validation is related to sub-fields: valid, mandatory and default subfield
   *
   * @param marcCategory the marc category used here as filter criterion.
   * @param code1        the first correlation used here as filter criterion.
   * @param code2        the second correlation used here as filter criterion.
   * @param code3        the third correlation used here as filter criterion.
   * @return Validation object containing subfield list.
   */
  public Validation getSubfieldsByCorrelations(final int marcCategory,
                                               final int code1,
                                               final int code2,
                                               final int code3) throws DataAccessException {
    final BibliographicValidationDAO daoBibliographicValidation = new BibliographicValidationDAO();
    try {
      final CorrelationValues correlationValues = new CorrelationValues(code1, code2, code3);
      return daoBibliographicValidation.load(session, marcCategory, correlationValues);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Returns the record types associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the record type associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getRecordTypes(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_REC_TYP.class, locale(lang));
  }

  /**
   * Returns the encoding levels associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the encoding level associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getEncodingLevels(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_ENCDG_LVL.class, locale(lang));
  }
  
  /**
   * Gets the Material description information.
   * The values depend on mtrl_dsc and bib_itm data (leader).
   *
   * @param recordTypeCode     the record type code (leader 05) used here as filter criterion.
   * @param bibliographicLevel the bibliographic level (leader 06) used here as filter criterion.
   * @param code               the tag number code used here as filter criterion.
   * @return a map with RecordTypeMaterial info.
   * @throws DataAccessException in case of data access failure.
   */
  public Map <String, Object> getMaterialTypeInfosByLeaderValues(final char recordTypeCode, final char bibliographicLevel, final String code) throws DataAccessException {

    final RecordTypeMaterialDAO dao = new RecordTypeMaterialDAO();

    try {
      final Map <String, Object> mapRecordTypeMaterial = new HashMap <>();
      final RecordTypeMaterial rtm = dao.getMaterialHeaderCode(session, recordTypeCode, bibliographicLevel);

      mapRecordTypeMaterial.put(Global.HEADER_TYPE_LABEL, (code.equals(Global.MATERIAL_TAG_CODE) ? rtm.getBibHeader008() : rtm.getBibHeader006()));
      mapRecordTypeMaterial.put(Global.FORM_OF_MATERIAL_LABEL, rtm.getAmicusMaterialTypeCode());
      return mapRecordTypeMaterial;
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  
  /**
   * Returns the multipart resource level associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the multipart resource level associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getMultipartResourceLevels(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_LNK_REC.class, locale(lang));
  }
  
  /**
   * Returns the descriptive catalog forms associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the descriptive catalog forms associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getDescriptiveCatalogForms(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_DSCTV_CTLG.class, locale(lang));
  }
  
  /**
   * Returns the bibliographic levels associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the bibliographic level associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getBibliographicLevels(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_BIB_LVL.class, locale(lang));
  }
  
  /**
   * Returns the character encoding schemas associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the character encoding schema associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getCharacterEncodingSchemas(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_CCS.class, locale(lang));
  }
  
  /**
   * Returns the control types associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the control type associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getControlTypes(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_CNTL_TYP.class, locale(lang));
  }

  
  /**
   * Returns the record status types associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the record status type associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List <Avp <String>> getRecordStatusTypes(final String lang) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getList(session, T_ITM_REC_STUS.class, locale(lang));
  }
  
  /**
   * Returns the description for heading type entity.
   *
   * @param code     the heading marc category code, used here as a filter criterion.
   * @param lang     the language code, used here as a filter criterion.
   * @param category the category field.
   * @return the description for index code associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public String getHeadingTypeDescription(final int code, final String lang, final int category) throws DataAccessException {
    final DAOCodeTable dao = new DAOCodeTable();
    return dao.getLongText(session, code, FIRST_CORRELATION_HEADING_CLASS_MAP.get(category), locale(lang));
  }
  /**
   * Return the language independent (key) index value to be used when
   * browsing for entries of this type of Descriptor
   *
   * @param descriptor the descriptor, used here as a filter criterion.
   * @param session    the session of hibernate
   * @return the browse index
   * @throws HibernateException
   */
  public String getBrowseKey(final Descriptor descriptor, final Session session) throws HibernateException {
    final DAOIndexList dao = new DAOIndexList();
    final String result = dao.getIndexBySortFormType(descriptor.getSortFormParameters().getSortFormMainType(), descriptor.getCorrelationValues().getValue(1), session);
    return (result != null) ? result : descriptor.getBrowseKey();
  }

  /**
   * Return a list of headings for a specific a search through the stringText of the tag
   *
   * @param stringText  the string text of the tag used here as filter criterion
   * @param view        the view used here as filter criterion
   * @param mainLibrary the main library used here as filter criterion
   * @param pageSize    the page size used here as filter criterion
   * @param lang        the lang used here as filter criterion
   * @return a list of headings
   * @throws DataAccessException
   * @throws InvalidBrowseIndexException
   */
  public List<MapHeading> getHeadingsByTag(final String tag, final String indicator1, final String indicator2, final String stringText, final int view, final int mainLibrary, final int pageSize, final String lang) {
    try {
      String key;
      String browseTerm;
      String operator = ">";
      final List<Descriptor> descriptorsList;
      final BibliographicCatalog catalog = new BibliographicCatalog();
      final CatalogItem item = new BibliographicItem();
      final TagImpl impl = new BibliographicTagImpl();
      final Correlation corr = impl.getCorrelation(tag, indicator1.charAt(0), indicator2.charAt(0), 0, session);
      final Tag newTag = catalog.getNewTag(item, corr.getKey().getMarcTagCategoryCode(), corr.getValues());
      if (newTag != null) {
        final StringText st = new StringText(stringText);
        ((VariableField) newTag).setStringText(st);
        if (newTag instanceof Browsable) {
          ((Browsable) newTag).setDescriptorStringText(st);
          final Descriptor descriptor = ((Browsable) newTag).getDescriptor();
          key = getBrowseKey(descriptor, session);
          final DAODescriptor dao = (DAODescriptor) descriptor.getDAO();
          String filter = Global.FILTER_MAP.get(key);
          if (dao instanceof ShelfListDAO) {
            filter = filter + " and hdg.mainLibraryNumber = " + mainLibrary;
          }
          browseTerm = descriptor.getDisplayText();
          browseTerm = dao.calculateSearchTerm(browseTerm, key, session);
          if (dao instanceof PublisherDescriptorDAO || dao instanceof NameTitleNameDescriptorDAO)
            operator = ">=";
          descriptorsList = dao.getHeadingsBySortform(operator, "", browseTerm, filter, view, pageSize, session);
          return getMapHeadings(view, descriptorsList, dao);
        }
      }
    } catch (final HibernateException | SQLException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
    return null;
  }


  /**
   * Get the record associated with given data.
   *
   * @param itemNumber -- the record identifier.
   * @param view       -- the search view.
   * @return the {@link BibliographicRecord} associated with the given data.
   */
  public ContainerRecordTemplate getBibliographicRecordById(final int itemNumber, final int view) {

	    final ContainerRecordTemplate container = new ContainerRecordTemplate();
	    CatalogItem item = null;
	    try {
	      item = getCatalogItemByKey(itemNumber, view);
	    } catch (RecordNotFoundException re) {
	      return null;
	    }

	    final BibliographicRecord bibliographicRecord = new BibliographicRecord();
	    bibliographicRecord.setId(item.getAmicusNumber());
	    bibliographicRecord.setRecordView(item.getUserView());

	    org.folio.marccat.resources.domain.Leader leader = new org.folio.marccat.resources.domain.Leader();
	    leader.setCode("000");
	    leader.setValue(((org.folio.marccat.dao.persistence.Leader) item.getTag(0)).getDisplayString());
	    bibliographicRecord.setLeader(leader);
	    final char canadianIndicator = ((BibliographicItem) item).getBibItmData().getCanadianContentIndicator();
	    bibliographicRecord.setCanadianContentIndicator(String.valueOf(canadianIndicator));
	    bibliographicRecord.setVerificationLevel(String.valueOf(item.getItemEntity().getVerificationLevel()));

	    item.getTags().stream().skip(1).forEach(aTag -> {
	      int keyNumber = 0;
	      int sequenceNbr = 0;
	      int skipInFiling = 0;

	      if (aTag.isFixedField() && aTag instanceof MaterialDescription) {
	        final MaterialDescription materialTag = (MaterialDescription) aTag;
	        keyNumber = materialTag.getMaterialDescriptionKeyNumber();
	        final String tagNbr = materialTag.getMaterialDescription008Indicator().equals("1") ? "008" : "006";
	        final Map <String, Object> map = getMaterialTypeInfosByLeaderValues(materialTag.getItemRecordTypeCode(), materialTag.getItemBibliographicLevelCode(), tagNbr);
	        materialTag.setHeaderType((int) map.get(Global.HEADER_TYPE_LABEL));
	        materialTag.setMaterialTypeCode(tagNbr.equalsIgnoreCase("006") ? (String) map.get(Global.MATERIAL_TYPE_CODE_LABEL) : null);
	        materialTag.setFormOfMaterial((String) map.get(Global.FORM_OF_MATERIAL_LABEL));
	      }

	      if (aTag.isFixedField() && aTag instanceof PhysicalDescription) {
	        final PhysicalDescription physicalTag = (PhysicalDescription) aTag;
	        keyNumber = physicalTag.getKeyNumber();
	      }

	      if (!aTag.isFixedField() && aTag instanceof BibliographicAccessPoint) {
	        keyNumber = ((BibliographicAccessPoint) aTag).getDescriptor().getKey().getHeadingNumber();
	        try {
	          sequenceNbr = ((BibliographicAccessPoint) aTag).getSequenceNumber();
	        } catch (Exception e) {
	          sequenceNbr = 0;
	        }

	        if (aTag instanceof TitleAccessPoint) {
	          skipInFiling = ((TitleAccessPoint) aTag).getDescriptor().getSkipInFiling();
	        }
	      }

	      if (!aTag.isFixedField() && aTag instanceof BibliographicNoteTag) {
	        keyNumber = ((BibliographicNoteTag) aTag).getNoteNbr();
	        try {
	          sequenceNbr = ((BibliographicNoteTag) aTag).getSequenceNumber();
	        } catch (Exception e) {
	          sequenceNbr = 0;
	        }
	      }

	      if (!aTag.isFixedField() && aTag instanceof PublisherManager) {
	        keyNumber = ((PublisherManager) aTag).getPublisherTagUnits().get(0).getPublisherHeadingNumber(); //add gestione multi publisher
	      }

	      final CorrelationKey correlation = aTag.getTagImpl().getMarcEncoding(aTag, session);

	      final String entry = aTag.isFixedField()
	        ? (((FixedField) aTag).getDisplayString())
	        : ((VariableField) aTag).getStringText().getMarcDisplayString(Subfield.SUBFIELD_DELIMITER);

	      final org.folio.marccat.resources.domain.Field field = new org.folio.marccat.resources.domain.Field();
	      org.folio.marccat.resources.domain.VariableField variableField;
	      org.folio.marccat.resources.domain.FixedField fixedField;
	      String tagNumber = correlation.getMarcTag();
	      if (aTag.isFixedField()) {
	        fixedField = new org.folio.marccat.resources.domain.FixedField();
	        fixedField.setSequenceNumber(ofNullable(sequenceNbr).isPresent() ? sequenceNbr : 0);
	        fixedField.setCode(tagNumber);
	        fixedField.setDisplayValue(entry);
	        fixedField.setHeaderTypeCode(aTag.getCorrelation(1));
	        fixedField.setCategoryCode(aTag.getCategory());
	        fixedField.setKeyNumber(keyNumber);
	        field.setFixedField(fixedField);
	      } else {
	        variableField = new org.folio.marccat.resources.domain.VariableField();
	        variableField.setSequenceNumber(ofNullable(sequenceNbr).isPresent() ? sequenceNbr : 0);
	        variableField.setCode(correlation.getMarcTag());
	        variableField.setInd1("" + correlation.getMarcFirstIndicator());
	        variableField.setInd2("" + correlation.getMarcSecondIndicator());
	        variableField.setHeadingTypeCode(Integer.toString(aTag.getCorrelation(1)));
	        variableField.setItemTypeCode(Integer.toString(aTag.getCorrelation(2)));
	        variableField.setFunctionCode(Integer.toString(aTag.getCorrelation(3)));
	        variableField.setValue(entry);
	        variableField.setCategoryCode(correlation.getMarcTagCategoryCode());
	        variableField.setKeyNumber(keyNumber);
	        variableField.setSkipInFiling(skipInFiling);
	        if (variableField.getInd2().equals("S"))
	          variableField.setInd2("" + skipInFiling);
	        field.setVariableField(variableField);
	      }

	      field.setCode(tagNumber);

	      bibliographicRecord.getFields().add(field);
	    });

	    container.setBibliographicRecord(bibliographicRecord);
	    container.setRecordTemplate(ofNullable(item.getModelItem()).map(model -> {
	      try {
	        final ObjectMapper objectMapper = new ObjectMapper();
	        final RecordTemplate template = objectMapper.readValue(model.getRecordFields(), RecordTemplate.class);
	        template.setId(model.getModel().getId());
	        return template;
	      } catch (IOException exception) {
	        logger.error(MessageCatalog._00013_IO_FAILURE, exception);
	        return null;
	      }
	    }).orElse(null));

	    return container;
	  }

  /**
   * Gets category code using tag and indicators.
   *
   * @param tag -- the tag number.
   * @param firstIndicator -- the 1.st indicator.
   * @param secondIndicator -- the 2nd. indicator.
   * @param hasTitle -- indicates if there is a title portion in tag value.
   * @return category code.
   * @throws DataAccessException -- in case of DataAccessException.
   */
  public int getTagCategory(final String tag,
                            final char firstIndicator,
                            final char secondIndicator,
                            final boolean hasTitle) throws DataAccessException {
    final BibliographicCorrelationDAO dao = new BibliographicCorrelationDAO();

    try {
      List<BibliographicCorrelation> correlations = dao.getCategoryCorrelation(session, tag, firstIndicator, secondIndicator);
      if (correlations.size() == 1) {
        return correlations.stream().filter(Objects::nonNull).findFirst().get().getKey().getMarcTagCategoryCode();
      } else {
        if (correlations.size() > 1) {
          if ((tag.endsWith("00") || tag.endsWith("10") || tag.endsWith("11")) && hasTitle){
            return Global.NAME_TITLE_CATEGORY;
          } else
            if (correlations.stream().filter(Objects::nonNull).findFirst().isPresent())
              return correlations.stream().filter(Objects::nonNull).findFirst().get().getKey().getMarcTagCategoryCode();
        }
      }

      return 0;

    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }


  /**
   * Checks if record is new then execute insert or update.
   *
   * @param record             -- the bibliographic record to save.
   * @param view               -- the view associated to user.
   * @param generalInformation -- @linked GeneralInformation for default values.
   * @throws DataAccessException in case of data access exception.
   */
  public void saveBibliographicRecord(final BibliographicRecord record, final RecordTemplate template, final int view, final GeneralInformation generalInformation, final String lang) throws DataAccessException {

    CatalogItem item = null;
    try {
      item = getCatalogItemByKey(record.getId(), view);
    } catch (DataAccessException exception) {
    }

    try {

      CasCache casCache = null;
      if (item == null || item.getTags().isEmpty()) {
        item = insertBibliographicRecord(record, view, generalInformation, lang);
        casCache = new CasCache(item.getAmicusNumber());
        casCache.setLevelCard("L1");
        casCache.setStatusDisponibilit(99);
      } else {
        updateBibliographicRecord(record, item, view, generalInformation);
      }

      final int an = item.getAmicusNumber();
      item.setModelItem(
        ofNullable(template).map(t ->{
          final BibliographicModelItemDAO dao = new BibliographicModelItemDAO();
          final ObjectMapper mapper = new ObjectMapper();

        try {
          BibliographicModel model = (BibliographicModel) ofNullable(dao.load(an, session).getModel()).get();
          if (model == null)
            model = new BibliographicModel();

          model.setId(t.getId());
          model.setLabel(t.getName());
          model.setFrbrFirstGroup(t.getGroup());
          model.setRecordFields(mapper.writeValueAsString(t));
          return model;
        } catch (Exception e) {
          logger.error(MessageCatalog._00023_SAVE_TEMPLATE_ASSOCIATED_FAILURE, t.getId(), record.getId(), e);
          throw new RuntimeException(e);
        }
      }).orElse(null));

      if (isNotNullOrEmpty(record.getVerificationLevel()))
        item.getItemEntity().setVerificationLevel(record.getVerificationLevel().charAt(0));
      if (isNotNullOrEmpty(record.getCanadianContentIndicator()))
        ((BibliographicItem) item).getBibItmData().setCanadianContentIndicator(record.getCanadianContentIndicator().charAt(0));

      final BibliographicCatalogDAO dao = new BibliographicCatalogDAO();
      dao.saveCatalogItem(item, casCache, session);

    } catch (Exception e) {
      logger.error(MessageCatalog._00019_SAVE_RECORD_FAILURE, record.getId(), e);
      throw new DataAccessException(e);
    }
  }

  /**
   * Updates a bibliographic record.
   *
   * @param record             -- the record to update.
   * @param item               -- the catalog item associated to record.
   * @param view               -- the current view associated to record.
   * @param generalInformation -- {@linked GeneralInformation} for default values.
   * @throws DataAccessException in case of data access exception.
   */
  private void updateBibliographicRecord(final BibliographicRecord record, final CatalogItem item, final int view,
                                         final GeneralInformation generalInformation) throws DataAccessException {

    final RecordParser recordParser = new RecordParser();
    final int bibItemNumber = item.getAmicusNumber();
    final String newLeader = record.getLeader().getValue();
    recordParser.changeLeader(item, newLeader);

    record.getFields().forEach(field -> {

      final String tagNbr = field.getCode();
      final Field.FieldStatus status = field.getFieldStatus();

      if (status == Field.FieldStatus.NEW
        || status == Field.FieldStatus.DELETED
        || status == Field.FieldStatus.CHANGED) {

        if (tagNbr.equals(Global.MATERIAL_TAG_CODE) && status == Field.FieldStatus.CHANGED) {
          recordParser.changeMaterialDescriptionTag(item, field, session);
        }

        if (tagNbr.equals(Global.OTHER_MATERIAL_TAG_CODE)) {
          final Map <String, Object> mapRecordTypeMaterial = getMaterialTypeInfosByLeaderValues(newLeader.charAt(6), newLeader.charAt(7), tagNbr);
          final String formOfMaterial = (String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL);
          recordParser.changeMaterialDescriptionOtherTag(item, field, session, formOfMaterial, generalInformation);
        }

        if (tagNbr.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)) {
          recordParser.changePhysicalDescriptionTag(item, field, bibItemNumber);
        }

        if (tagNbr.equals(Global.CATALOGING_SOURCE_TAG_CODE) && status == Field.FieldStatus.CHANGED) {
          item.getTags().stream().filter(aTag -> !aTag.isFixedField() && aTag instanceof CataloguingSourceTag).forEach(aTag -> {
            final CataloguingSourceTag cst = (CataloguingSourceTag) aTag;
            cst.setStringText(new StringText(field.getVariableField().getValue()));
            cst.markChanged();
          });
        }

        if (field.getVariableField() != null && !tagNbr.equals(Global.CATALOGING_SOURCE_TAG_CODE)) {
          final org.folio.marccat.resources.domain.VariableField variableField = field.getVariableField();
          final CorrelationValues correlationValues = getCorrelationVariableField(variableField.getCategoryCode(),
            variableField.getInd1(), variableField.getInd2(), tagNbr);
          if (correlationValues == null) {
            logger.error(MessageCatalog._00018_NO_HEADING_TYPE_CODE, variableField.getCode());
            throw new DataAccessException();
          }

          try {
            if (field.getVariableField().getCategoryCode() == Global.BIB_NOTE_CATEGORY && correlationValues.getValue(1) != Global.PUBLISHER_DEFAULT_NOTE_TYPE) {
              recordParser.changeNoteTag(item, field, correlationValues, bibItemNumber, view);
            } else if (field.getVariableField().getCategoryCode() == Global.BIB_NOTE_CATEGORY && correlationValues.getValue(1) == Global.PUBLISHER_DEFAULT_NOTE_TYPE) {
              recordParser.changePublisherTag(item, field, correlationValues, bibItemNumber, view, session);
            } else {
              recordParser.changeAccessPointTag(item, field, correlationValues, bibItemNumber, view, session);
            }

          } catch (HibernateException | SQLException e) {
            throw new DataAccessException(e);
          }
        }
      }
    });

  }

  /**
   * Set descriptors for each tag.
   *
   * @param item            -- the catalog item.
   * @param recordView      -- the record view.
   * @param cataloguingView -- the cataloguing view.
   * @throws DataAccessException in case of data access exception.
   */
  public void setDescriptors(final CatalogItem item, final int recordView, final int cataloguingView) throws DataAccessException {

    item.getTags().forEach(aTag -> {
      if (aTag instanceof AccessPoint) {
        try {
          AccessPoint apf = ((AccessPoint) aTag);
          Descriptor d = apf.getDAODescriptor().findOrCreateMyView(apf.getHeadingNumber(), View.makeSingleViewString(recordView), cataloguingView, session);
          apf.setDescriptor(d);
        } catch (HibernateException e) {
          throw new DataAccessException(e);
        }
      } else if (aTag instanceof BibliographicRelationshipTag) {
        BibliographicRelationshipTag relTag = (BibliographicRelationshipTag) aTag;
        relTag.copyFromAnotherItem();
      }
    });
  }
  
  /**
   * Insert a new bibliographic record.
   *
   * @param record -- the record bibliographic.
   * @param view   -- the current view associated to record.
   * @param giAPI  -- {@linked GeneralInformation} for default values.
   * @throws DataAccessException in case of data access exception.
   */
  private CatalogItem insertBibliographicRecord(final BibliographicRecord record, final int view, final GeneralInformation giAPI, final String lang) throws DataAccessException {
    final RecordParser recordParser = new RecordParser();
    final BibliographicCatalog catalog = new BibliographicCatalog();
    final int bibItemNumber = record.getId();
    final CatalogItem item = catalog.newCatalogItem(new Object[]{new Integer(view), new Integer(bibItemNumber)});

    Leader leader = record.getLeader();
    item.getItemEntity().setLanguageOfCataloguing(lang);

    if (leader != null) {
      final BibliographicLeader bibLeader = catalog.createRequiredLeaderTag(item);
      catalog.toBibliographicLeader(leader.getValue(), bibLeader);
      item.addTag(bibLeader);
    }

    ControlNumberTag cnt = catalog.createRequiredControlNumberTag(item);
    item.addTag(cnt);

    DateOfLastTransactionTag dateOfLastTransactionTag = catalog.createRequiredDateOfLastTransactionTag(item);
    item.addTag(dateOfLastTransactionTag);

    record.getFields().stream().skip(1).forEach(field -> {
      final String tagNbr = field.getCode();
      if (tagNbr.equals(Global.MATERIAL_TAG_CODE) || tagNbr.equals(Global.OTHER_MATERIAL_TAG_CODE)) {
        final org.folio.marccat.resources.domain.FixedField fixedField = field.getFixedField();
        final Map <String, Object> mapRecordTypeMaterial;
        final String formOfMaterial;
        if (tagNbr.equals(Global.MATERIAL_TAG_CODE)) {
          mapRecordTypeMaterial = getMaterialTypeInfosByLeaderValues(leader.getValue().charAt(6), leader.getValue().charAt(7), tagNbr);
          formOfMaterial = (String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL);
          fixedField.setHeaderTypeCode((int) mapRecordTypeMaterial.get(Global.HEADER_TYPE_LABEL));
        } else {
          mapRecordTypeMaterial = getMaterialTypeInfosByHeaderCode(fixedField.getHeaderTypeCode(), tagNbr);
          formOfMaterial = (String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL);
        }

        recordParser.addMaterialDescriptionToCatalog(tagNbr, item, fixedField, giAPI, formOfMaterial);
      }

      if (tagNbr.equals(Global.PHYSICAL_DESCRIPTION_TAG_CODE)) {
        final org.folio.marccat.resources.domain.FixedField fixedField = field.getFixedField();
        recordParser.addPhysicalDescriptionTag(item, fixedField, bibItemNumber);
      }

      if (tagNbr.equals(Global.CATALOGING_SOURCE_TAG_CODE)) {
        final org.folio.marccat.resources.domain.VariableField variableField = field.getVariableField();
        CataloguingSourceTag cst = catalog.createRequiredCataloguingSourceTag(item);
        cst.setStringText(new StringText(variableField.getValue()));
        item.addTag(cst);
      }

      if (field.getVariableField() != null && !tagNbr.equals(Global.CATALOGING_SOURCE_TAG_CODE)) {
        final org.folio.marccat.resources.domain.VariableField variableField = field.getVariableField();
        final CorrelationValues correlationValues = getCorrelationVariableField(variableField.getCategoryCode(),
          variableField.getInd1(), variableField.getInd2(), tagNbr);
        if (correlationValues == null) {
          logger.error(MessageCatalog._00018_NO_HEADING_TYPE_CODE, variableField.getCode());
          throw new DataAccessException();
        }
        recordParser.insertNewVariableField(item, variableField, bibItemNumber, correlationValues, session, view);
      }

    });
    setDescriptors(item, item.getUserView(), view);
    return item;
  }

  /**
   * Checks if record is new then execute insert or update.
   *
   * @param record             -- the bibliographic record to save.
   * @param view               -- the view associated to user.
   * @param generalInformation -- @linked GeneralInformation for default values.
   * @throws DataAccessException in case of data access exception.
   */
  public void saveBibliographicRecord(final BibliographicRecord record, final int view, final GeneralInformation generalInformation, final String lang) throws DataAccessException {

    CatalogItem item = null;
    try {
      item = getCatalogItemByKey(record.getId(), view);
    } catch (DataAccessException exception) {
    }

    try {
      CasCache casCache = null;
      if (item == null || item.getTags().size() == 0) {
        item = insertBibliographicRecord(record, view, generalInformation, lang);
        casCache = new CasCache(item.getAmicusNumber());
        casCache.setLevelCard("L1");
        casCache.setStatusDisponibilit(99);

      } else {
        updateBibliographicRecord(record, item, view, generalInformation);
      }

      if (isNotNullOrEmpty(record.getVerificationLevel()))
        item.getItemEntity().setVerificationLevel(record.getVerificationLevel().charAt(0));
      if (isNotNullOrEmpty(record.getCanadianContentIndicator()))
        ((BibliographicItem) item).getBibItmData().setCanadianContentIndicator(record.getCanadianContentIndicator().charAt(0));

      final BibliographicCatalogDAO dao = new BibliographicCatalogDAO();
      dao.saveCatalogItem(item, casCache, session);

    } catch (Exception e) {
      logger.error(MessageCatalog._00019_SAVE_RECORD_FAILURE, record.getId(), e);
      throw new DataAccessException(e);
    }
  }

  /**
   * Gets Validation for tag field.
   *
   * @param marcCategory the marc category used here as filter criterion.
   * @param tagNumber    the tag number used here as filter criterion.
   * @return Validation object containing subfield list.
   */
  public Validation getTagValidation(final int marcCategory,
                                     final String tagNumber) throws DataAccessException {
    final BibliographicValidationDAO daoBibliographicValidation = new BibliographicValidationDAO();
    try {
      return daoBibliographicValidation.load(session, tagNumber, marcCategory);
    } catch (final HibernateException exception) {
      logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  
  /**
   * Delete a bibliographic record.
   *
   * @param itemNumber -- the amicus number associated to record.
   */
  public void deleteBibliographicRecordById(final Integer itemNumber, final int view, final String uuid, final String userName) throws DataAccessException {
    final BibliographicCatalog catalog = new BibliographicCatalog();

    try {
      CatalogItem item = getCatalogItemByKey(itemNumber, view);
      lockRecord(itemNumber, userName, uuid);
      catalog.deleteCatalogItem(item, session);
      unlockRecord(itemNumber, userName);
    } catch (RecordNotFoundException exception) {
      //ignore
    } catch (Exception exception) {
      logger.error(MessageCatalog._00022_DELETE_RECORD_FAILURE, itemNumber, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Unlock a record or heading locked from user previously.
   *
   * @param id       -- the key number or amicus number.
   * @param userName -- the username who unlock entity.
   */
  public void unlockRecord(final int id, final String userName) throws DataAccessException {
    try {
      final BibliographicCatalog catalog = new BibliographicCatalog();
      catalog.unlock(id, userName, session);
    } catch (RecordInUseException exception) {
      logger.error(MessageCatalog._00021_UNLOCK_FAILURE, id, userName, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Lock a record or heading.
   *
   * @param id       -- the key number or amicus number.
   * @param userName -- the username who unlock entity.
   * @param uuid     -- the uuid associated to lock/unlock session.
   */
  public void lockRecord(final int id, final String userName, final String uuid) throws DataAccessException {
    try {
      final BibliographicCatalog catalog = new BibliographicCatalog();
      catalog.lock(id, userName, uuid, session);
    } catch (RecordInUseException exception) {
      logger.error(MessageCatalog._00020_LOCK_FAILURE, id, userName, exception);
      throw new DataAccessException(exception);
    }
  }

  
}
