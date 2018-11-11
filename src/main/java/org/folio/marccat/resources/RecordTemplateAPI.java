package org.folio.marccat.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.log.MessageCatalog;
import org.folio.marccat.resources.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.F.isNotNullOrEmpty;
import static org.folio.marccat.integration.CatalogingHelper.*;
import static org.folio.marccat.resources.domain.CatalogingEntityType.A;

/**
 * BIB / AUT Record templates API.
 *
 * @author agazzarini
 * @author carment
 * @since 1.0
 */
@RestController
@CrossOrigin("http://localhost:3000")
@Api(value = "modcat-api", description = "Record template resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class RecordTemplateAPI extends BaseResource {

  private Function <Avp <Integer>, RecordTemplate> toRecordTemplate = avp -> {
    final RecordTemplate template = new RecordTemplate();
    template.setId(avp.getValue());
    template.setName(avp.getLabel());
    return template;

  };

  @ApiOperation(value = "Returns all templates associated with a given type")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested templates."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/record-templates")
  public RecordTemplateCollection getRecordTemplates(
    @RequestParam final CatalogingEntityType type,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final List <Avp <Integer>> templates =
        type == A
          ? storageService.getAuthorityRecordTemplates()
          : storageService.getBibliographicRecordTemplates();

      final RecordTemplateCollection collection = new RecordTemplateCollection();
      collection.setRecordTemplates(templates.stream().map(toRecordTemplate).collect(toList()));
      return collection;
    }, tenant, configurator);
  }

  @ApiOperation(value = "Returns the template associated with a given type and identifier")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested template."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
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

  @ApiOperation(value = "Creates a new template.")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Method successfully created the new template."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @PostMapping("/record-template")
  public ResponseEntity <RecordTemplate> createNew(
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

  @ApiOperation(value = "Updates an existing template.")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Method successfully updated the template."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
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

  @ApiOperation(value = "Deletes a template.")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Method successfully deleted the target template."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
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

  @ApiOperation(value = "Creates a new template from a bibliographic record.")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Method successfully created the new template."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @PostMapping("/record-template/from-record")
  public ResponseEntity <Object> createFromRecord(
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

        record.getFields().stream().filter(this::isFixedField)
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
        return new ResponseEntity <>(template, HttpStatus.OK);

      } catch (final Exception exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return record;
      }

    }, tenant, configurator, () -> isNotNullOrEmpty(record.getId().toString()));
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
}
