package org.folio.cataloging.integration.record;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicCatalog;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCommandLibrary;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.descriptor.PublisherTagDescriptor;
import org.folio.cataloging.dao.RecordTypeMaterialDAO;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.resources.domain.Field;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.shared.GeneralInformation;
import org.folio.cataloging.util.StringText;

import java.sql.SQLException;
import java.util.List;

import static java.util.Optional.ofNullable;


public class RecordParser {

  static final BibliographicCatalog catalog = new BibliographicCatalog ( );

  /**
   * Change leader tag.
   *
   * @param item   -- the current catalog item.
   * @param leader -- the new leader string values.
   */
  public void changeLeader(final CatalogItem item, final String leader) {
    final BibliographicLeader bibliographicLeader = ((BibliographicLeader) item.getTag (0));
    final String leaderString = bibliographicLeader.getDisplayString ( );
    final String valuesToCompare = leaderString.substring (5, 10) + leaderString.substring (17, 20);
    final String newLeader = leader.substring (5, 10) + leader.substring (17, 20);

    if (!valuesToCompare.equals (newLeader)) {
      catalog.toBibliographicLeader (leader, bibliographicLeader);
      bibliographicLeader.markChanged ( );
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
    item.getTags ( ).stream ( ).filter (aTag -> aTag.isFixedField ( ) && aTag instanceof MaterialDescription).forEach (aTag -> {
      final MaterialDescription materialTag = (MaterialDescription) aTag;
      final CorrelationKey correlation = aTag.getTagImpl ( ).getMarcEncoding (aTag, session);
      if (correlation.getMarcTag ( ).equalsIgnoreCase (GlobalStorage.MATERIAL_TAG_CODE)) {
        final BibliographicLeader bibliographicLeader = ((BibliographicLeader) item.getTag (0));
        final RecordTypeMaterial rtm;
        try {
          rtm = new RecordTypeMaterialDAO().getMaterialHeaderCode(session, bibliographicLeader.getItemRecordTypeCode(), bibliographicLeader.getItemBibliographicLevelCode());
          materialTag.setFormOfMaterial(ofNullable(rtm).map(material -> rtm.getAmicusMaterialTypeCode()).orElse(" "));
          materialTag.setCorrelationValues (new CorrelationValues (rtm.getBibHeader008(), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
          catalog.toMaterialDescription (field.getFixedField(), materialTag);
          //materialTag.setPersistenceState(UpdateStatus.CHANGED);
          materialTag.markChanged ( );

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

    if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED || field.getFieldStatus ( ) == Field.FieldStatus.DELETED) {
      item.getTags ( ).stream ( ).skip (1).filter (aTag -> aTag.isFixedField ( ) && aTag instanceof PhysicalDescription).forEach (aTag -> {
        final PhysicalDescription physicalTag = (PhysicalDescription) aTag;
        if (physicalTag.getKeyNumber ( ) == field.getFixedField ( ).getKeyNumber ( )) {
          if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED) {
            final char gmd = field.getFixedField().getDisplayValue().charAt(0);
            final int headerTypeCode = (field.getFixedField().getHeaderTypeCode()!=null) ?field.getFixedField().getHeaderTypeCode() :PhysicalDescription.getInstanceByGMD(gmd).getHeaderType();
            final CorrelationValues correlationValues = new CorrelationValues (headerTypeCode, CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED);
            physicalTag.setCorrelationValues (correlationValues);
            catalog.toPhysicalDescription (field.getFixedField ( ), physicalTag);
            physicalTag.markChanged ( );
          } else {
            physicalTag.markDeleted ( );
            item.getDeletedTags ( ).add (physicalTag);
          }
        }
      });
    } else if (field.getVariableField ( ).getKeyNumber ( ) == null && field.getFieldStatus ( ) == Field.FieldStatus.NEW) {
      addPhysicalDescriptionTag (item, field.getFixedField ( ), bibItemNumber);
    }
  }

  /**
   * Changes note tags.
   *
   * @param item              -- the current catalog item.
   * @param field             -- bibliographic record field.
   * @param correlationValues -- the new correlation values to set.
   * @param bibItemNumber     -- the amicus number associated to record.
   */
  public void changeNoteTag(final CatalogItem item, final Field field, final CorrelationValues correlationValues, final int bibItemNumber, final int view) throws HibernateException, SQLException {
    if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED || field.getFieldStatus ( ) == Field.FieldStatus.DELETED) {
      item.getTags ( ).stream ( ).skip (1).filter (aTag -> !aTag.isFixedField ( ) && aTag instanceof BibliographicNoteTag).forEach (aTag -> {
        final BibliographicNoteTag noteTag = (BibliographicNoteTag) aTag;
        if (noteTag.getNoteNbr ( ) == field.getVariableField ( ).getKeyNumber ( )) {
          if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED) {
            noteTag.setCorrelationValues (correlationValues);
            noteTag.setStringText (new StringText (field.getVariableField ( ).getValue ( )));
            noteTag.markChanged ( );
          } else {
            noteTag.markDeleted ( );
            item.getDeletedTags ( ).add (noteTag);
          }
        }
      });
    } else if (field.getVariableField ( ).getKeyNumber ( ) == null && field.getFieldStatus ( ) == Field.FieldStatus.NEW) {
      insertNewVariableField (item, field.getVariableField ( ), bibItemNumber, correlationValues, null, view);
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

    if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED || field.getFieldStatus ( ) == Field.FieldStatus.DELETED) {
      item.getTags ( ).stream ( ).skip (1).filter (aTag -> aTag.isFixedField ( ) && aTag instanceof MaterialDescription).forEach (aTag -> {
        final MaterialDescription materialTag = (MaterialDescription) aTag;
        final CorrelationKey correlation = aTag.getTagImpl ( ).getMarcEncoding (aTag, session);
        if (correlation.getMarcTag ( ).equalsIgnoreCase (GlobalStorage.OTHER_MATERIAL_TAG_CODE)) {
          if (materialTag.getMaterialDescriptionKeyNumber ( ) == field.getFixedField ( ).getKeyNumber ( )) {
            if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED) {
              materialTag.setCorrelationValues (new CorrelationValues (field.getFixedField ( ).getHeaderTypeCode ( ), CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED));
              catalog.toMaterialDescription (field.getFixedField ( ), materialTag);
              materialTag.markChanged ( );
            } else {
              materialTag.markDeleted ( );
              item.getDeletedTags ( ).add (materialTag);
            }
          }
        }
      });
    } else if (field.getFixedField ( ).getKeyNumber ( ) == null && field.getFieldStatus ( ) == Field.FieldStatus.NEW) {
      addMaterialDescriptionToCatalog (GlobalStorage.OTHER_MATERIAL_TAG_CODE, item, field.getFixedField ( ), generalInformation, formOfMaterial);
    }
  }

  /**
   * Populate and add to catalog a {@link MaterialDescription} object for saving record.
   *
   * @param tagNbr         -- the tag number field.
   * @param item           -- the item to add tags.
   * @param fixedField     -- the fixed field containing data.
   * @param giAPI          -- the {@link GeneralInformation}.
   * @param formOfMaterial -- the formOfMaterial string.
   * @throws DataAccessException in case of data access exception.
   */
  public void addMaterialDescriptionToCatalog(final String tagNbr,
                                              final CatalogItem item,
                                              final org.folio.cataloging.resources.domain.FixedField fixedField,
                                              final GeneralInformation giAPI,
                                              final String formOfMaterial) {

    final MaterialDescription bibMaterial = catalog.createRequiredMaterialDescriptionTag (item);
    bibMaterial.setCartographicMaterial (giAPI.getCartographicMaterial ( ));

    final String materialDescription008Indicator = tagNbr.equals (GlobalStorage.MATERIAL_TAG_CODE) ? "1" : "0";
    bibMaterial.setMaterialDescription008Indicator (materialDescription008Indicator);
    final int headerTypeCode = fixedField.getHeaderTypeCode();
    final CorrelationValues correlationValues = new CorrelationValues (headerTypeCode, CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED);
    bibMaterial.setCorrelationValues (correlationValues);
    bibMaterial.setFormOfMaterial (formOfMaterial);
    catalog.toMaterialDescription (fixedField, bibMaterial);
    bibMaterial.markNew ( );
    item.addTag (bibMaterial);
  }

  /**
   * Sets default values to MaterialDescription class.
   *
   * @param giAPI -- the general information from API.
   */
  private void setDefaultValues(final GeneralInformation giAPI, MaterialDescription md) {

    md.setRecordModifiedCode (giAPI.getRecordModifiedCode ( ).charAt (0));
    md.setRecordCataloguingSourceCode (giAPI.getRecordCataloguingSourceCode ( ).charAt (0));
    md.setItemDateTypeCode (giAPI.getItemDateTypeCode ( ).charAt (0));
    md.setLanguageCode (giAPI.getLanguageCode ( ));
    md.setItemDateFirstPublication (GlobalStorage.ITEM_DATE_FIRST_PUBLICATION);
    md.setItemDateLastPublication (GlobalStorage.ITEM_DATE_LAST_PUBLICATION);
    md.setMarcCountryCode (giAPI.getMarcCountryCode ( ));

    md.setBookIllustrationCode (giAPI.getBookIllustrationCode ( ));
    md.setTargetAudienceCode (giAPI.getTargetAudienceCode ( ));
    md.setFormOfItemCode (giAPI.getFormOfItemCode ( ));
    md.setNatureOfContentsCode (giAPI.getNatureOfContentsCode ( ));
    md.setConferencePublicationCode (giAPI.getConferencePublicationCode ( ));
    md.setBookFestschrift (giAPI.getBookFestschrift ( ));
    md.setBookIndexAvailabilityCode (giAPI.getBookIndexAvailabilityCode ( ));
    md.setBookLiteraryFormTypeCode (giAPI.getBookLiteraryFormTypeCode ( ));
    md.setBookBiographyCode (giAPI.getBookBiographyCode ( ));
    md.setGovernmentPublicationCode (giAPI.getGovernmentPublicationCode ( ));
    md.setComputerTargetAudienceCode (giAPI.getComputerTargetAudienceCode ( ));
    md.setComputerFileTypeCode (giAPI.getComputerFileTypeCode ( ));
    md.setComputerFileFormCode (giAPI.getComputerFileFormCode ( ));
    md.setCartographicIndexAvailabilityCode ("0");
    md.setCartographicReliefCode (giAPI.getCartographicReliefCode ( ));
    md.setCartographicProjectionCode (giAPI.getCartographicProjectionCode ( ));
    md.setCartographicMaterial (giAPI.getCartographicMaterial ( ));
    md.setCartographicFormatCode (giAPI.getCartographicFormatCode ( ));
    md.setMusicFormOfCompositionCode (giAPI.getMusicFormOfCompositionCode ( ));
    md.setMusicFormatCode (giAPI.getMusicFormatCode ( ));
    md.setMusicTextualMaterialCode (giAPI.getMusicTextualMaterialCode ( ));
    md.setMusicLiteraryTextCode (giAPI.getMusicLiteraryTextCode ( ));
    md.setMusicPartsCode (giAPI.getMusicPartsCode ( ));
    md.setMusicTranspositionArrangementCode (giAPI.getMusicTranspositionArrangementCode ( ));
    md.setSerialFrequencyCode (giAPI.getSerialFrequencyCode ( ));
    md.setSerialRegularityCode (giAPI.getSerialRegularityCode ( ));
    md.setSerialTypeCode (giAPI.getSerialTypeCode ( ));
    md.setSerialFormOriginalItemCode (giAPI.getSerialFormOriginalItemCode ( ));
    md.setSerialOriginalAlphabetOfTitleCode (giAPI.getSerialOriginalAlphabetOfTitleCode ( ));
    md.setSerialCumulativeIndexCode (giAPI.getSerialEntryConventionCode ( ));
    md.setVisualRunningTime (giAPI.getVisualRunningTime ( ));
    md.setVisualTargetAudienceCode (giAPI.getVisualTargetAudienceCode ( ));
    md.setVisualMaterialTypeCode (giAPI.getVisualMaterialTypeCode ( ));
    md.setVisualTechniqueCode (giAPI.getVisualTechniqueCode ( ));

  }

  /**
   * Insert a new physical description tag.
   *
   * @param item          -- the item to add tags.
   * @param ff            -- the fixed field containing data.
   * @param bibItemNumber -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  public void addPhysicalDescriptionTag(final CatalogItem item,
                                        final org.folio.cataloging.resources.domain.FixedField ff,
                                        final int bibItemNumber) throws DataAccessException {

    final char gmd = ff.getDisplayValue().charAt(0);
    final int headerTypeCode = (ff.getHeaderTypeCode()!=null) ?ff.getHeaderTypeCode() :PhysicalDescription.getInstanceByGMD(gmd).getHeaderType();
    final CorrelationValues correlationValues = new CorrelationValues (headerTypeCode, CorrelationValues.UNDEFINED, CorrelationValues.UNDEFINED);
    final PhysicalDescription pd = catalog.createPhysicalDescriptionTag (item, correlationValues);
    catalog.toPhysicalDescription (ff, pd);
    pd.markNew();
    pd.setBibItemNumber(bibItemNumber);
    item.addTag (pd);

  }

  /**
   * Changes access point for variable record fields.
   *
   * @param item              -- the item to add tags.
   * @param field             -- bibliographic record field.
   * @param correlationValues -- the selection of correlation values.
   * @param bibItemNumber     -- the bibliographic item number.
   * @throws HibernateException -- in case of hibernate exception.
   * @throws SQLException       -- in case of sql exception.
   */
  public void changeAccessPointTag(final CatalogItem item, final Field field, final CorrelationValues correlationValues,
                                   final int bibItemNumber, final int view, final Session session) throws HibernateException, SQLException {

    if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED || field.getFieldStatus ( ) == Field.FieldStatus.DELETED) {
      item.getTags ( ).stream ( ).filter (aTag -> aTag instanceof BibliographicAccessPoint && aTag.getCategory ( ) == field.getVariableField ( ).getCategoryCode ( )).forEach (aTag -> {
        BibliographicAccessPoint acs = (BibliographicAccessPoint) aTag;
        int keyNumber = ((BibliographicAccessPoint) aTag).getDescriptor ( ).getKey ( ).getHeadingNumber ( );
        if (keyNumber == field.getVariableField ( ).getKeyNumber ( )) {
          if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED) {
            acs.setCorrelationValues (correlationValues);
            acs.setAccessPointStringText (new StringText (field.getVariableField ( ).getValue ( )));
            acs.setSequenceNumber (field.getVariableField ( ).getSequenceNumber ( ));

            if (field.getVariableField ( ).getNewKeyNumber ( ) != null && keyNumber != field.getVariableField ( ).getNewKeyNumber ( )) {
              try {
                Descriptor descriptorNew = acs.getDAODescriptor ( ).findOrCreateMyView (field.getVariableField ( ).getNewKeyNumber ( ), View.makeSingleViewString (item.getUserView ( )), view, session);
                MarcCommandLibrary.replaceDescriptor (item, acs, descriptorNew);
              } catch (HibernateException e) {
                throw new RuntimeException (e);
              }
            } else
              acs.markChanged ( );

          } else {
            acs.markDeleted ( );
            item.getDeletedTags ( ).add (acs);
          }
        }
      });
    } else if (field.getVariableField ( ).getKeyNumber ( ) != null && field.getFieldStatus ( ) == Field.FieldStatus.NEW) {
      insertNewVariableField (item, field.getVariableField ( ), bibItemNumber, correlationValues, session, view);
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
   * @throws HibernateException -- in case of hibernate exception.
   * @throws SQLException       -- in case of sql exception.
   */
  public void changePublisherTag(final CatalogItem item, final Field field, final CorrelationValues correlationValues,
                                 final int bibItemNumber, final int view, final Session session) throws HibernateException, SQLException {

    if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED || field.getFieldStatus ( ) == Field.FieldStatus.DELETED) {
      StringText st = new StringText (field.getVariableField ( ).getValue ( ));

      item.getTags ( ).stream ( ).filter (aTag -> aTag instanceof PublisherManager && aTag.getCategory ( ) == field.getVariableField ( ).getCategoryCode ( )).forEach (aTag -> {
        Integer newKeyNumber = field.getVariableField ( ).getNewKeyNumber ( );
        PublisherManager pm = (PublisherManager) aTag;
        PublisherAccessPoint apf = pm.getApf ( );
        int pTagNumber = apf.getDescriptor ( ).getKey ( ).getHeadingNumber ( );
        List <PUBL_TAG> publTags = ((PublisherTagDescriptor) apf.getDescriptor ( )).getPublisherTagUnits ( );
        for ( PUBL_TAG pTag : publTags ) {
          if (pTag.getPublisherTagNumber ( ) == pTagNumber) {
            int keyNumber = pTag.getPublisherHeadingNumber ( );
            if (keyNumber == field.getVariableField ( ).getKeyNumber ( )) {
              if (field.getFieldStatus ( ) == Field.FieldStatus.CHANGED) {
                pm.setCorrelationValues (correlationValues);
                apf.setAccessPointStringText (new StringText (field.getVariableField ( ).getValue ( )));
                apf.setSequenceNumber (field.getVariableField ( ).getSequenceNumber ( ));
                if (newKeyNumber != null && keyNumber != newKeyNumber) {
                  Descriptor descriptorNew = null;
                  try {
                    descriptorNew = pTag.getDescriptorDAO ( ).findOrCreateMyView (newKeyNumber,
                      View.makeSingleViewString (item.getUserView ( )), view, session);

                    pTag.setDescriptor ((PUBL_HDG) descriptorNew);
                    pTag.setUserViewString (View.makeSingleViewString (view));
                    pTag.setOtherSubfields (st.getSubfieldsWithCodes ("c").toString ( ));
                    pTag.markNew ( );

                    MarcCommandLibrary.setNewStringText (pm, st, View.makeSingleViewString (view), session);

                  } catch (HibernateException | SQLException e) {
                    throw new RuntimeException (e);
                  }
                  apf.setUserViewString (View.makeSingleViewString (view));
                  pm.setApf (apf);

                } else {
                  pTag.setOtherSubfields (st.getSubfieldsWithCodes ("c").toString ( ));
                  apf.markChanged ( );
                }
              } else {
                apf.markDeleted ( );
                item.getDeletedTags ( ).add (apf);
              }
              break;
            }
          }
        }
      });
    } else if (field.getVariableField ( ).getKeyNumber ( ) != null && field.getFieldStatus ( ) == Field.FieldStatus.NEW) {
      insertNewVariableField (item, field.getVariableField ( ), bibItemNumber, correlationValues, session, view);
    }
  }

  /**
   * Insert of a new variable field.
   *
   * @param item          -- the item to add tags.
   * @param variableField -- the variable field containing data.
   * @param bibItemNumber -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  public void insertNewVariableField(final CatalogItem item,
                                     final org.folio.cataloging.resources.domain.VariableField variableField,
                                     final int bibItemNumber,
                                     final CorrelationValues correlationValues,
                                     final Session session,
                                     final int view) throws DataAccessException {

    if (variableField.getCategoryCode ( ) == GlobalStorage.TITLE_CATEGORY) {
      addTitleToCatalog (item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode ( ) == GlobalStorage.NAME_CATEGORY) {
      addNameToCatalog (item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode ( ) == GlobalStorage.CONTROL_NUMBER_CATEGORY) {
      addControlFieldToCatalog (item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode ( ) == GlobalStorage.CLASSIFICATION_CATEGORY) {
      addClassificationToCatalog (item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode ( ) == GlobalStorage.SUBJECT_CATEGORY) {
      addSubjectToCatalog (item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode ( ) == GlobalStorage.BIB_NOTE_CATEGORY && correlationValues.getValue (1) != GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE) {
      addNoteToCatalog (item, correlationValues, variableField, bibItemNumber);
    } else if (variableField.getCategoryCode ( ) == GlobalStorage.BIB_NOTE_CATEGORY && correlationValues.getValue (1) == GlobalStorage.PUBLISHER_DEFAULT_NOTE_TYPE) {

      try {
        addPublisherToCatalog (item, correlationValues, variableField, view, session);
      } catch (HibernateException | SQLException e) {
        throw new DataAccessException (e);
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
   * @throws DataAccessException in case of data access exception.
   */
  private void addPublisherToCatalog(final CatalogItem item,
                                     final CorrelationValues correlationValues,
                                     final org.folio.cataloging.resources.domain.VariableField variableField,
                                     final int view,
                                     final Session session) throws DataAccessException, HibernateException, SQLException {

    final PublisherManager publisherManager = catalog.createPublisherTag (item, correlationValues);
    publisherManager.setUserViewString (View.makeSingleViewString (view));

    PUBL_TAG ptag = new PUBL_TAG ( );
    Descriptor descriptorNew = ptag.getDescriptorDAO ( ).findOrCreateMyView (variableField.getKeyNumber ( ), View.makeSingleViewString (item.getUserView ( )), view, session);
    ptag.setDescriptor ((PUBL_HDG) descriptorNew);
    ptag.setPublisherHeadingNumber (new Integer (ptag.getDescriptor ( ).getKey ( ).getHeadingNumber ( )));
    ptag.setUserViewString (View.makeSingleViewString (view));
    ptag.markNew ( );
    StringText st = new StringText (variableField.getValue ( ));

    ptag.setOtherSubfields (st.getSubfieldsWithCodes ("c").toString ( ));
    publisherManager.getPublisherTagUnits ( ).add (ptag);

    item.addTag (publisherManager);
  }

  /**
   * Creates and add to catalog a new persistent {@link BibliographicNoteTag} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  private void addNoteToCatalog(final CatalogItem item,
                                final CorrelationValues correlationValues,
                                final org.folio.cataloging.resources.domain.VariableField variableField,
                                final int bibItemNumber) throws DataAccessException {
    final BibliographicNoteTag nTag = catalog.createBibliographicNoteTag (item, correlationValues);
    nTag.getNote ( ).setContent (variableField.getValue ( ));
    if (variableField.getKeyNumber ( ) != null && variableField.getKeyNumber ( ) != 0)
      nTag.getNote ( ).setNoteNbr (variableField.getKeyNumber ( ));

    nTag.setItemNumber (bibItemNumber);
    nTag.markNew ( );
    item.addTag (nTag);
  }

  /**
   * Creates and add to catalog a new persistent {@link SubjectAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  private void addSubjectToCatalog(final CatalogItem item,
                                   final CorrelationValues correlationValues,
                                   final org.folio.cataloging.resources.domain.VariableField variableField,
                                   final int bibItemNumber) throws DataAccessException {
    final SubjectAccessPoint sap = catalog.createSubjectAccessPoint (item, correlationValues);
    sap.setAccessPointStringText (new StringText (variableField.getValue ( )));
    sap.setHeadingNumber (variableField.getKeyNumber ( ));
    sap.setItemNumber (bibItemNumber);
    sap.markNew ( );
    item.addTag (sap);
  }

  /**
   * Creates and add to catalog a new persistent {@link ClassificationAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  private void addClassificationToCatalog(final CatalogItem item,
                                          final CorrelationValues correlationValues,
                                          final org.folio.cataloging.resources.domain.VariableField variableField,
                                          final int bibItemNumber) throws DataAccessException {
    final ClassificationAccessPoint clap = catalog.createClassificationAccessPoint (item, correlationValues);
    clap.setAccessPointStringText (new StringText (variableField.getValue ( )));
    clap.setHeadingNumber (variableField.getKeyNumber ( ));
    clap.setItemNumber (bibItemNumber);
    clap.markNew ( );
    item.addTag (clap);
  }

  /**
   * Creates and add to catalog a new persistent {@link ControlNumberAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  private void addControlFieldToCatalog(final CatalogItem item,
                                        final CorrelationValues correlationValues,
                                        final org.folio.cataloging.resources.domain.VariableField variableField,
                                        final int bibItemNumber) throws DataAccessException {
    final ControlNumberAccessPoint cnap = catalog.createControlNumberAccessPoint (item, correlationValues);
    cnap.setAccessPointStringText (new StringText (variableField.getValue ( )));
    cnap.setHeadingNumber (variableField.getKeyNumber ( ));
    cnap.setItemNumber (bibItemNumber);
    cnap.markNew ( );
    item.addTag (cnap);
  }

  /**
   * Creates and add to catalog a new persistent {@link NameAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  private void addNameToCatalog(final CatalogItem item,
                                final CorrelationValues correlationValues,
                                final org.folio.cataloging.resources.domain.VariableField variableField,
                                final int bibItemNumber) throws DataAccessException {
    final NameAccessPoint nap = catalog.createNameAccessPointTag (item, correlationValues);
    nap.setAccessPointStringText (new StringText (variableField.getValue ( )));
    nap.setHeadingNumber (variableField.getKeyNumber ( ));
    nap.setItemNumber (bibItemNumber);
    nap.markNew ( );
    item.addTag (nap);
  }

  /**
   * Creates and add to catalog a new persistent {@link TitleAccessPoint} object for saving record.
   *
   * @param item              -- the item to add tags.
   * @param correlationValues -- the selection of correlation values.
   * @param variableField     -- the variable field containing data.
   * @param bibItemNumber     -- the bibliographic item number.
   * @throws DataAccessException in case of data access exception.
   */
  private void addTitleToCatalog(final CatalogItem item,
                                 final CorrelationValues correlationValues,
                                 final org.folio.cataloging.resources.domain.VariableField variableField,
                                 final int bibItemNumber) throws DataAccessException {

    final TitleAccessPoint tap = catalog.createTitleAccessPointTag (item, correlationValues);
    tap.setAccessPointStringText (new StringText (variableField.getValue ( )));
    tap.setHeadingNumber (variableField.getKeyNumber ( ));
    tap.setItemNumber (bibItemNumber);
    tap.markNew ( );
    item.addTag (tap);
  }

}
