package org.folio.marccat.integration;

import static org.folio.marccat.config.constants.Global.EMPTY_VALUE;
import static org.folio.marccat.util.F.locale;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.folio.marccat.business.cataloguing.authority.AuthorityCatalog;
import org.folio.marccat.business.cataloguing.authority.AuthorityItem;
import org.folio.marccat.business.cataloguing.authority.AuthorityTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.VariableField;
import org.folio.marccat.business.cataloguing.common.Browsable;
import org.folio.marccat.business.cataloguing.common.CataloguingSourceTag;
import org.folio.marccat.business.cataloguing.common.ControlNumberTag;
import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.AuthorityCatalogDAO;
import org.folio.marccat.dao.CodeTableDAO;
import org.folio.marccat.dao.AuthorityModelDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.dao.persistence.Authority008Tag;
import org.folio.marccat.dao.persistence.AuthorityLeader;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.T_AUT_ENCDG_LVL;
import org.folio.marccat.dao.persistence.T_AUT_REC_STUS;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.resources.domain.AuthorityRecord;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.resources.shared.RecordUtils;
import org.folio.marccat.util.StringText;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.hibernate.HibernateException;

/**
 * @author Elena
 *
 */
public class AuthorityStorageService {
  private static final Log logger = new Log(AuthorityStorageService.class);
  StorageService storageService;

  public StorageService getStorageService() {
    return storageService;
  }

  public void setStorageService(StorageService storageService) {
    this.storageService = storageService;
  }

  /**
   * Returns the record status types associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the record status
   *         type associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List<Avp<String>> getRecordStatusTypes(final String lang) {
    final CodeTableDAO dao = new CodeTableDAO();
    return dao.getList(getStorageService().getSession(), T_AUT_REC_STUS.class, locale(lang));
  }

  /**
   * Returns the encoding levels associated with the given language.
   *
   * @param lang the language code, used here as a filter criterion.
   * @return a list of code / description tuples representing the encoding level
   *         associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List<Avp<String>> getEncodingLevels(final String lang) {
    final CodeTableDAO dao = new CodeTableDAO();
    return dao.getList(getStorageService().getSession(), T_AUT_ENCDG_LVL.class, locale(lang));
  }

  /**
   * Returns the codes list associated with the given language and key.
   *
   * @param lang the language code, used here as a filter criterion.
   * @param codeListType the code list type key.
   * @return a list of code / description tuples representing the date format
   *         associated with the requested language.
   * @throws DataAccessException in case of data access failure.
   */
  public List<Avp<String>> getCodesList(final String lang, final CodeListsType codeListType) {
    final CodeTableDAO dao = new CodeTableDAO();
    return dao.getList(getStorageService().getSession(), Global.MAP_CODE_LISTS.get(codeListType.toString()), locale(lang));
  }

  /**
   * Find the {@link CatalogItem} associated with the given data.
   *
   * @param itemNumber the record identifier.
   * @param searchingView the search view.
   * @return the {@link CatalogItem} associated with the given data.
   */
  public CatalogItem getCatalogItemByKey(final int itemNumber, final int searchingView) {
    return new AuthorityCatalogDAO().getCatalogItemByKey(getStorageService().getSession(), itemNumber, searchingView);
  }

  /**
   * Return a Authority Record Template by id.
   *
   * @param id the record template id.
   * @return the authority record template associated with the given id.
   * @throws DataAccessException in case of data access failure.
   */
  public RecordTemplate getAuthorityRecordRecordTemplatesById(final Integer id) {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(
          new AuthorityModelDAO().load(id, getStorageService().getSession()).getRecordFields(), RecordTemplate.class);
    } catch (final HibernateException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    } catch (final IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Checks if authority record is new then execute insert or update.
   *
   * @param record -- the authority record to save.
   * @param view -- the view associated to user.
   * @param generalInformation -- @linked GeneralInformation for default values.
   * @throws DataAccessException in case of data access exception.
   */
  public void saveAuthorityRecord(final AuthorityRecord record, final int view, final String lang, final Map<String, String> configuration) {
    CatalogItem item = null;
    try {
      item = getCatalogItemByKey(record.getId(), view);
    } catch (DataAccessException exception) {
      // do not put any exception here!!!!!!!!!!!!! , because the microservice doesn't
      // insert the record
    }

    try {

      if (item == null || item.getTags().isEmpty()) {
        item = insertAuthorityRecord(record, view, lang, configuration);
      }

      final AuthorityCatalogDAO dao = new AuthorityCatalogDAO();
      dao.saveCatalogItem(item, getStorageService().getSession());

    } catch (Exception e) {
      logger.error(Message.MOD_MARCCAT_00019_SAVE_AUT_RECORD_FAILURE, record.getId(), e);
      throw new DataAccessException(e);
    }
  }

  /**
   * Insert a new authority record.
   *
   * @param record -- the authority record.
   * @param view -- the current view associated to record.
   * @param giAPI -- {@linked GeneralInformation} for default values.
   * @throws DataAccessException in case of data access exception.
   * @throws HibernateException
   */
  private CatalogItem insertAuthorityRecord(final AuthorityRecord record, final int view, final String lang, final Map<String, String> configuration)
      throws HibernateException {

    final AuthorityCatalog catalog = new AuthorityCatalog();
    final int autItemNumber = new SystemNextNumberDAO().getNextNumber("AA", getStorageService().getSession());
    final CatalogItem item = catalog.newCatalogItem(new Object[] { view, autItemNumber });

    AuthorityTagImpl tagImpl = new AuthorityTagImpl();

    item.getItemEntity().setAmicusNumber(autItemNumber);
    record.setId(autItemNumber);

    Leader leader = record.getLeader();
    item.getItemEntity().setLanguageOfCataloguing(lang);
    if (leader != null) {
      final AuthorityLeader autLeader = catalog.createRequiredLeaderTag(item);
      autLeader.setCorrelationKey(tagImpl.getMarcEncoding(autLeader, getStorageService().getSession()));
      if (!item.getTags().contains(autLeader))
        item.addTag(autLeader);
    }

    ControlNumberTag cnt = catalog.createRequiredControlNumberTag(item);
    if (!item.getTags().contains(cnt))
      item.addTag(cnt);

    DateOfLastTransactionTag dateOfLastTransactionTag = catalog.createRequiredDateOfLastTransactionTag(item);
    if (!item.getTags().contains(dateOfLastTransactionTag))
      item.addTag(dateOfLastTransactionTag);

    Authority008Tag a008 = catalog.createRequired008Tag(item);
    if (!item.getTags().contains(a008))
      item.addTag(a008);

    record.getFields().stream().skip(1).forEach(field -> {
      final String tagNbr = field.getCode();
      if (tagNbr.equals(Global.CATALOGING_SOURCE_TAG_CODE)) {
        final org.folio.marccat.resources.domain.VariableField variableField = field.getVariableField();
        CataloguingSourceTag cst = catalog.createRequiredCataloguingSourceTag(item);
        cst.setStringText(new StringText(variableField.getValue()));
        if (!item.getTags().contains(cst))
          item.addTag(cst);
      }

      if (Global.NAMES.contains(tagNbr)) {
        try {
          processNameTag(field, tagImpl, item, catalog, configuration);
        } catch (HibernateException | SQLException e) {
          throw new DataAccessException(e);
        }
      }

    });
    return item;
  }

  private void processNameTag(Field field, AuthorityTagImpl tagImpl, CatalogItem item, AuthorityCatalog catalog, final Map<String, String> configuration)
      throws HibernateException, SQLException {
    final String tagNbr = field.getCode();
    final org.folio.marccat.resources.domain.VariableField variableField = field.getVariableField();
    Heading heading = new Heading();
    heading.setTag(tagNbr);
    heading.setInd1(variableField.getInd1());
    heading.setInd2(variableField.getInd2());
    heading.setDisplayValue(variableField.getValue());

    heading.setCategoryCode(RecordUtils.getTagCategory(heading, this.getStorageService()));
    int headingNumber = 0;
    final boolean isInd1IsEmpty = heading.getInd1().equals(EMPTY_VALUE);
    final boolean isInd2IsEmpty = heading.getInd2().equals(EMPTY_VALUE);
    final Correlation corr = tagImpl.getCorrelation(heading.getTag(), (isInd1IsEmpty) ? " ".charAt(0) : heading.getInd1().charAt(0),
        (isInd2IsEmpty) ? " ".charAt(0) : heading.getInd2().charAt(0), heading.getCategoryCode(), getStorageService().getSession());
    final Tag newTag = catalog.getNewTag(item, corr.getKey().getMarcTagCategoryCode(), corr.getValues());
    if (newTag != null) {
      newTag.getMarcEncoding(getStorageService().getSession());
      final StringText st = new StringText(heading.getDisplayValue());
      ((VariableField) newTag).setStringText(st);
      if (newTag instanceof Browsable) {
        final int skipInFiling = getStorageService().updateIndicatorNotNumeric(corr.getKey(), heading.getInd1(), heading.getInd2());
        ((Browsable) newTag).setDescriptorStringText(st);
        final Descriptor descriptor = ((Browsable) newTag).getDescriptor();
        descriptor.setSkipInFiling(skipInFiling);
        headingNumber = getStorageService().createOrReplaceDescriptor(configuration, descriptor, View.DEFAULT_BIBLIOGRAPHIC_VIEW);
        heading.setKeyNumber(headingNumber);
      }

    }

    ((AuthorityItem) item).getAutItmData().setHeadingNumber(headingNumber);
    ((AuthorityItem) item).getAutItmData().setHeadingType(Global.NAME_TYPE_HDG);
    item.addTag(newTag);

  }

}
