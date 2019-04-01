package org.folio.marccat.integration.record;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.marccat.business.cataloguing.bibliographic.BibliographicCatalog;
import org.folio.marccat.business.cataloguing.bibliographic.MarcCommandLibrary;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.Global;
import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.RecordTypeMaterialDAO;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.util.StringText;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static java.util.Optional.ofNullable;


public class RecordParser {

  static final BibliographicCatalog catalog = new BibliographicCatalog();

  /**
   * Change leader tag.
   *
   * @param item   -- the current catalog item.
   * @param leader -- the new leader string values.
   */
  public void changeLeader(final CatalogItem item, final String leader) {
    final BibliographicLeader bibliographicLeader = ((BibliographicLeader) item.getTag(0));
    final String leaderString = bibliographicLeader.getDisplayString();
    final String valuesToCompare = leaderString.substring(5, 10) + leaderString.substring(17, 20);
    final String newLeader = leader.substring(5, 10) + leader.substring(17, 20);

    if (!valuesToCompare.equals(newLeader)) {
      catalog.toBibliographicLeader(leader, bibliographicLeader);
      bibliographicLeader.markChanged();
    }
  }

  /**
   * Changes a material description tag for 008 tag.
   *
   * @param item    -- the current catalog item.
   * @param field   -- bibliographic record field.
   * @param session -- the current hibernate session.
   */
  public void changeMaterialDescriptionTag(final CatalogItem item, final Field field, final Session session) {
    item.getTags().stream().filter(aTag -> aTag.isFixedField() && aTag instanceof MaterialDescription).forEach(aTag -> {
      final MaterialDescription materialTag = (MaterialDescription) aTag;
      final CorrelationKey correlation = aTag.getTagImpl().getMarcEncoding(aTag, session);
      if (correlation.getMarcTag().equalsIgnoreCase(Global.MATERIAL_TAG_CODE)) {
        final BibliographicLeader bibliographicLeader = ((BibliographicLeader) item.getTag(0));
        final RecordTypeMaterial rtm;
        try {
          rtm = new RecordTypeMaterialDAO().getMaterialHeaderCode(session, bibliographicLeader.getItemRecordTypeCode(), bibliographicLeader.getItemBibliographicLevelCode());
          materialTag.setFormOfMaterial(ofNullable(rtm).map(material -> rtm.getAmicusMaterialTypeCode()).orElse(" "));
          materialTag.setCorrelationValues(new CorrelationValues(rtm.getBibHeader008(), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
          if (field.getFieldStatus() == Field.FieldStatus.CHANGED) {
            catalog.toMaterialDescription(field.getFixedField(), materialTag);
            materialTag.markChanged();
          }

        } catch (HibernateException e) {
          //ignore
        }
      }
    });
  }

  /**
   * Changes a physical description tag for 007 tag.
   *
   * @param item          -- the current catalog item.
   * @param field         -- bibliographic record field.
   * @param bibItemNumber -- the amicus number associated to record.
   */
  public void changePhysicalDescriptionTag(final CatalogItem item, final Field field, final int bibItemNumber) {

    if (field.getFieldStatus() == Field.FieldStatus.CHANGED || field.getFieldStatus() == Field.FieldStatus.DELETED) {
      item.getTags().stream().skip(1).filter(aTag -> aTag.isFixedField() && aTag instanceof PhysicalDescription).forEach(aTag -> {
        final PhysicalDescription physicalTag = (PhysicalDescription) aTag;
        if (physicalTag.getKeyNumber() == field.getFixedField().getKeyNumber()) {
          if (field.getFieldStatus() == Field.FieldStatus.CHANGED) {
            final char gmd = field.getFixedField().getDisplayValue().charAt(0);
            final int headerTypeCode = (field.getFixedField().getHeaderTypeCode() != null) ? field.getFixedField().getHeaderTypeCode() : PhysicalDescription.getInstanceByGMD(gmd).getHeaderType();
            final CorrelationValues correlationValues = new CorrelationValues(headerTypeCode, CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED);
            physicalTag.setCorrelationValues(correlationValues);
            catalog.toPhysicalDescription(field.getFixedField(), physicalTag);
            physicalTag.markChanged();
          } else {
            physicalTag.markDeleted();
            item.getDeletedTags().add(physicalTag);
          }
        }
      });
    } else if (field.getVariableField().getKeyNumber() == null && field.getFieldStatus() == Field.FieldStatus.NEW) {
      addPhysicalDescriptionTag(item, field.getFixedField(), bibItemNumber);
    }
  }

  /**
   * Changes note tags.
   *
   * @param item              -- the current catalog item.
   * @param field             -- bibliographic record field.
   * @param correlationValues -- the new correlation values to set.
   * @param bibItemNumber     -- the amicus number associated to record.
   * @param view              -- the view.
   * @param configuration     -- the configuration.
   */
  public void changeNoteTag(final CatalogItem item, final Field field, final CorrelationValues correlationValues, final int bibItemNumber, final int view, final Map<String, String> configuration) {
    if (field.getFieldStatus() == Field.FieldStatus.CHANGED || field.getFieldStatus() == Field.FieldStatus.DELETED) {
      item.getTags().stream().skip(1).filter(aTag -> !aTag.isFixedField() && aTag instanceof BibliographicNoteTag).forEach(aTag -> {
        final BibliographicNoteTag noteTag = (BibliographicNoteTag) aTag;
        if (noteTag.getNoteNbr() == field.getVariableField().getKeyNumber()) {
          if (field.getFieldStatus() == Field.FieldStatus.CHANGED) {
            noteTag.setCorrelationValues(correlationValues);
            noteTag.setStringText(new StringText(field.getVariableField().getValue()));
            noteTag.markChanged();
          } else {
            noteTag.markDeleted();
            item.getDeletedTags().add(noteTag);
          }
        }
      });
    } else if (field.getVariableField().getKeyNumber() == null && field.getFieldStatus() == Field.FieldStatus.NEW) {
      insertNewVariableField(item, field.getVariableField(), bibItemNumber, correlationValues, configuration, null, view);
    }

  }

  /**
   * Changes a other material description tag for 006 tag.
   *
   * @param item               -- the current catalog item.
   * @param field              -- bibliographic record field.
   * @param session            -- the current hibernate session.
   * @param formOfMaterial     -- the form of material associated.
   * @param generalInformation -- {@linked GeneralInformation} for default values.
   */
  public void changeMaterialDescriptionOtherTag(final CatalogItem item, final Field field, final Session session,
                                                final String formOfMaterial,
                                                final GeneralInformation generalInformation) {

    if (field.getFieldStatus() == Field.FieldStatus.CHANGED || field.getFieldStatus() == Field.FieldStatus.DELETED) {
      item.getTags().stream().skip(1).filter(aTag -> aTag.isFixedField() && aTag instanceof MaterialDescription).forEach(aTag -> {
        final MaterialDescription materialTag = (MaterialDescription) aTag;
        final CorrelationKey correlation = aTag.getTagImpl().getMarcEncoding(aTag, session);
        if (correlation.getMarcTag().equalsIgnoreCase(Global.OTHER_MATERIAL_TAG_CODE) && materialTag.getMaterialDescriptionKeyNumber() == field.getFixedField().getKeyNumber())  {
             if (field.getFieldStatus() == Field.FieldStatus.CHANGED) {
              materialTag.setCorrelationValues(new CorrelationValues(field.getFixedField().getHeaderTypeCode(), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
              catalog.toMaterialDescription(field.getFixedField(), materialTag);
              materialTag.markChanged();
            } else {
              materialTag.markDeleted();
              item.getDeletedTags().add(materialTag);
            }
        }
      });
    } else if (field.getFixedField().getKeyNumber() == null && field.getFieldStatus() == Field.FieldStatus.NEW) {
      addMaterialDescriptionToCatalog(Global.OTHER_MATERIAL_TAG_CODE, item, field.getFixedField(), generalInformation, formOfMaterial);
    }
  }

  @SuppressWarnings("unchecked")
  private boolean checkIfAlreadyExistNote(final int headingNbr, CatalogItem item, final Class clazz) {
    return item.getTags().stream().filter(aTag -> clazz.isAssignableFrom(aTag.getClass()))
      .anyMatch(t -> {
        BibliographicNoteTag noteTag = (BibliographicNoteTag) t;
        return noteTag.getNote().getNoteNbr() == headingNbr;
      });
  }

  @SuppressWarnings("unchecked")
  private boolean checkIfAlreadyExist(final int headingNbr, CatalogItem item, final Class clazz) {
    return item.getTags().stream().filter(aTag -> clazz.isAssignableFrom(aTag.getClass()))
      .anyMatch(t -> {
        AccessPoint apf = (AccessPoint) t;
        return apf.getHeadingNumber() == headingNbr;
      });
  }

  /**
   * Populate and add to catalog a {@link MaterialDescription} object for saving record.
   *
   * @param tagNbr         -- the tag number field.
   * @param item           -- the item to add tags.
   * @param fixedField     -- the fixed field containing data.
   * @param giAPI          -- the {@link GeneralInformation}.
   * @param formOfMaterial -- the formOfMaterial string.
   */
  public void addMaterialDescriptionToCatalog(final String tagNbr,
                                              final CatalogItem item,
                                              final org.folio.marccat.resources.domain.FixedField fixedField,
                                              final GeneralInformation giAPI,
                                              final String formOfMaterial) {

    final MaterialDescription bibMaterial = catalog.createRequiredMaterialDescriptionTag(item);
    bibMaterial.setCartographicMaterial(giAPI.getCartographicMaterial());

    final String materialDescription008Indicator = tagNbr.equals(Global.MATERIAL_TAG_CODE) ? "1" : "0";
    bibMaterial.setMaterialDescription008Indicator(materialDescription008Indicator);
    final int headerTypeCode = fixedField.getHeaderTypeCode();
    final CorrelationValues correlationValues = new CorrelationValues(headerTypeCode, CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED);
    bibMaterial.setCorrelationValues(correlationValues);
    bibMaterial.setFormOfMaterial(formOfMaterial);
    catalog.toMaterialDescription(fixedField, bibMaterial);
    bibMaterial.markNew();
    item.addTag(bibMaterial);
  }

  /**
   * Sets default values to MaterialDescription class.
   *
   * @param giAPI -- the general information from API.
   * @param md    -- the MaterialDescription string.
   */
  public void setDefaultValues(final GeneralInformation giAPI, MaterialDescription md) {

    md.setRecordModifiedCode(giAPI.getRecordModifiedCode().charAt(0));
    md.setRecordCataloguingSourceCode(giAPI.getRecordCataloguingSourceCode().charAt(0));
    md.setItemDateTypeCode(giAPI.getItemDateTypeCode().charAt(0));
    md.setLanguageCode(giAPI.getLanguageCode());
    md.setItemDateFirstPublication(Global.ITEM_DATE_FIRST_PUBLICATION);
    md.setItemDateLastPublication(Global.ITEM_DATE_LAST_PUBLICATION);
    md.setMarcCountryCode(giAPI.getMarcCountryCode());

    md.setBookIllustrationCode(giAPI.getBookIllustrationCode());
    md.setTargetAudienceCode(giAPI.getTargetAudienceCode());
    md.setFormOfItemCode(giAPI.getFormOfItemCode());
    md.setNatureOfContentsCode(giAPI.getNatureOfContentsCode());
    md.setConferencePublicationCode(giAPI.getConferencePublicationCode());
    md.setBookFestschrift(giAPI.getBookFestschrift());
    md.setBookIndexAvailabilityCode(giAPI.getBookIndexAvailabilityCode());
    md.setBookLiteraryFormTypeCode(giAPI.getBookLiteraryFormTypeCode());
    md.setBookBiographyCode(giAPI.getBookBiographyCode());
    md.setGovernmentPublicationCode(giAPI.getGovernmentPublicationCode());
    md.setComputerTargetAudienceCode(giAPI.getComputerTargetAudienceCode());
    md.setComputerFileTypeCode(giAPI.getComputerFileTypeCode());
    md.setComputerFileFormCode(giAPI.getComputerFileFormCode());
    md.setCartographicIndexAvailabilityCode("0");
    md.setCartographicReliefCode(giAPI.getCartographicReliefCode());
    md.setCartographicProjectionCode(giAPI.getCartographicProjectionCode());
    md.setCartographicMaterial(giAPI.getCartographicMaterial());
    md.setCartographicFormatCode(giAPI.getCartographicFormatCode());
    md.setMusicFormOfCompositionCode(giAPI.getMusicFormOfCompositionCode());
    md.setMusicFormatCode(giAPI.getMusicFormatCode());
    md.setMusicTextualMaterialCode(giAPI.getMusicTextualMaterialCode());
    md.setMusicLiteraryTextCode(giAPI.getMusicLiteraryTextCode());
    md.setMusicPartsCode(giAPI.getMusicPartsCode());
    md.setMusicTranspositionArrangementCode(giAPI.getMusicTranspositionArrangementCode());
    md.setSerialFrequencyCode(giAPI.getSerialFrequencyCode());
    md.setSerialRegularityCode(giAPI.getSerialRegularityCode());
    md.setSerialTypeCode(giAPI.getSerialTypeCode());
    md.setSerialFormOriginalItemCode(giAPI.getSerialFormOriginalItemCode());
    md.setSerialOriginalAlphabetOfTitleCode(giAPI.getSerialOriginalAlphabetOfTitleCode());
    md.setSerialCumulativeIndexCode(giAPI.getSerialEntryConventionCode());
    md.setVisualRunningTime(giAPI.getVisualRunningTime());
    md.setVisualTargetAudienceCode(giAPI.getVisualTargetAudienceCode());
    md.setVisualMaterialTypeCode(giAPI.getVisualMaterialTypeCode());
    md.setVisualTechniqueCode(giAPI.getVisualTechniqueCode());

  }

  /**
   * Insert a new physical description tag.
   *
   * @param item          -- the item to add tags.
   * @param ff            -- the fixed field containing data.
   * @param bibItemNumber -- the bibliographic item number.
   */
  public void addPhysicalDescriptionTag(final CatalogItem item,
                                        final org.folio.marccat.resources.domain.FixedField ff,
                                        final int bibItemNumber) {

    final char gmd = ff.getDisplayValue().charAt(0);
    final int headerTypeCode = (ff.getHeaderTypeCode() != null) ? ff.getHeaderTypeCode() : PhysicalDescription.getInstanceByGMD(gmd).getHeaderType();
    final CorrelationValues correlationValues = new CorrelationValues(headerTypeCode, CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED);
    final PhysicalDescription pd = catalog.createPhysicalDescriptionTag(item, correlationValues);
    catalog.toPhysicalDescription(ff, pd);
    pd.markNew();
    pd.setBibItemNumber(bibItemNumber);
    item.addTag(pd);

  }

  /**
   * Changes access point for variable record fields.
   *
   * @param item              -- the item to add tags.
   * @param field             -- bibliographic record field.
   * @param correlationValues -- the selection of correlation values.
   * @param bibItemNumber     -- the bibliographic item number.
   * @param view              -- the view.
   * @param session           -- the current hibernate session.
   * @param configuration     -- the configuration.
   * @throws HibernateException -- in case of hibernate exception.
   * @throws SQLException       -- in case of sql exception.
   */
  public void changeAccessPointTag(final CatalogItem item, final Field field, final CorrelationValues correlationValues,
                                   final int bibItemNumber, final int view, final Session session, final Map <String, String> configuration) throws HibernateException, SQLException {

    if (field.getFieldStatus() == Field.FieldStatus.CHANGED || field.getFieldStatus() == Field.FieldStatus.DELETED) {
      item.getTags().stream().filter(aTag -> aTag instanceof BibliographicAccessPoint && aTag.getCategory() == field.getVariableField().getCategoryCode()).forEach(aTag -> {
        BibliographicAccessPoint acs = (BibliographicAccessPoint) aTag;
        int keyNumber = ((BibliographicAccessPoint) aTag).getDescriptor().getKey().getHeadingNumber();
        if (keyNumber == field.getVariableField().getKeyNumber()) {
          if (field.getFieldStatus() == Field.FieldStatus.CHANGED) {
            acs.setCorrelationValues(correlationValues);
            acs.setAccessPointStringText(new StringText(field.getVariableField().getValue()));
            acs.setSequenceNumber(field.getVariableField().getSequenceNumber());

            if (field.getVariableField().getNewKeyNumber() != null && keyNumber != field.getVariableField().getNewKeyNumber()) {
              try {
                Descriptor descriptorNew = acs.getDAODescriptor().findOrCreateMyView(field.getVariableField().getNewKeyNumber(), View.makeSingleViewString(item.getUserView()), view, session);
                MarcCommandLibrary.replaceDescriptor(item, acs, descriptorNew);
              } catch (HibernateException e) {
                throw new ModMarccatException(e);
              }
            } else {
              acs.markChanged();
            }

          } else {
            acs.markDeleted();
            item.getDeletedTags().add(acs);
          }
        }
      });
    } else if (field.getVariableField().getKeyNumber() != null && field.getFieldStatus() == Field.FieldStatus.NEW) {
      insertNewVariableField(item, field.getVariableField(), bibItemNumber, correlationValues, configuration, session, view);
    }
  }

  /**
   * Changes or deletes publisher tags.
   *
   * @param item              -- the item to add tags.
   * @param field             -- bibliographic record field.
   * @param correlationValues -- the selection of correlation values.
   * @param bibItemNumber     -- the bibliographic item number.
   * @param view              -- the cataloguing user view.
   * @param session           -- the hibernate session associated.
   * @param configuration     -- the configuration.
   * @throws HibernateException -- in case of hibernate exception.
   * @throws SQLException       -- in case of sql exception.
   */
  public void changePublisherTag(final CatalogItem item, final Field field, final CorrelationValues correlationValues,
                                 final int bibItemNumber, final int view, final Session session, final Map<String, String> configuration) throws HibernateException, SQLException {

    if (field.getFieldStatus() == Field.FieldStatus.CHANGED || field.getFieldStatus() == Field.FieldStatus.DELETED) {
      item.getTags().stream().filter(aTag -> aTag instanceof PublisherManager && aTag.getCategory() == field.getVariableField().getCategoryCode()).forEach(aTag -> {
        try {
          PublisherManager publisherManager = (PublisherManager) aTag;
          if (field.getFieldStatus() == Field.FieldStatus.CHANGED) {
            updatePublisherToCatalog(publisherManager, field.getVariableField(), view, configuration, session);
          }
          else {
            publisherManager.markDeleted();
            item.getDeletedTags().add(publisherManager);
          }
        } catch (HibernateException | SQLException e) {
          throw new ModMarccatException(e);
        }
      });
    }

    else if (field.getVariableField().getKeyNumber() != null && field.getFieldStatus() == Field.FieldStatus.NEW) {
      insertNewVariableField(item, field.getVariableField(), bibItemNumber, correlationValues, configuration, session, view);
    }
  }

  /**
   * Insert of a new variable field.
   *
   * @param item              -- the item to add tags.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   * @param correlationValues -- the selection of correlation values.
   * @param configuration     -- the configuration.
   * @param session           -- the current hibernate session.
   * @param view              -- the view.
   */
  public void insertNewVariableField(final CatalogItem item,
                                     final org.folio.marccat.resources.domain.VariableField variableField,
                                     final int bibItemNumber,
                                     final CorrelationValues correlationValues,
                                     Map <String, String> configuration,
                                     final Session session,
                                     final int view) {

    if (variableField.getCategoryCode() == Global.TITLE_CATEGORY) {
      if (!checkIfAlreadyExist(variableField.getKeyNumber(), item, TitleAccessPoint.class))
        addTitleToCatalog(item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode() == Global.NAME_CATEGORY) {
      if (!checkIfAlreadyExist(variableField.getKeyNumber(), item, NameAccessPoint.class))
        addNameToCatalog(item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode() == Global.CONTROL_NUMBER_CATEGORY) {
      if (!checkIfAlreadyExist(variableField.getKeyNumber(), item, ControlNumberAccessPoint.class))
        addControlFieldToCatalog(item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode() == Global.CLASSIFICATION_CATEGORY) {
      if (!checkIfAlreadyExist(variableField.getKeyNumber(), item, ClassificationAccessPoint.class))
        addClassificationToCatalog(item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode() == Global.SUBJECT_CATEGORY) {
      if (!checkIfAlreadyExist(variableField.getKeyNumber(), item, SubjectAccessPoint.class))
        addSubjectToCatalog(item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode() == Global.BIB_NOTE_CATEGORY && correlationValues.getValue(1) != Global.PUBLISHER_DEFAULT_NOTE_TYPE) {
      if (!checkIfAlreadyExistNote(variableField.getKeyNumber(), item, BibliographicNoteTag.class))
        addNoteToCatalog(item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode() == Global.PUBLISHER_CATEGORY && correlationValues.getValue(1) == Global.PUBLISHER_DEFAULT_NOTE_TYPE) {

      try {
        addPublisherToCatalog(item, correlationValues, variableField, view, configuration, session);
      } catch (HibernateException | SQLException e) {
        throw new DataAccessException(e);
      }
    }
  }

  /**
   * Creates and add to catalog a new persistent {@link PublisherManager} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param view              -- the view.
   * @param configuration     -- the configuration.
   * @param session           -- the current hibernate session.
   * @throws HibernateException -- in case of hibernate exception.
   * @throws DataAccessException -- in case of data access exception.
   */
  private void addPublisherToCatalog(final CatalogItem item,
                                     final CorrelationValues correlationValues,
                                     final org.folio.marccat.resources.domain.VariableField variableField,
                                     final int view,
                                     final Map<String, String> configuration,
                                     final Session session) throws HibernateException, SQLException {
    boolean exist = item.getTags().stream()
      .filter(aTag -> PublisherManager.class.isAssignableFrom(aTag.getClass()))
      .anyMatch(t -> {
        PublisherManager pm = (PublisherManager) t;
        PublisherAccessPoint apf = pm.getApf();
        int pTagNumber = apf.getDescriptor().getKey().getHeadingNumber();
        return pTagNumber == variableField.getKeyNumber();
      });

    if (exist)
      return;

    final PublisherManager publisherManager = catalog.createPublisherTag(item, correlationValues);

    publisherManager.setUserViewString(View.makeSingleViewString(view));
    final StringText st = new StringText(variableField.getValue());

    publisherManager.setStringText(st);
    List<PUBL_TAG> publisherTagUnits =  publisherManager.getPublisherTagUnits();
    int idx = 1;
    for (PUBL_TAG ptag : publisherTagUnits) {
      Descriptor descriptor = ptag.getDescriptor();
      descriptor.setUserViewString(View.makeSingleViewString(view));
      descriptor.setConfigValues(configuration);
      final Descriptor descriptorNew = ((DAODescriptor) (descriptor.getDAO())).getMatchingHeading(descriptor, session);
      if(descriptorNew != null) {
        ptag.setDescriptor((PUBL_HDG) descriptorNew);
        ptag.setPublisherHeadingNumber(ptag.getDescriptor().getKey().getHeadingNumber());
        ptag.setSequenceNumber(idx ++);
        ptag.markNew();
      }
    }
     item.addTag(publisherManager);
  }

  /**
    * Updates a new persistent {@link PublisherManager} object for saving record.
    *
    * @param publisherManager   -- the publisher manager to update.
    * @param variableField      -- the variable field containing data.
    * @param view               -- the view.
    * @param configuration     -- the configuration.
    * @param session           -- the current hibernate session.
    * @throws HibernateException -- in case of hibernate exception.
    * @throws DataAccessException in case of data access exception.
    */
  private void updatePublisherToCatalog(final PublisherManager publisherManager,
                                        final org.folio.marccat.resources.domain.VariableField variableField,
                                        final int view,
                                        final Map<String, String> configuration,
                                       final Session session) throws HibernateException, SQLException {
    final StringText st = new StringText(variableField.getValue());
    publisherManager.setStringText(st);
    final List<PUBL_TAG> publisherTagUnits =  publisherManager.getPublisherTagUnits();
    int idx = 1;
    for (PUBL_TAG ptag : publisherTagUnits) {
      final Descriptor descriptor = ptag.getDescriptor();
      descriptor.setUserViewString(View.makeSingleViewString(view));
      descriptor.setConfigValues(configuration);
      final Descriptor descriptorNew = ((DAODescriptor) (descriptor.getDAO())).getMatchingHeading(descriptor, session);
      if(descriptorNew != null) {
        ptag.setDescriptor((PUBL_HDG) descriptorNew);
        ptag.setUserViewString(View.makeSingleViewString(view));
        ptag.setSequenceNumber(idx ++);
        ptag.markChanged();
      }
    }
    publisherManager.getApf().markChanged();
    publisherManager.markChanged();

  }

  /**
   * Creates and add to catalog a new persistent {@link BibliographicNoteTag} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
    */
  private void addNoteToCatalog(final CatalogItem item,
                                final CorrelationValues correlationValues,
                                final org.folio.marccat.resources.domain.VariableField variableField,
                                final int bibItemNumber){
    final BibliographicNoteTag nTag = catalog.createBibliographicNoteTag(item, correlationValues);
    nTag.getNote().setContent(variableField.getValue());
    if (variableField.getKeyNumber() != null && variableField.getKeyNumber() != 0)
      nTag.getNote().setNoteNbr(variableField.getKeyNumber());

    nTag.setItemNumber(bibItemNumber);
    nTag.markNew();
    item.addTag(nTag);
  }

  /**
   * Creates and add to catalog a new persistent {@link SubjectAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   */
  private void addSubjectToCatalog(final CatalogItem item,
                                   final CorrelationValues correlationValues,
                                   final org.folio.marccat.resources.domain.VariableField variableField,
                                   final int bibItemNumber) {
    final SubjectAccessPoint sap = catalog.createSubjectAccessPoint(item, correlationValues);
    sap.setAccessPointStringText(new StringText(variableField.getValue()));
    sap.setHeadingNumber(variableField.getKeyNumber());
    sap.setItemNumber(bibItemNumber);
    sap.markNew();
    item.addTag(sap);
  }

  /**
   * Creates and add to catalog a new persistent {@link ClassificationAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   */
  private void addClassificationToCatalog(final CatalogItem item,
                                          final CorrelationValues correlationValues,
                                          final org.folio.marccat.resources.domain.VariableField variableField,
                                          final int bibItemNumber) {
    final ClassificationAccessPoint clap = catalog.createClassificationAccessPoint(item, correlationValues);
    clap.setAccessPointStringText(new StringText(variableField.getValue()));
    clap.setHeadingNumber(variableField.getKeyNumber());
    clap.setItemNumber(bibItemNumber);
    clap.markNew();
    item.addTag(clap);
  }

  /**
   * Creates and add to catalog a new persistent {@link ControlNumberAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   */
  private void addControlFieldToCatalog(final CatalogItem item,
                                        final CorrelationValues correlationValues,
                                        final org.folio.marccat.resources.domain.VariableField variableField,
                                        final int bibItemNumber) {
    final ControlNumberAccessPoint cnap = catalog.createControlNumberAccessPoint(item, correlationValues);
    cnap.setAccessPointStringText(new StringText(variableField.getValue()));
    cnap.setHeadingNumber(variableField.getKeyNumber());
    cnap.setItemNumber(bibItemNumber);
    cnap.markNew();
    item.addTag(cnap);
  }

  /**
   * Creates and add to catalog a new persistent {@link NameAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
    */
  private void addNameToCatalog(final CatalogItem item,
                                final CorrelationValues correlationValues,
                                final org.folio.marccat.resources.domain.VariableField variableField,
                                final int bibItemNumber) {
    final NameAccessPoint nap = catalog.createNameAccessPointTag(item, correlationValues);
    nap.setAccessPointStringText(new StringText(variableField.getValue()));
    nap.setHeadingNumber(variableField.getKeyNumber());
    nap.setItemNumber(bibItemNumber);
    nap.markNew();
    item.addTag(nap);
  }

  /**
   * Creates and add to catalog a new persistent {@link TitleAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   */
  private void addTitleToCatalog(final CatalogItem item,
                                 final CorrelationValues correlationValues,
                                 final org.folio.marccat.resources.domain.VariableField variableField,
                                 final int bibItemNumber) {

    final TitleAccessPoint tap = catalog.createTitleAccessPointTag(item, correlationValues);
    tap.setAccessPointStringText(new StringText(variableField.getValue()));
    tap.setHeadingNumber(variableField.getKeyNumber());
    tap.setItemNumber(bibItemNumber);
    tap.markNew();
    item.addTag(tap);
  }

}
