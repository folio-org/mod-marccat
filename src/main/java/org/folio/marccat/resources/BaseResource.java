package org.folio.marccat.resources;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.resources.shared.RecordUtils.getLeaderValue;
import static org.folio.marccat.resources.shared.RecordUtils.resetStatus;

import java.util.Map;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.SystemInternalFailureException;
import org.folio.marccat.integration.Configuration;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.ErrorCollection;
import org.folio.marccat.resources.domain.Field;
import org.folio.marccat.resources.domain.FixedField;
import org.folio.marccat.resources.domain.Leader;
import org.folio.marccat.resources.domain.Record;
import org.folio.marccat.resources.domain.RecordTemplate;
import org.folio.marccat.resources.domain.VariableField;
import org.folio.marccat.util.F;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

public abstract class BaseResource {
  protected Log logger = new Log(getClass());

  @Autowired
  protected Configuration configurator;

  @Autowired
  protected RestTemplate restTemplate;

  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "System internal failure has occurred.")
  @ExceptionHandler(SystemInternalFailureException.class)
  public ResponseEntity<Object> systemInternalFailure(final Exception exception, final ErrorCollection errors) {
    logger.error(Message.MOD_MARCCAT_00011_NWS_FAILURE, exception);
    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ResponseEntity<Object> notFoundFailure(final ErrorCollection errors, final String message) {
    logger.error(message, errors);
    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
  }

  public void getEmptyRecord(final boolean isBib, final Record record, final RecordTemplate template, final String lang,
      final Map<String, String> configuration, final StorageService storageService) {
    if (isBib)
      record.setId(storageService.generateNewKey(Global.AN_KEY_CODE_FIELD));
    else
      record.setId(storageService.generateNewKey(Global.AN_KEY_AUT));
    record.setLeader(ofNullable(template.getLeader()).map(leader -> template.getLeader()).orElseGet(() -> {
      Leader leader = new Leader();
      leader.setCode(Global.LEADER_TAG_NUMBER);
      leader.setValue(getLeaderValue(isBib));
      return leader;
    }));

    for (Field field : template.getFields()) {
      if (field.isMandatory() && !field.getCode().equals(Global.DATETIME_TRANSACTION_TAG_CODE)) {
        if (field.getCode().equals(Global.CONTROL_NUMBER_TAG_CODE)) {
          FixedField controlNumber = field.getFixedField();
          controlNumber.setDisplayValue(F.padNumber("0", 11, record.getId()));
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
          if (isBib)
            variableField.setValue(configuration.get("bibliographicItem.cataloguingSourceStringText"));
          else
            variableField.setValue(configuration.get("authority.cataloguingSourceStringText"));
        }
        field.setFieldStatus(Field.FieldStatus.NEW);
        record.getFields().add(field);
      }
    }

    FixedField fixed005 = new FixedField();
    if (isBib)
      fixed005.setHeaderTypeCode(Global.DATETIME_TRANSACION_HEADER_TYPE);
    else
      fixed005.setHeaderTypeCode(Global.AUT_DATETIME_TRANSACION_HEADER_TYPE);
    fixed005.setCode(Global.DATETIME_TRANSACTION_TAG_CODE);
    fixed005.setDisplayValue(F.getFormattedToday("yyyyMMddHHmmss."));
    fixed005.setCategoryCode(Global.INT_CATEGORY);
    fixed005.setDescription(
        storageService.getHeadingTypeDescription(fixed005.getHeaderTypeCode(), lang, Global.INT_CATEGORY));

    final Field field = new Field();
    field.setCode(Global.DATETIME_TRANSACTION_TAG_CODE);
    field.setMandatory(true);
    field.setFixedField(fixed005);
    field.setFieldStatus(Field.FieldStatus.NEW);
    record.getFields().add(1, field);
    if (isBib)
      record.setVerificationLevel(configuration.get("bibliographicItem.verificationLevel"));
    else
      record.setVerificationLevel(configuration.get("authority.verificationLevel"));
    record.setCanadianContentIndicator("0");
    resetStatus(record);
  }

}
