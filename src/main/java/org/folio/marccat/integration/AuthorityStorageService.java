package org.folio.marccat.integration;

import static org.folio.marccat.config.constants.Global.EMPTY_VALUE;

import java.sql.SQLException;
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
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.AuthorityCatalogDAO;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.dao.persistence.Authority008Tag;
import org.folio.marccat.dao.persistence.AuthorityLeader;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordInUseException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.resources.domain.AuthorityRecord;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.shared.RecordUtils;
import org.folio.marccat.util.StringText;

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
   * Find the {@link CatalogItem} associated with the given data.
   *
   * @param itemNumber    the record identifier.
   * @param searchingView the search view.
   * @return the {@link CatalogItem} associated with the given data.
   */
  public CatalogItem getCatalogItemByKey(final int itemNumber, final int searchingView) {
    return new AuthorityCatalogDAO().getCatalogItemByKey(getStorageService().getSession(), itemNumber, searchingView);
  }

  /**
   * Checks if authority record is new then execute insert or update.
   *
   * @param record             -- the authority record to save.
   * @param view               -- the view associated to user.
   * @param generalInformation -- @linked GeneralInformation for default values.
   * @throws DataAccessException in case of data access exception.
   */
  public Integer saveAuthorityRecord(final AuthorityRecord record, final int view, final String lang,
      final Map<String, String> configuration) {
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
   * @param view   -- the current view associated to record.
   * @param giAPI  -- {@linked GeneralInformation} for default values.
   * @throws DataAccessException in case of data access exception.
   * @throws HibernateException
   */
  private CatalogItem insertAuthorityRecord(final AuthorityRecord record, final int view, final String lang,
      final Map<String, String> configuration) throws HibernateException {

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

  private void processNameTag(Field field, AuthorityTagImpl tagImpl, CatalogItem item, AuthorityCatalog catalog,
      final Map<String, String> configuration) throws HibernateException, SQLException {
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
    final Correlation corr = tagImpl.getCorrelation(heading.getTag(),
        (isInd1IsEmpty) ? " ".charAt(0) : heading.getInd1().charAt(0),
        (isInd2IsEmpty) ? " ".charAt(0) : heading.getInd2().charAt(0), heading.getCategoryCode(),
        getStorageService().getSession());
    final Tag newTag = catalog.getNewTag(item, corr.getKey().getMarcTagCategoryCode(), corr.getValues());
    if (newTag != null) {
      newTag.getMarcEncoding(getStorageService().getSession());
      final StringText st = new StringText(heading.getDisplayValue());
      ((VariableField) newTag).setStringText(st);
      if (newTag instanceof Browsable) {
        final int skipInFiling = getStorageService().updateIndicatorNotNumeric(corr.getKey(), heading.getInd1(),
            heading.getInd2());
        ((Browsable) newTag).setDescriptorStringText(st);
        final Descriptor descriptor = ((Browsable) newTag).getDescriptor();
        descriptor.setSkipInFiling(skipInFiling);
        headingNumber = getStorageService().createOrReplaceDescriptor(configuration, descriptor,
            View.DEFAULT_BIBLIOGRAPHIC_VIEW);
        heading.setKeyNumber(headingNumber);
      }

    }

    ((AuthorityItem) item).getAutItmData().setHeadingNumber(headingNumber);
    ((AuthorityItem) item).getAutItmData().setHeadingType(Global.NAME_TYPE_HDG);
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
