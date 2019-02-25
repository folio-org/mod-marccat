package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.Global;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.enumaration.CatalogingEntityType;
import org.folio.marccat.resources.domain.*;
import org.folio.marccat.resources.shared.FixeFieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.enumaration.CatalogingEntityType.A;
import static org.folio.marccat.integration.MarccatHelper.*;
import static org.folio.marccat.resources.shared.FixeFieldUtils.isFixedField;
import static org.folio.marccat.resources.shared.MappingUtils.toRecordTemplate;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

/**
 * BIB / AUT Record templates API.
 *
 * @author agazzarini
 * @author carment
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class RecordTemplateAPI extends BaseResource {

  @GetMapping("/record-templates")
  public RecordTemplateCollection getRecordTemplates(
    @RequestParam final CatalogingEntityType type,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final List<Avp<Integer>> templates =
        type == A
          ? storageService.getAuthorityRecordTemplates()
          : storageService.getBibliographicRecordTemplates();

      final RecordTemplateCollection collection = new RecordTemplateCollection();
      collection.setRecordTemplates(templates.stream().map(toRecordTemplate).collect(toList()));
      return collection;
    }, tenant, configurator);
  }


  @GetMapping("/record-template/{id}")
  public RecordTemplate getCatalogingRecordTemplatesById(
    @PathVariable final Integer id,
    @RequestParam final CatalogingEntityType type,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) ->
        type == A
          ? storageService.getAuthorityRecordRecordTemplatesById(id)
          : storageService.getBibliographicRecordRecordTemplatesById(id)
      , tenant, configurator);
  }


  @PostMapping("/record-template")
  public ResponseEntity<RecordTemplate> createNew(
    @RequestBody final RecordTemplate template,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doPost((storageService, configuration) -> {
      if ("A".equals(template.getType())) {
        storageService.saveAuthorityRecordTemplate(template);
      } else {
        storageService.saveBibliographicRecordTemplate(template);
      }
      return template;
    }, tenant, configurator, () -> isNotNullOrEmpty(template.getName()));
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/record-template/{id}")
  public void update(
    @PathVariable final String id,
    @RequestBody final RecordTemplate template,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    doPut((storageService, configuration) -> {
      try {
        final ObjectMapper mapper = new ObjectMapper();
        final String jsonInString = mapper.writeValueAsString(template);
        if ("A".equals(template.getType())) {
          storageService.updateAuthorityRecordTemplate(template);
        } else {
          storageService.updateBibliographicRecordTemplate(template);
        }
        return template;
      } catch (final Exception exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator, () -> isNotNullOrEmpty(id) && isNotNullOrEmpty(template.getName()));
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/record-template/{id}")
  public void deleteCatalogingRecordTemplatesById(
    @PathVariable final String id,
    @RequestParam final CatalogingEntityType type,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    doDelete((storageService, configuration) -> {
      switch (type) {
        case A:
          storageService.deleteAuthorityRecordTemplate(id);
          break;
        case B:
          storageService.deleteBibliographicRecordTemplate(id);
      }
      return id;
    }, tenant, configurator);
  }


  @PostMapping("/record-template/from-record")
  public ResponseEntity<Object> createFromRecord(
    @RequestBody final BibliographicRecord record,
    @RequestParam final String templateName,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doPost((storageService, configuration) -> {
      try {
        final RecordTemplate template = new RecordTemplate();
        template.setName(templateName);
        template.setLeader(record.getLeader());
        template.setType("B");

        record.getFields().stream().filter(FixeFieldUtils::isFixedField)
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
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return record;
      }

    }, tenant, configurator, () -> isNotNullOrEmpty(record.getId().toString()));
  }
}
