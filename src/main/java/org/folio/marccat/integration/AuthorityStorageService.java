package org.folio.marccat.integration;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.folio.marccat.config.constants.Global.EMPTY_VALUE;
import static org.folio.marccat.resources.shared.RecordUtils.getRecordField;
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
import org.folio.marccat.dao.AuthorityCorrelationDAO;
import org.folio.marccat.dao.AuthorityModelDAO;
import org.folio.marccat.dao.CodeTableDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.dao.persistence.Authority008Tag;
import org.folio.marccat.dao.persistence.AuthorityCorrelation;
import org.folio.marccat.dao.persistence.AuthorityLeader;
import org.folio.marccat.dao.persistence.AuthorityNote;
import org.folio.marccat.dao.persistence.AuthorityReferenceTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.EquivalenceReference;
import org.folio.marccat.dao.persistence.SBJCT_HDG;
import org.folio.marccat.dao.persistence.T_AUT_ENCDG_LVL;
import org.folio.marccat.dao.persistence.T_AUT_REC_STUS;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordInUseException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.resources.domain.AuthorityRecord;
import org.folio.marccat.resources.domain.ContainerRecordTemplate;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.shared.CorrelationValues;
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

  public CorrelationValues getCorrelationVariableField(final Integer category, final String indicator1,
      final String indicator2, final String code) {
    final AuthorityCorrelationDAO authorityCorrelationDAO = new AuthorityCorrelationDAO();
    try {
      return ofNullable(authorityCorrelationDAO.getAuthorityCorrelation(getStorageService().getSession(), code,
          indicator1.charAt(0), indicator2.charAt(0), category)).map(AuthorityCorrelation::getValues).orElse(null);
    } catch (final HibernateException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
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
   * Get the record associated with given data.
   *
   * @param itemNumber -- the record identifier.
   * @param view -- the search view.
   * @return the {@link AuthorityRecord} associated with the given data.
   */
  public ContainerRecordTemplate getAuthorityRecordById(final int itemNumber, final int view) {

    final ContainerRecordTemplate container = new ContainerRecordTemplate();
    CatalogItem item;
    try {
      item = getCatalogItemByKey(itemNumber, view);
    } catch (RecordNotFoundException re) {
      return null;
    }

    final AuthorityRecord authorityRecord = new AuthorityRecord();
    authorityRecord.setId(item.getAmicusNumber());
    authorityRecord.setRecordView(View.AUTHORITY);

    org.folio.marccat.resources.domain.Leader leader = new org.folio.marccat.resources.domain.Leader();
    leader.setCode("000");
    leader.setValue(((org.folio.marccat.dao.persistence.Leader) item.getTag(0)).getDisplayString());
    authorityRecord.setLeader(leader);
    authorityRecord.setCanadianContentIndicator(Global.AUTHORITY_CANADIAN_INDICATOR);
    authorityRecord.setVerificationLevel(valueOf(item.getItemEntity().getVerificationLevel()));

    item.getTags().stream().skip(1).forEach((Tag aTag) -> {
      org.folio.marccat.resources.domain.Field field = getRecordField(aTag, getStorageService().getSession());
      authorityRecord.getFields().add(field);
    });

    container.setAuthorityRecord(authorityRecord);
    container.setRecordTemplate(ofNullable(item.getModelItem()).map(model -> {
      try {
        final ObjectMapper objectMapper = new ObjectMapper();
        final RecordTemplate template = objectMapper.readValue(model.getRecordFields(), RecordTemplate.class);
        template.setId(model.getModel().getId());
        return template;
      } catch (IOException exception) {
        logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
        return null;
      }
    }).orElse(null));

    return container;
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
   * @param lang -- the language associated to user.
   * @param generalInformation -- @linked GeneralInformation for default values.
   * @throws DataAccessException in case of data access exception.
   */
  public Integer saveAuthorityRecord(final AuthorityRecord record, final int view, final String lang, final Map<String, String> configuration) {
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
      return item.getAmicusNumber();
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

    final int autItemNumber = getAutItemNumber(record.getId());

    final CatalogItem item = catalog.newCatalogItem(new Object[] { view, autItemNumber });

    AuthorityTagImpl tagImpl = new AuthorityTagImpl();

    item.getItemEntity().setAmicusNumber(autItemNumber);
    record.setId(autItemNumber);

    Leader leader = record.getLeader();
    item.getItemEntity().setLanguageOfCataloguing(lang);
    if (leader != null) {
      final AuthorityLeader autLeader = catalog.createRequiredLeaderTag(item);
      catalog.toAuthorityLeader(leader.getValue(), autLeader);
      if (!item.getTags().contains(autLeader))
        item.addTag(autLeader);
    }
    ControlNumberTag cnt = catalog.createRequiredControlNumberTag(item);
    if (!item.getTags().contains(cnt))
      item.addTag(cnt);

    DateOfLastTransactionTag dateOfLastTransactionTag = catalog.createRequiredDateOfLastTransactionTag(item);
    if (!item.getTags().contains(dateOfLastTransactionTag))
      item.addTag(dateOfLastTransactionTag);

    record.getFields().forEach(field -> {
      final String tagNbr = field.getCode();
      if (tagNbr.equals(Global.MATERIAL_TAG_CODE)) {
        Authority008Tag a008 = catalog.createRequired008Tag(item);
        final org.folio.marccat.resources.domain.FixedField tag008 = field.getFixedField();
        catalog.toAuthority008Tag(tag008.getDisplayValue(), a008);
        if (!item.getTags().contains(a008))
          item.addTag(a008);

      } else if (tagNbr.equals(Global.CATALOGING_SOURCE_TAG_CODE)) {
        CataloguingSourceTag cst = catalog.createRequiredCataloguingSourceTag(item);
        cst.setStringText(new StringText(field.getVariableField().getValue()));
        if (!item.getTags().contains(cst))
          item.addTag(cst);
      }

      else if (Global.AUT_NAMES.contains(tagNbr) || Global.AUT_TITLE.contains(tagNbr)
          || Global.AUT_SUBJECT.contains(tagNbr) || Global.AUT_NAMES_X.contains(tagNbr)
          || Global.AUT_TITLE_X.contains(tagNbr) || Global.AUT_SUBJECT_X.contains(tagNbr)) {
        try {
          processDesciptorTag(tagNbr, ((AuthorityItem) item).getAutItmData().getHeadingNumber(),
              field.getVariableField(), catalog, item, tagImpl, configuration);
        } catch (HibernateException | SQLException e) {
          throw new DataAccessException(e);
        }
      } else if (Global.AUT_NOTES.contains(tagNbr)) {
        final CorrelationValues correlationValues = getCorrelationVariableField(
            field.getVariableField().getCategoryCode(), field.getVariableField().getInd1(),
            field.getVariableField().getInd2(), tagNbr);
        AuthorityNote newTag = catalog.createAuthorityNote(item, correlationValues);
        newTag.setContent(field.getVariableField().getValue());

        final StringText st = new StringText(field.getVariableField().getValue());
        newTag.setStringText(st);

        newTag.setItemNumber(autItemNumber);
        newTag.markNew();
        item.addTag(newTag);
      }

    });
    return item;
  }


  private int getAutItemNumber(Integer recordIdJson) throws HibernateException {
    if (recordIdJson == 0) {
      return new SystemNextNumberDAO().getNextNumber("AA", getStorageService().getSession());
    } else {
      return recordIdJson;
    }
  }

  public void processDesciptorTag(String tagNbr, int headingAutNumber,
      org.folio.marccat.resources.domain.VariableField variableField, AuthorityCatalog catalog, CatalogItem item,
      AuthorityTagImpl tagImpl, Map<String, String> configuration) throws HibernateException, SQLException {

    Heading heading = new Heading();
    heading.setTag(tagNbr);
    heading.setInd1(variableField.getInd1());
    heading.setInd2(variableField.getInd2());
    heading.setDisplayValue(variableField.getValue());

    heading.setCategoryCode(tagImpl.getTagCategory(heading, getStorageService().getSession()));
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
        descriptor.getKey().setHeadingNumber(headingNumber);

        if (descriptor instanceof SBJCT_HDG)
          ((SBJCT_HDG) descriptor).setSourceCode(Global.SUBJECT_SOURCE_CODE_OTHERS);

        heading.setKeyNumber(headingNumber);
        ((Browsable) newTag).setDescriptor(descriptor);
        if (newTag instanceof AuthorityReferenceTag) {
          ((AuthorityReferenceTag) newTag).getReference().toSubfieldWReferenceTag(variableField.getValue(),
              (newTag instanceof EquivalenceReference));
          ((AuthorityReferenceTag) newTag).getReference().setSource(headingAutNumber);
        }

      }

    }
    if (!(newTag instanceof AuthorityReferenceTag))
      ((AuthorityItem) item).getAutItmData().setHeadingNumber(headingNumber);

    item.addTag(newTag);

  }

  /**
   * Delete a authority record.
   *
   * @param itemNumber -- the amicus number associated to record.
   */
  public void deleteAuhorityRecordById(final Integer itemNumber) {
    final AuthorityCatalog catalog = new AuthorityCatalog();

    try {
      CatalogItem item = getCatalogItemByKey(itemNumber, View.AUTHORITY);
      CountDocument countDocument = getStorageService().getCountDocumentByAutNumber(itemNumber,
          View.DEFAULT_BIBLIOGRAPHIC_VIEW);
      if (countDocument.getCountDocuments() == 0) {
        catalog.deleteCatalogItem(item, getStorageService().getSession());
      } else {
        throw new RecordInUseException();
      }

    } catch (RecordInUseException exception) {
      throw new RecordInUseException();
    } catch (RecordNotFoundException exception) {
      throw new RecordNotFoundException(exception);
    } catch (Exception exception) {
      logger.error(Message.MOD_MARCCAT_00022_DELETE_RECORD_FAILURE, itemNumber, exception);
      throw new DataAccessException(exception);
    }
  }

}
