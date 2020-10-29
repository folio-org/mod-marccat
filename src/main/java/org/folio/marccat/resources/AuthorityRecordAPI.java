package org.folio.marccat.resources;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.integration.MarccatHelper.doPost;
import static org.folio.marccat.resources.shared.RecordUtils.getLeaderValue;
import static org.folio.marccat.resources.shared.RecordUtils.resetStatus;
import static org.folio.marccat.resources.shared.RecordUtils.setCategory;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.integration.AuthorityStorageService;
import org.folio.marccat.resources.domain.AuthorityRecord;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.resources.domain.VariableField;
import org.folio.marccat.resources.shared.FixedFieldUtils;
import org.folio.marccat.shared.GeneralInformation;
import org.folio.marccat.util.F;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author elena
 *
 */
@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class AuthorityRecordAPI extends BaseResource {

  @PostMapping("/authority-record")
  public ResponseEntity<Object> save(@RequestBody final AuthorityRecord record,
      // ContainerRecordTemplate container,
      @RequestParam(name = "view", defaultValue = View.DEFAULT_AUTHORITY_VIEW_AS_STRING) final int view,
      @RequestParam final String lang, @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl) {

    return doPost((storageService, configuration) -> {
      try {

        AuthorityStorageService authorityStorageService = new AuthorityStorageService();

        authorityStorageService.setStorageService(storageService);

        record.getFields().forEach(field -> setCategory(field, storageService));

        final GeneralInformation gi = new GeneralInformation();
        gi.setDefaultValues(configuration);

        record.getFields().stream().filter(FixedFieldUtils::isFixedField)
            .filter(field -> field.getCode().equalsIgnoreCase(Global.MATERIAL_TAG_CODE)).forEach(field -> {
            });

        authorityStorageService.saveAuthorityRecord(record, view, lang, configuration);
        return new ResponseEntity<>("1", HttpStatus.CREATED);

      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return new ResponseEntity<>("0", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }, tenant, okapiUrl, configurator, () -> isNotNullOrEmpty(record.getId().toString()));

  }

  @GetMapping("/authority-record/from-template/{idTemplate}")
  public AuthorityRecord getEmptyRecord(@PathVariable final Integer idTemplate, @RequestParam final String lang,
      @RequestParam(name = "view", defaultValue = View.DEFAULT_AUTHORITY_VIEW_AS_STRING) final int view,
      @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      AuthorityStorageService authorityStorageService = new AuthorityStorageService();

      authorityStorageService.setStorageService(storageService);
      AuthorityRecord authorityRecord = new AuthorityRecord();
      final RecordTemplate template = authorityStorageService.getAuthorityRecordRecordTemplatesById(idTemplate);
      authorityRecord.setId(storageService.generateNewKey(Global.AN_KEY_AUT));
      authorityRecord.setLeader(ofNullable(template.getLeader()).map(leader -> template.getLeader()).orElseGet(() -> {
        Leader leader = new Leader();
        leader.setCode(Global.LEADER_TAG_NUMBER);
        leader.setValue(getLeaderValue(false));
        return leader;
      }));

      for (Field field : template.getFields()) {
        if (field.isMandatory() && !field.getCode().equals(Global.DATETIME_TRANSACTION_TAG_CODE)) {
          if (field.getCode().equals(Global.CONTROL_NUMBER_TAG_CODE)) {
            FixedField controlNumber = field.getFixedField();
            controlNumber.setDisplayValue(F.padNumber("0", 11, authorityRecord.getId()));
            field.setFixedField(controlNumber);
          }
          if (field.getCode().equals(Global.MATERIAL_TAG_CODE)) {
            FixedField materialTag = field.getFixedField();
            String dateEnteredOnFile = F.getFormattedToday("yyMMdd");
            materialTag.setDateEnteredOnFile(dateEnteredOnFile);
            materialTag.setDisplayValue(dateEnteredOnFile + materialTag.getDisplayValue().substring(6));
            field.setFixedField(materialTag);
          }
          if (field.getCode().equals(Global.CATALOGING_SOURCE_TAG_CODE)) {
            VariableField variableField = field.getVariableField();
            variableField.setValue(configuration.get("authority.cataloguingSourceStringText"));
          }
          field.setFieldStatus(Field.FieldStatus.NEW);
          authorityRecord.getFields().add(field);
        }
      }

      FixedField fixed005 = new FixedField();
      fixed005.setHeaderTypeCode(Global.AUT_DATETIME_TRANSACION_HEADER_TYPE);
      fixed005.setCode(Global.DATETIME_TRANSACTION_TAG_CODE);
      fixed005.setDisplayValue(F.getFormattedToday("yyyyMMddHHmmss."));
      fixed005.setCategoryCode(Global.INT_CATEGORY);
      fixed005.setDescription(storageService.getHeadingTypeDescription(Global.AUT_DATETIME_TRANSACION_HEADER_TYPE, lang,
          Global.INT_CATEGORY));

      final Field field = new Field();
      field.setCode(Global.DATETIME_TRANSACTION_TAG_CODE);
      field.setMandatory(true);
      field.setFixedField(fixed005);
      field.setFieldStatus(Field.FieldStatus.NEW);
      authorityRecord.getFields().add(1, field);

      authorityRecord.setVerificationLevel(configuration.get("authority.verificationLevel"));
      authorityRecord.setCanadianContentIndicator("0");
      resetStatus(authorityRecord);
      return authorityRecord;
    }, tenant, okapiUrl, configurator);
  }
}
