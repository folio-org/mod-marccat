package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.domain.ConversionFieldUtils;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.DuplicateTagException;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.*;
import org.folio.marccat.resources.domain.Error;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.shared.Validation;
import org.folio.marccat.util.F;
import org.folio.marccat.util.StringText;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.integration.CatalogingHelper.*;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

/**
 * Bibliographic records API.
 *
 * @author nbianchini
 * @since 1.0
 */

@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class BibliographicRecordAPI extends BaseResource {


  @GetMapping("/bibliographic-record/{id}")
  public ResponseEntity<Object> getRecord(
    @PathVariable final Integer id,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final ContainerRecordTemplate container = storageService.getBibliographicRecordById(id, view);
      final BibliographicRecord record = container.getBibliographicRecord();
      if (record != null)
        resetStatus(record);
      else {
        return new ResponseEntity<>(container, HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(container, HttpStatus.OK);
    }, tenant, configurator);
  }

  @GetMapping("/bibliographic-record/duplicate")
  public ResponseEntity<Object> duplicate(
    @RequestParam final Integer id,
    @RequestParam final String lang,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

    return doGet((storageService, configuration) -> {

      BibliographicRecord newRecord = new BibliographicRecord();
      newRecord.setId(storageService.generateNewKey(Global.AN_KEY_CODE_FIELD));

      final ContainerRecordTemplate container = storageService.getBibliographicRecordById(id, view);
      if (container == null)
        return new ResponseEntity<>(container, HttpStatus.NOT_FOUND);

      final BibliographicRecord fromRecord = container.getBibliographicRecord();
      final RecordTemplate template = container.getRecordTemplate();

      newRecord.setLeader(fromRecord.getLeader());

      for (Field field : fromRecord.getFields()) {
          if (field.getCode().equals(Global.CONTROL_NUMBER_TAG_CODE)) {
            FixedField controlNumber = field.getFixedField();
            controlNumber.setDisplayValue(F.padNumber("0", 11, newRecord.getId()));
            field.setFixedField(controlNumber);
          }

          if (field.getCode().equals(Global.DATETIME_TRANSACTION_TAG_CODE)) {
            final Field fieldDate = addTagTransactionDate(lang, storageService);
            newRecord.getFields().add(1, fieldDate);
          }

          if ( (Integer.parseInt(field.getCode()) < Global.TAG_RELATION_MIN || Integer.parseInt(field.getCode()) > Global.TAG_RELATION_MAX)
             && !field.getCode().equals(Global.DATETIME_TRANSACTION_TAG_CODE)
             && !field.getCode().startsWith("9")) {

            field.setFieldStatus(Field.FieldStatus.NEW);
            field.setMandatory(isMandatory(field, template));
            newRecord.getFields().add(field);
          }
       }

      newRecord.setRecordView(fromRecord.getRecordView());
      newRecord.setGroup(fromRecord.getGroup());
      newRecord.setVerificationLevel(fromRecord.getVerificationLevel());
      newRecord.setCanadianContentIndicator(fromRecord.getCanadianContentIndicator());

      container.setBibliographicRecord(newRecord);
      return new ResponseEntity<>(container, HttpStatus.OK);
    }, tenant, configurator);
  }

  /**
   * Create a new field for transaction data.
   *
   * @param lang -- the lang associated to request.
   * @param storageService -- the storageService.
   * @return new transaction data field.
   */
  private Field addTagTransactionDate(final String lang, final StorageService storageService) {
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
  private boolean isMandatory(final Field field, final RecordTemplate template){
    if (ofNullable(template).isPresent()){
      return template.getFields().stream().filter(f -> f.getCode().equals(field.getCode())).anyMatch(f -> f.isMandatory());
    }

    return Global.MANDATORY_FIELDS.contains(field.getCode());
  }

  @GetMapping("/bibliographic-record/from-template/{idTemplate}")
  public BibliographicRecord getEmptyRecord(
    @PathVariable final Integer idTemplate,
    @RequestParam final String lang,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

    return doGet((storageService, configuration) -> {

      BibliographicRecord bibliographicRecord = new BibliographicRecord();
      final RecordTemplate template = storageService.getBibliographicRecordRecordTemplatesById(idTemplate);
      bibliographicRecord.setId(storageService.generateNewKey(Global.AN_KEY_CODE_FIELD));
      bibliographicRecord.setLeader(ofNullable(template.getLeader()).map(leader -> template.getLeader())
        .orElseGet(() -> {
          Leader leader = new Leader();
          leader.setCode(Global.LEADER_TAG_NUMBER);
          leader.setValue(getLeaderValue());
          return leader;
        }));

      for (Field field : template.getFields()) {
        if (field.isMandatory()) {
          if (field.getCode().equals(Global.CONTROL_NUMBER_TAG_CODE)) {
            FixedField controlNumber = field.getFixedField();
            controlNumber.setDisplayValue(F.padNumber("0", 11, bibliographicRecord.getId()));
            field.setFixedField(controlNumber);
          }
          field.setFieldStatus(Field.FieldStatus.NEW);
          bibliographicRecord.getFields().add(field);
        }
      }

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
      bibliographicRecord.getFields().add(1, field);

      bibliographicRecord.setVerificationLevel(configuration.get("bibliographicItem.recordCataloguingSourceCode"));
      bibliographicRecord.setCanadianContentIndicator("0");

      resetStatus(bibliographicRecord);
      return bibliographicRecord;

    }, tenant, configurator, "bibliographic");
  }


  @PostMapping("/bibliographic-record")
  public ResponseEntity<Object> save(
    @RequestBody final ContainerRecordTemplate container,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

    return doPost((storageService, configuration) -> {
      try {

        final BibliographicRecord record = container.getBibliographicRecord();
        RecordTemplate template = ofNullable(container.getRecordTemplate()).isPresent() ?container.getRecordTemplate() :null;

        record.getFields().forEach(field -> setCategory(field, storageService));

        final Integer itemNumber = record.getId();
        final ErrorCollection errors = validate(record, storageService);
        if (!errors.getErrors().isEmpty())
          return systemInternalFailure(new DataAccessException(), errors);

        final GeneralInformation gi = new GeneralInformation();
        gi.setDefaultValues(configuration);

        final Leader leader = record.getLeader();
        record.getFields().stream().filter(this::isFixedField)
          .filter(field -> field.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE) ||
            field.getCode().equalsIgnoreCase(Global.OTHER_MATERIAL_TAG_CODE) ||
            field.getCode().equalsIgnoreCase(Global.PHYSICAL_DESCRIPTION_TAG_CODE)).forEach(field -> {

          FixedField ff = field.getFixedField();
          final int headerTypeCode = ff.getHeaderTypeCode();

          if (field.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE) || field.getCode().equalsIgnoreCase(Global.OTHER_MATERIAL_TAG_CODE)) {

            final Map<String, Object> mapRecordTypeMaterial = (field.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE))
              ? storageService.getMaterialTypeInfosByLeaderValues(leader.getValue().charAt(6), leader.getValue().charAt(7), field.getCode())
              : storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, field.getCode());

            ConversionFieldUtils.setMaterialValuesInFixedField(ff, (String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
          } else {
            ConversionFieldUtils.setPhysicalInformationValuesInFixedField(ff);
          }
        });

        storageService.saveBibliographicRecord(record, template, view, gi, lang);
        final ContainerRecordTemplate containerSaved = storageService.getBibliographicRecordById(itemNumber, view);
        resetStatus(containerSaved.getBibliographicRecord());
        return new ResponseEntity<>(containerSaved, HttpStatus.OK);

      } catch (final Exception exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return new ResponseEntity<>(container, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }, tenant, configurator, () -> isNotNullOrEmpty(container.getBibliographicRecord().getId().toString()), "bibliographic", "material");
  }


  /**
   * Reset status fields to UNCHANGED.
   *
   * @param newRecord -- the new record created.
   */
  private void resetStatus(BibliographicRecord newRecord) {
    newRecord.getFields().forEach(field -> {
      if (Global.MANDATORY_FIELDS.contains(field.getCode()))
        field.setMandatory(true);
      field.setFieldStatus(Field.FieldStatus.UNCHANGED);
    });
  }

  /**
   * Validates tags record.
   *
   * @param record         -- the record to validate.
   * @param storageService -- the storage service.
   * @return error collection.
   */
  private ErrorCollection validate(BibliographicRecord record, StorageService storageService) {
    final ErrorCollection errors = new ErrorCollection();

    if (!checkMandatory(record)) {
      logger.error(MessageCatalog._00026_MANDATORY_FAILURE, record.getId());
      errors.getErrors().add(getError(Global.ERROR_MANDATORY_TAG));
    }

    String wrongTags = checkRepeatability(record, storageService);
    if (F.isNotNullOrEmpty(wrongTags)) {
      logger.error(MessageCatalog._00025_DUPLICATE_TAG, wrongTags);
      errors.getErrors().add(getError(Global.ERROR_DUPLICATE_TAG, wrongTags));
    }

    String emptyTags = checkEmptyTag(record);
    if (F.isNotNullOrEmpty(emptyTags)) {
      logger.error(MessageCatalog._00027_EMPTY_TAG, emptyTags);
      errors.getErrors().add(getError(Global.ERROR_EMPTY_TAG, emptyTags));
    }

    return errors;
  }

  /**
   * Create a new Error object.
   *
   * @param code   -- code of error.
   * @param values -- placeholders for description object.
   * @return a new error.
   */
  private Error getError(final String code, final Object... values) {
    Error e = new Error();
    e.setCode(code);
    e.setDescription(values == null ? Global.ERRORS_MAP.get(code) : String.format(Global.ERRORS_MAP.get(code), values));
    return e;
  }

  /**
   * Checks if there is one or more empty tag.
   *
   * @param record -- the record with tags to check.
   * @throws DataAccessException in case of data access exception.
   */
  private String checkEmptyTag(final BibliographicRecord record) {
    StringBuilder tags = new StringBuilder();
    record.getFields().forEach(field -> {
      if (!isFixedField(field)) {
        final StringText st = new StringText(field.getVariableField().getValue());
        if (st.isEmpty()) {
          tags.append(field.getCode()).append(",");
        }
      }
    });

    if (tags.length() > 0)
      tags.deleteCharAt(tags.length() - 1);
    return tags.toString();
  }

  /**
   * Checks tags repeatability.
   *
   * @param record  -- the record with tags to check.
   * @param storage -- the storage service.
   * @throws DuplicateTagException in case of duplicate tag exception.
   */
  private String checkRepeatability(final BibliographicRecord record, final StorageService storage) {
    Map<String, List<Field>> fieldsGroupedByCode = record.getFields().stream().collect(Collectors.groupingBy(Field::getCode));
    List<String> duplicates =
      fieldsGroupedByCode.entrySet().stream()
        .filter(entry -> entry.getValue().size() > 1)
        .map(Map.Entry::getKey).collect(Collectors.toList());

    StringBuilder tags = new StringBuilder();
    duplicates.forEach(tagNbr -> {
      Validation bv = storage.getTagValidation(getCategory(fieldsGroupedByCode.get(tagNbr).get(0)), tagNbr);
      if (!bv.isMarcTagRepeatable()) {
        tags.append(tagNbr).append(",");
      }
    });

    if (tags.length() > 0)
      tags.deleteCharAt(tags.length() - 1);

    return tags.toString();
  }

  /**
   * Checks mandatory tags.
   *
   * @param record -- the record with tags to check.
   * @return check mandatory.
   */
  private boolean checkMandatory(final BibliographicRecord record) {
    List<String> found = new ArrayList<>();
    if (record.getLeader() != null)
      found.add(record.getLeader().getCode());

    record.getFields().forEach(field -> {
      found.add(field.getCode());
    });
    ArrayList<String> result = new ArrayList<>(Global.MANDATORY_FIELDS);
    result.retainAll(found);
    return result.size() == Global.MANDATORY_FIELDS.size() && !found.isEmpty();
  }

  /**
   * Check if is a fixed field or not.
   *
   * @param field the tag entity.
   * @return true if is fixedfield, false otherwise.
   */
  private boolean isFixedField(final Field field) {
    return Global.FIXED_FIELDS.contains(field.getCode());
  }

  /**
   * Utility to get category code.
   *
   * @param field -- the field containing category.
   * @return category.
   */
  private int getCategory(final Field field) {

    if (isFixedField(field))
      return Global.HEADER_CATEGORY;

    if (!isFixedField(field) && ofNullable(field.getVariableField().getCategoryCode()).isPresent())
      return field.getVariableField().getCategoryCode();

    return 0;
  }

  /**
   * Sets the default leader value.
   *
   * @return the default leader value.
   */
  private String getLeaderValue() {
    return new StringBuilder(Global.FIXED_LEADER_LENGTH)
      .append(Global.RECORD_STATUS_CODE)
      .append(Global.RECORD_TYPE_CODE)
      .append(Global.BIBLIOGRAPHIC_LEVEL_CODE)
      .append(Global.CONTROL_TYPE_CODE)
      .append(Global.CHARACTER_CODING_SCHEME_CODE)
      .append(Global.FIXED_LEADER_BASE_ADDRESS)
      .append(Global.ENCODING_LEVEL)
      .append(Global.DESCRIPTIVE_CATALOGUING_CODE)
      .append(Global.LINKED_RECORD_CODE)
      .append(Global.FIXED_LEADER_PORTION)
      .toString();
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/bibliographic-record/{id}")
  public void delete(@PathVariable final String id,
                     @RequestParam final String uuid,
                     @RequestParam final String userName,
                     @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
                     @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    doDelete((storageService, configuration) -> {
      storageService.deleteBibliographicRecordById(Integer.parseInt(id), view, uuid, userName);
      return id;
    }, tenant, configurator);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/bibliographic-record/unlock/{id}")
  public void unlock(@PathVariable final String id,
                     @RequestParam final String uuid,
                     @RequestParam final String userName,
                     @RequestParam final LockEntityType type,
                     @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    doDelete((storageService, configuration) -> {
      if (isNotNullOrEmpty(id) && isNotNullOrEmpty(uuid) && type == LockEntityType.R) {
        storageService.unlockRecord(Integer.parseInt(id), userName);
        return uuid;
      }
      return null;
    }, tenant, configurator);
  }


  @PutMapping("/bibliographic-record/lock/{id}")
  public void lock(@PathVariable final String id,
                   @RequestParam final String uuid,
                   @RequestParam final String userName,
                   @RequestParam final LockEntityType type,
                   @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

    doPut((storageService, configuration) -> {

      storageService.lockRecord(Integer.parseInt(id), userName, uuid);
      return uuid;

    }, tenant, configurator, () -> isNotNullOrEmpty(id) && isNotNullOrEmpty(uuid) && type == LockEntityType.R);
  }


  /**
   * Sets category code on field.
   *
   * @param field          -- the field to set category.
   * @param storageService -- the storageService module.
   */
  private void setCategory(final Field field, final StorageService storageService) {
    if (isFixedField(field))
      field.getFixedField().setCategoryCode(Global.HEADER_CATEGORY);
    else if (getCategory(field) == 0) {
      boolean hasTitle = ((field.getCode().endsWith("00") || field.getCode().endsWith("10") || field.getCode().endsWith("11"))
        && field.getVariableField().getValue().contains(Global.SUBFIELD_DELIMITER + "t"));

      final int category = storageService.getTagCategory(field.getCode(),
        field.getVariableField().getInd1().charAt(0), field.getVariableField().getInd2().charAt(0), hasTitle);
      field.getVariableField().setCategoryCode(category);
    }

  }
}
