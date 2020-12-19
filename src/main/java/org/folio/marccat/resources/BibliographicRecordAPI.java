package org.folio.marccat.resources;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.doDelete;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.integration.MarccatHelper.doPost;
import static org.folio.marccat.integration.MarccatHelper.doPut;
import static org.folio.marccat.resources.shared.ConversionFieldUtils.getDisplayValueOfMaterial;
import static org.folio.marccat.resources.shared.ConversionFieldUtils.getDisplayValueOfPhysicalInformation;
import static org.folio.marccat.resources.shared.RecordUtils.addTagTransactionDate;
import static org.folio.marccat.resources.shared.RecordUtils.isMandatory;
import static org.folio.marccat.resources.shared.RecordUtils.resetStatus;
import static org.folio.marccat.resources.shared.RecordUtils.setCategory;
import static org.folio.marccat.resources.shared.ValidationUtils.validate;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

import java.util.Map;

import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.resources.domain.BibliographicRecord;
import org.folio.marccat.resources.domain.ContainerRecordTemplate;
import org.folio.marccat.resources.domain.ErrorCollection;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.domain.LockEntityType;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.resources.shared.ConversionFieldUtils;
import org.folio.marccat.resources.shared.FixedFieldUtils;
import org.folio.marccat.resources.shared.RecordUtils;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.util.F;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class BibliographicRecordAPI extends RecordAPI {

  @GetMapping("/bibliographic-record/{id}")
  public ResponseEntity<Object> getRecord(@PathVariable final Integer id,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final ContainerRecordTemplate container = storageService.getBibliographicRecordById(id, view);
      if (container != null) {
        final BibliographicRecord record = container.getBibliographicRecord();
        if (record != null)
          resetStatus(record);
      } else {
        return new ResponseEntity<>(container, HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(container, HttpStatus.OK);
    }, tenant, okapiUrl, configurator);
  }

  @GetMapping("/bibliographic-record/from-template/{idTemplate}")
  public BibliographicRecord getEmptyRecord(@PathVariable final Integer idTemplate,
    @RequestParam final String lang,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final RecordTemplate template = storageService.getBibliographicRecordRecordTemplatesById(idTemplate);
      BibliographicRecord bibliographicRecord = new BibliographicRecord();
      getEmptyRecord(true, bibliographicRecord, template, lang, configuration, storageService);
      return bibliographicRecord;
    }, tenant, okapiUrl, configurator);
  }

  @PostMapping("/bibliographic-record")
  public ResponseEntity<Object> save(@RequestBody final ContainerRecordTemplate container,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {

    return doPost((storageService, configuration) -> {
      try {

        final BibliographicRecord record = container.getBibliographicRecord();
        RecordTemplate template = ofNullable(container.getRecordTemplate()).isPresent() ? container.getRecordTemplate()
            : null;
        record.getFields().forEach(field -> setCategory(field, view, storageService));
        final Integer itemNumber = record.getId();
        final ErrorCollection errors = validate(record, storageService);
        if (!errors.getErrors().isEmpty())
          return systemInternalFailure(new DataAccessException(), errors);

        final GeneralInformation gi = new GeneralInformation();
        gi.setDefaultValues(configuration);

        final Leader leader = record.getLeader();
        record.getFields().stream().filter(FixedFieldUtils::isFixedField)
            .filter(field -> field.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE)
                || field.getCode().equalsIgnoreCase(Global.OTHER_MATERIAL_TAG_CODE)
                || field.getCode().equalsIgnoreCase(Global.PHYSICAL_DESCRIPTION_TAG_CODE))
            .forEach(field -> {

              FixedField ff = field.getFixedField();
              final int headerTypeCode = ff.getHeaderTypeCode();

              if (field.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE)
                  || field.getCode().equalsIgnoreCase(Global.OTHER_MATERIAL_TAG_CODE)) {

                final Map<String, Object> mapRecordTypeMaterial = (field.getCode()
                    .equalsIgnoreCase(Global.MATERIAL_TAG_CODE))
                        ? RecordUtils
                          .getMaterialTypeInfosByLeaderValues(leader.getValue().charAt(6), leader.getValue().charAt(7), field.getCode(),
                              storageService.getSession())
                        : storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode, field.getCode());

                ConversionFieldUtils.setMaterialValuesInFixedField(ff,
                    (String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
              } else {
                ConversionFieldUtils.setPhysicalInformationValuesInFixedField(ff);
              }
            });

        storageService.saveBibliographicRecord(record, template, view, gi, lang, configuration);
        final ContainerRecordTemplate containerSaved = storageService.getBibliographicRecordById(itemNumber, view);
        resetStatus(containerSaved.getBibliographicRecord());
        return new ResponseEntity<>(containerSaved, HttpStatus.OK);

      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return new ResponseEntity<>(container, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }, tenant, okapiUrl, configurator, () -> isNotNullOrEmpty(container.getBibliographicRecord().getId().toString()));

  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/bibliographic-record/{id}")
  public void delete(@PathVariable final String id,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    doDelete((storageService, configuration) -> {
      storageService.deleteBibliographicRecordById(Integer.parseInt(id), view);
      return id;
    }, tenant, okapiUrl, configurator);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/bibliographic-record/unlock/{id}")
  public void unlock(@PathVariable final String id,
    @RequestParam final String uuid,
    @RequestParam final String userName,
    @RequestParam final LockEntityType type,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    doDelete((storageService, configuration) -> {
      if (isNotNullOrEmpty(id) && isNotNullOrEmpty(uuid) && type == LockEntityType.R) {
        storageService.unlockRecord(Integer.parseInt(id), userName);
        return uuid;
      }
      return null;
    }, tenant, okapiUrl, configurator);
  }

  @PutMapping("/bibliographic-record/lock/{id}")
  public void lock(@PathVariable final String id,
    @RequestParam final String uuid,
    @RequestParam final String userName,
    @RequestParam final LockEntityType type,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {

    doPut((storageService, configuration) -> {

      storageService.lockRecord(Integer.parseInt(id), userName, uuid);
      return uuid;

    }, tenant, okapiUrl, configurator,
        () -> isNotNullOrEmpty(id) && isNotNullOrEmpty(uuid) && type == LockEntityType.R);
  }

  @GetMapping("/bibliographic-record/duplicate")
  public ResponseEntity<Object> duplicate(@RequestParam final Integer id,
    @RequestParam final String lang,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {

    return doGet((storageService, configuration) -> {

      BibliographicRecord newRecord = new BibliographicRecord();
      newRecord.setId(storageService.generateNewKey(Global.AN_KEY_CODE_FIELD));

      final ContainerRecordTemplate container = storageService.getBibliographicRecordById(id, view);
      if (container == null)
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

      final BibliographicRecord fromRecord = container.getBibliographicRecord();
      RecordTemplate template = ofNullable(container.getRecordTemplate()).isPresent() ? container.getRecordTemplate()
          : null;

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

        if ((Integer.parseInt(field.getCode()) < Global.TAG_RELATION_MIN
            || Integer.parseInt(field.getCode()) > Global.TAG_RELATION_MAX)
            && !field.getCode().equals(Global.DATETIME_TRANSACTION_TAG_CODE) && !field.getCode().startsWith("9")) {

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
    }, tenant, okapiUrl, configurator);
  }

  @PostMapping("/bibliographic-record/fixed-field-display-value")
  public ResponseEntity<FixedField> getFixedFieldWithDisplayValue(@RequestBody final FixedField fixed,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doPost((storageService, configuration) -> {
      final int headerTypeCode = fixed.getHeaderTypeCode();
      final Map<String, Object> mapRecordTypeMaterial = storageService.getMaterialTypeInfosByHeaderCode(headerTypeCode,
          fixed.getCode());
      if (fixed.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE)
          || fixed.getCode().equalsIgnoreCase(Global.OTHER_MATERIAL_TAG_CODE)) {
        return getDisplayValueOfMaterial(fixed, (String) mapRecordTypeMaterial.get(Global.FORM_OF_MATERIAL_LABEL));
      } else {
        return getDisplayValueOfPhysicalInformation(fixed);
      }
    }, tenant, okapiUrl, configurator, () -> (isNotNullOrEmpty(fixed.getCode())));
  }
}
