package org.folio.marccat.resources;

import org.apache.commons.lang.StringUtils;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.enumaration.CatalogingEntityType;
import org.folio.marccat.resources.domain.*;
import org.folio.marccat.resources.shared.FixedFieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.*;
import static org.folio.marccat.resources.shared.FixedFieldUtils.isFixedField;
import static org.folio.marccat.resources.shared.MappingUtils.toRecordTemplate;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class RecordTemplateAPI extends BaseResource {

  @GetMapping("/record-templates")
  public RecordTemplateCollection getRecordTemplates(
    @RequestParam final CatalogingEntityType type,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final List<Avp<Integer>> templates = storageService.getBibliographicRecordTemplates();
      final RecordTemplateCollection collection = new RecordTemplateCollection();
      collection.setRecordTemplates(templates.stream().map(toRecordTemplate).collect(toList()));
      return collection;
    }, tenant, okapiUrl, configurator);
  }


  @GetMapping("/record-template/{id}")
  public RecordTemplate getCatalogingRecordTemplatesById(
    @PathVariable final Integer id,
    @RequestParam final CatalogingEntityType type,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) ->
         storageService.getBibliographicRecordRecordTemplatesById(id)
      , tenant, okapiUrl, configurator);
  }


  @PostMapping("/record-template")
  public ResponseEntity<RecordTemplate> createNew(
    @RequestBody final RecordTemplate template,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doPost((storageService, configuration) -> {
        storageService.saveBibliographicRecordTemplate(template);
        return template;
      }
   , tenant, okapiUrl, configurator, () -> isNotNullOrEmpty(template.getName()));
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/record-template/{id}")
  public void update(
    @PathVariable final String id,
    @RequestBody final RecordTemplate template,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    doPut((storageService, configuration) -> {
      try {
        storageService.updateBibliographicRecordTemplate(template);
        return template;
      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, okapiUrl, configurator, () -> isNotNullOrEmpty(id) && isNotNullOrEmpty(template.getName()));
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/record-template/{id}")
  public void deleteCatalogingRecordTemplatesById(
    @PathVariable final String id,
    @RequestParam final CatalogingEntityType type,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    doDelete((storageService, configuration) -> {
      storageService.deleteBibliographicRecordTemplate(id);
      return id;
    }, tenant, okapiUrl, configurator);
  }


  @PostMapping("/record-template/from-record")
  public ResponseEntity<Object> createFromRecord(
    @RequestBody final BibliographicRecord record,
    @RequestParam final String templateName,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doPost((storageService, configuration) -> {
      try {
        final RecordTemplate template = new RecordTemplate();
        template.setName(templateName);
        template.setLeader(record.getLeader());
        template.setType("B");

        record.getFields().stream().filter(FixedFieldUtils::isFixedField)
          .filter(field -> !field.getCode().equals(Global.DATETIME_TRANSACTION_TAG_CODE)).forEach(field -> {

          Field newField = new Field();
          newField.setCode(field.getCode());
          newField.setMandatory(field.isMandatory());
          FixedField ff = new FixedField();
          ff.setHeaderTypeCode(field.getFixedField().getHeaderTypeCode());
          ff.setCategoryCode(field.getFixedField().getCategoryCode());
          ff.setDescription(field.getFixedField().getDescription());
          ff.setSequenceNumber(0);

          final String valueField = (field.getCode().equals(Global.MATERIAL_TAG_CODE) && isNotNullOrEmpty(field.getFixedField().getDisplayValue()))
            ? field.getFixedField().getDisplayValue().substring(0, 7) + StringUtils.repeat(" ", 8) + field.getFixedField().getDisplayValue().substring(15)
            : "";

          ff.setDisplayValue(valueField);

          ff.setCode(field.getCode());
          newField.setFixedField(ff);

          template.getFields().add(newField);
        });

        record.getFields().stream().filter(f -> !isFixedField(f)).forEach(field -> {
          Field newField = new Field();
          newField.setCode(field.getCode());
          newField.setMandatory(field.isMandatory());
          VariableField vf = new VariableField();
          vf.setCode(field.getCode());
          vf.setInd1(field.getVariableField().getInd1());
          vf.setInd2(field.getVariableField().getInd2());
          vf.setCategoryCode(field.getVariableField().getCategoryCode());
          vf.setDescription(field.getVariableField().getDescription());
          vf.setSequenceNumber(0);
          vf.setValue("");
          newField.setVariableField(vf);

          template.getFields().add(newField);
        });

        storageService.saveBibliographicRecordTemplate(template);
        return new ResponseEntity<>(template, HttpStatus.OK);

      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return record;
      }

    }, tenant, okapiUrl, configurator, () -> isNotNullOrEmpty(record.getId().toString()));
  }
}
