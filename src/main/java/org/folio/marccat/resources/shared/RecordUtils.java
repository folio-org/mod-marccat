package org.folio.marccat.resources.shared;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.config.constants.Global.EMPTY_STRING;
import static org.folio.marccat.resources.shared.FixedFieldUtils.isFixedField;

import java.util.HashMap;
import java.util.Map;

import org.folio.marccat.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.RecordTypeMaterialDAO;
import org.folio.marccat.dao.persistence.AuthorityNote;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.dao.persistence.MaterialDescription;
import org.folio.marccat.dao.persistence.PhysicalDescription;
import org.folio.marccat.dao.persistence.PublisherManager;
import org.folio.marccat.dao.persistence.RecordTypeMaterial;
import org.folio.marccat.dao.persistence.TitleAccessPoint;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.domain.Record;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.resources.domain.VariableField;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.shared.Validation;
import org.folio.marccat.util.F;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

public class RecordUtils {
  private static final Log logger = new Log(RecordUtils.class);

  private RecordUtils() {
  }

  /**
   * Sets the default leader value.
   *
   * @return the default leader value.
   */
  public static String getLeaderValue(boolean isBib) {
    if (isBib)
      return Global.FIXED_LEADER_LENGTH +
          Global.RECORD_STATUS_CODE +
          Global.RECORD_TYPE_CODE +
          Global.BIBLIOGRAPHIC_LEVEL_CODE +
          Global.CONTROL_TYPE_CODE +
          Global.CHARACTER_CODING_SCHEME_CODE +
          Global.FIXED_LEADER_BASE_ADDRESS +
          Global.ENCODING_LEVEL +
          Global.DESCRIPTIVE_CATALOGUING_CODE +
          Global.LINKED_RECORD_CODE +
          Global.FIXED_LEADER_PORTION;
    else
      return Global.FIXED_LEADER_LENGTH +
          Global.RECORD_STATUS_CODE +
          Global.AUT_RECORD_TYPE_CODE +
          Global.EMPTY_VALUE +
          Global.EMPTY_VALUE +
          Global.CHARACTER_CODING_SCHEME_CODE +
          Global.FIXED_LEADER_BASE_ADDRESS +
          Global.AUT_ENCODING_LEVEL +
          Global.PUNCTUATION_POLICY +
          Global.EMPTY_VALUE +
          Global.FIXED_LEADER_PORTION;
  }

  /**
   * Reset status fields to UNCHANGED.
   *
   * @param newRecord -- the new record created.
   */
  public static void resetStatus(Record newRecord) {
    newRecord.getFields().forEach(field -> {
      if (Global.MANDATORY_FIELDS.contains(field.getCode()))
        field.setMandatory(true);
      field.setFieldStatus(Field.FieldStatus.UNCHANGED);
    });
  }

  /**
   * Utility to get category code.
   *
   * @param field -- the field containing category.
   * @return category.
   */
  public static int getCategory(final Field field) {
    if (isFixedField(field))
      return Global.HEADER_CATEGORY;
    if (!isFixedField(field) && ofNullable(field.getVariableField().getCategoryCode()).isPresent())
      return field.getVariableField().getCategoryCode();

    return 0;
  }

  /**
   * Create a new field for transaction data.
   *
   * @param lang -- the lang associated to request.
   * @param storageService -- the storageService.
   * @return new transaction data field.
   */
  public static Field addTagTransactionDate(final String lang, final StorageService storageService) {
    FixedField fixed005 = new FixedField();
    fixed005.setHeaderTypeCode(Global.DATETIME_TRANSACTION_HEADER_TYPE);
    fixed005.setCode(Global.DATETIME_TRANSACTION_TAG_CODE);
    fixed005.setDisplayValue(F.getFormattedToday("yyyyMMddHHmmss."));
    fixed005.setCategoryCode(Global.INT_CATEGORY);
    fixed005.setDescription(storageService.getHeadingTypeDescription(Global.DATETIME_TRANSACTION_HEADER_TYPE, lang, Global.INT_CATEGORY));

    final Field field = new Field();
    field.setCode(Global.DATETIME_TRANSACTION_TAG_CODE);
    field.setMandatory(true);
    field.setFixedField(fixed005);
    field.setFieldStatus(Field.FieldStatus.NEW);
    return field;
  }

  /**
   * Check if field exists in template and is mandatory.
   *
   * @param field -- current field in record.
   * @param template -- the associated template.
   * @return true if mandatory, false otherwise.
   */
  public static boolean isMandatory(final Field field, final RecordTemplate template) {
    if (ofNullable(template).isPresent()) {
      return template.getFields().stream().filter(f -> f.getCode().equals(field.getCode())).anyMatch(Field::isMandatory);
    }

    return Global.MANDATORY_FIELDS.contains(field.getCode());
  }

  /**
   * Sets category code on field.
   *
   * @param field -- the field to set category.
   * @param storageService -- the storageService module.
   */
  public static void setCategory(final Field field, final StorageService storageService) {
    if (isFixedField(field))
      field.getFixedField().setCategoryCode(Global.HEADER_CATEGORY);
    else if (getCategory(field) == 0) {
      final int category = getTagCategory(field, storageService);
      field.getVariableField().setCategoryCode(category);
    }

  }

  /**
   * Return category code on field.
   *
   * @param field -- the field to set category.
   * @param storageService -- the storageService module.
   * @return a category code.
   */
  public static int getTagCategory(final Field field, final StorageService storageService) {
    boolean hasTitle = isNameTitle(field.getCode(), field.getVariableField().getValue());
    return storageService.getTagCategory(field.getCode(), field.getVariableField().getInd1().charAt(0), field.getVariableField().getInd2().charAt(0), hasTitle);
  }

  /**
   * Return category code of a heading by tag number.
   *
   * @param heading -- the heading
   * @param storageService -- the storageService module.
   * @return a category code.
   */
  public static int getTagCategory(final Heading heading, final StorageService storageService) {
    boolean hasTitle = isNameTitle(heading.getTag(), heading.getDisplayValue());
    return storageService.getTagCategory(heading.getTag(), heading.getInd1().charAt(0), heading.getInd2().charAt(0), hasTitle);
  }

  /**
   * Check if present a tag of type title name.
   *
   * @param tag -- the tag number
   * @param displayValue -- the display value of a tag
   * @return true if name title, false otherwise.
   */
  public static boolean isNameTitle(String tag, String displayValue) {
    return ((tag.endsWith("00") || tag.endsWith("10") || tag.endsWith("11")) && displayValue.contains(Global.SUBFIELD_DELIMITER + "t"));
  }

  /**
   * Creates a cataloging source field (tag 040) using default values.
   *
   * @param configuration the configuration parameters
   * @param storageService the storage service.
   * @param lang the lang code.
   * @return a new 040 {@link Field} entity populated with default values.
   */
  public static Field createCatalogingSourceField(final Map<String, String> configuration, final StorageService storageService, final String lang) {
    final CorrelationValues correlationValues = new CorrelationValues(Global.CATALOGING_SOURCE_HEADER_TYPE, Global.CORRELATION_UNDEFINED,
        Global.CORRELATION_UNDEFINED);

    final String description = storageService.getHeadingTypeDescription(Global.CATALOGING_SOURCE_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final Validation validation = storageService
      .getSubfieldsByCorrelations(Global.INT_CATEGORY, correlationValues.getValue(1), correlationValues.getValue(2), correlationValues.getValue(3));

    final VariableField catalogingSourceField = new VariableField();

    catalogingSourceField.setCode(Global.CATALOGING_SOURCE_TAG_CODE);
    catalogingSourceField.setHeadingTypeCode(Integer.toString(Global.CATALOGING_SOURCE_HEADER_TYPE));
    catalogingSourceField.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split("")).collect(toList()));
    catalogingSourceField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));
    catalogingSourceField.setValue(configuration.get("bibliographicItem.cataloguingSourceStringText"));
    catalogingSourceField.setDescription(description);
    catalogingSourceField.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.CATALOGING_SOURCE_TAG_CODE);
    field.setMandatory(true);
    field.setVariableField(catalogingSourceField);

    return field;
  }

  /**
   * Creates default control field value.
   *
   * @param storageService the storage service.
   * @param lang the lang associated with the current request.
   * @return a new 001 {@link Field} entity populated with default values.
   */
  public static Field createControlNumberField(final StorageService storageService, final String lang) {
    final String description = storageService.getHeadingTypeDescription(Global.CONTROL_NUMBER_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final FixedField controlNumberFixedField = new FixedField();
    controlNumberFixedField.setCode(Global.CONTROL_NUMBER_TAG_CODE);
    controlNumberFixedField.setDisplayValue(Global.DECIMAL_FORMAT_AN.format(0));
    controlNumberFixedField.setHeaderTypeCode(Global.CONTROL_NUMBER_HEADER_TYPE);
    controlNumberFixedField.setDescription(description);
    controlNumberFixedField.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.CONTROL_NUMBER_TAG_CODE);
    field.setMandatory(true);
    field.setFixedField(controlNumberFixedField);

    return field;

  }

  /**
   * Creates a leader with default values.
   *
   * @param storageService the storage service.
   * @param lang the lang associated with the current request.
   * @return a new leader {@link Field} entity populated with default values.
   */
  public static Field createRequiredLeaderField(final StorageService storageService, final String lang) {
    final String description = storageService.getHeadingTypeDescription(Global.LEADER_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final FixedField leader = new FixedField();
    leader.setHeaderTypeCode(Global.LEADER_HEADER_TYPE);
    leader.setCode(Global.LEADER_TAG_NUMBER);
    leader.setDisplayValue(getLeaderValue(true));
    leader.setDescription(description);
    leader.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.LEADER_TAG_NUMBER);
    field.setMandatory(true);
    field.setFixedField(leader);
    return field;
  }

  /**
   * Creates default 008 field.
   *
   * @param configuration the configuration parameters.
   * @param storageService the storage service.
   * @param lang the lang associated with the current request.
   * @return the 008 default tag definition.
   */
  public static Field createRequiredMaterialDescriptionField(final Map<String, String> configuration, final StorageService storageService, final String lang) {

    final String description = storageService.getHeadingTypeDescription(Global.MATERIAL_DESCRIPTION_HEADER_TYPE, lang, Global.INT_CATEGORY);
    final GeneralInformation generalInformation = new GeneralInformation();
    generalInformation.setMaterialDescription008Indicator("1");
    generalInformation.setFormOfMaterial(Global.BOOKFORM_OF_MATERIAL);
    generalInformation.setDefaultValues(configuration);
    generalInformation.setEnteredOnFileDateYYMMDD(F.getFormattedToday("yyMMdd"));

    final FixedField materialDescription = new FixedField();
    materialDescription.setHeaderTypeCode(Global.MATERIAL_DESCRIPTION_HEADER_TYPE);
    materialDescription.setCode(Global.MATERIAL_TAG_CODE);
    materialDescription.setDisplayValue(generalInformation.getValueString());
    materialDescription.setDescription(description);
    materialDescription.setCategoryCode(Global.INT_CATEGORY);

    final Field field = new Field();
    field.setCode(Global.MATERIAL_TAG_CODE);
    field.setMandatory(true);
    field.setFixedField(materialDescription);

    return field;
  }

  /**
   * Sets variable field with selected drop-down list (correlation entity) and
   * sub-fields (validation entity).
   *
   * @param categoryCode the field category code.
   * @param ind1 the first indicator of tag field.
   * @param ind2 the second indicator of tag field.
   * @param code the tag number code.
   * @param correlations the selected drop-down list.
   * @param description the field description
   * @param validation the sub-fields valid for this tag/field
   * @return a VariableField entity.
   */
  public static VariableField getVariableField(final int categoryCode,
    final String ind1,
    final String ind2,
    final String code,
    final CorrelationValues correlations,
    final String description,
    final Validation validation) {

    final VariableField variableField = new VariableField();
    if (!isFixedField(code)) {
      variableField.setHeadingTypeCode(Integer.toString(correlations.getValue(1)));
      variableField.setItemTypeCode(Integer.toString(correlations.getValue(2)));
      variableField.setFunctionCode(Integer.toString(correlations.getValue(3)));
      variableField.setCategoryCode(categoryCode);
      variableField.setCode(code);
      variableField.setInd1(ind1);
      variableField.setInd2(ind2);
      variableField.setDescription(description);

      variableField.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split("")).collect(toList()));
      variableField.setDefaultSubfieldCode(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));

    }
    return variableField;
  }

  /**
   * Gets the Material description information.
   *
   * @param recordTypeCode the record type code (Tag 006//00) used here as filter
   *        criterion.
   * @return a map with RecordTypeMaterial info.
   * @throws DataAccessException in case of data access failure.
   */
  public static Map<String, Object> getHeaderTypeByRecordTypeCode(final char recordTypeCode, Session session) {

    final RecordTypeMaterialDAO dao = new RecordTypeMaterialDAO();

    try {
      final Map<String, Object> mapRecordTypeMaterial = new HashMap<>();
      final RecordTypeMaterial rtm = dao.get006HeaderCode(session, recordTypeCode);

      mapRecordTypeMaterial.put(Global.HEADER_TYPE_LABEL, rtm.getBibHeader006());
      mapRecordTypeMaterial.put(Global.FORM_OF_MATERIAL_LABEL, rtm.getAmicusMaterialTypeCode());
      return mapRecordTypeMaterial;
    } catch (final HibernateException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
  /**
   * Gets the Material description information. The values depend on mtrl_dsc and
   * bib_itm data (leader).
   *
   * @param recordTypeCode the record type code (leader 05) used here as filter
   *        criterion.
   * @param bibliographicLevel the bibliographic level (leader 06) used here as
   *        filter criterion.
   * @param code the tag number code used here as filter criterion.
   * @return a map with RecordTypeMaterial info.
   * @throws DataAccessException in case of data access failure.
   */
  public static Map<String, Object> getMaterialTypeInfosByLeaderValues(final char recordTypeCode, final char bibliographicLevel, final String code, Session session) {

    final RecordTypeMaterialDAO dao = new RecordTypeMaterialDAO();

    try {
      final Map<String, Object> mapRecordTypeMaterial = new HashMap<>();
      final RecordTypeMaterial rtm = dao.getMaterialHeaderCode(session, recordTypeCode, bibliographicLevel);

      mapRecordTypeMaterial.put(Global.HEADER_TYPE_LABEL, (code.equals(Global.MATERIAL_TAG_CODE) ? rtm.getBibHeader008() : rtm.getBibHeader006()));
      mapRecordTypeMaterial.put(Global.FORM_OF_MATERIAL_LABEL, rtm.getAmicusMaterialTypeCode());
      return mapRecordTypeMaterial;
    } catch (final HibernateException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }

  public static Field getRecordField(final Tag aTag, Session session) {
    final org.folio.marccat.resources.domain.Field field = new org.folio.marccat.resources.domain.Field();
    org.folio.marccat.resources.domain.VariableField variableField;
    org.folio.marccat.resources.domain.FixedField fixedField;
    int keyNumber = 0;
    int sequenceNbr = 0;
    int skipInFiling = 0;
    final CorrelationKey correlation = aTag.getTagImpl().getMarcEncoding(aTag, session);
    String entry = aTag.isFixedField() ? (((org.folio.marccat.business.cataloguing.bibliographic.FixedField) aTag).getDisplayString())
        : ((org.folio.marccat.business.cataloguing.bibliographic.VariableField) aTag).getStringText().getMarcDisplayString(Subfield.SUBFIELD_DELIMITER);

    String tagNumber = correlation.getMarcTag();

    if (aTag.isFixedField() && aTag instanceof MaterialDescription) {
      final MaterialDescription materialTag = (MaterialDescription) aTag;
      keyNumber = materialTag.getMaterialDescriptionKeyNumber();
      final String tagNbr = materialTag.getMaterialDescription008Indicator().equals("1") ? "008" : "006";
      final Map<String, Object> map;
      if (tagNbr.equals("008"))
        map = getMaterialTypeInfosByLeaderValues(materialTag.getItemRecordTypeCode(), materialTag.getItemBibliographicLevelCode(), tagNbr, session);
      else
        map = getHeaderTypeByRecordTypeCode(materialTag.getMaterialTypeCode().charAt(0), session);
      materialTag.setHeaderType((int) map.get(Global.HEADER_TYPE_LABEL));
      materialTag.setMaterialTypeCode(tagNbr.equalsIgnoreCase("006") ? materialTag.getMaterialTypeCode() : null);
      materialTag.setFormOfMaterial((String) map.get(Global.FORM_OF_MATERIAL_LABEL));
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
    if (aTag.isFixedField() && aTag instanceof PhysicalDescription) {
      final PhysicalDescription physicalTag = (PhysicalDescription) aTag;
      keyNumber = physicalTag.getKeyNumber();
    }

    if (!aTag.isFixedField() && aTag instanceof AuthorityNote) {
      keyNumber = ((AuthorityNote) aTag).getNoteNbr();
      try {
        sequenceNbr = ((AuthorityNote) aTag).getSequenceNumber();
      } catch (Exception e) {
        sequenceNbr = 0;
      }
    }
    if (!aTag.isFixedField() && aTag instanceof PublisherManager && !((PublisherManager) aTag).getPublisherTagUnits().isEmpty()) {
      keyNumber = ((PublisherManager) aTag).getPublisherTagUnits().get(0).getPublisherHeadingNumber(); // add gestione
                                                                                                       // multi
                                                                                                       // publisher
    }

    if (aTag instanceof PublisherManager) {
      try {
        entry = aTag.addPunctuation().getMarcDisplayString(Subfield.SUBFIELD_DELIMITER);
      } catch (Exception exception) {
        logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
      }
    }

    if (aTag.isFixedField()) {
      fixedField = new org.folio.marccat.resources.domain.FixedField();
      fixedField.setSequenceNumber(sequenceNbr);
      fixedField.setCode(tagNumber);
      fixedField.setDisplayValue(entry);
      fixedField.setHeaderTypeCode(aTag.getCorrelation(1));
      fixedField.setCategoryCode(aTag.getCategory());
      fixedField.setKeyNumber(keyNumber);
      field.setFixedField(fixedField);
    } else {
      variableField = new org.folio.marccat.resources.domain.VariableField();
      variableField.setSequenceNumber(sequenceNbr);
      variableField.setCode(correlation.getMarcTag());
      variableField.setInd1(EMPTY_STRING + correlation.getMarcFirstIndicator());
      variableField.setInd2(EMPTY_STRING + correlation.getMarcSecondIndicator());
      variableField.setHeadingTypeCode(Integer.toString(aTag.getCorrelation(1)));
      variableField.setItemTypeCode(Integer.toString(aTag.getCorrelation(2)));
      variableField.setFunctionCode(Integer.toString(aTag.getCorrelation(3)));
      variableField.setValue(entry);
      variableField.setCategoryCode(correlation.getMarcTagCategoryCode());
      variableField.setKeyNumber(keyNumber);
      variableField.setSkipInFiling(skipInFiling);
      if (variableField.getInd2().equals("S"))
        variableField.setInd2(EMPTY_STRING + skipInFiling);
      field.setVariableField(variableField);
    }

    field.setCode(tagNumber);
    return field;

  }

}
