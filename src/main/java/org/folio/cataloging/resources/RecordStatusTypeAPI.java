package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.RecordStatusType;
import org.folio.cataloging.resources.domain.RecordStatusTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Record Status Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Record status resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class RecordStatusTypeAPI extends BaseResource {

  private Function <Avp <String>, RecordStatusType> toRecordStatusType = source -> {
    final RecordStatusType recordStatusType = new RecordStatusType();
    recordStatusType.setCode(source.getValue());
    recordStatusType.setDescription(source.getLabel());
    return recordStatusType;
  };

  @ApiOperation(value = "Returns all types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/record-status-types")
  public RecordStatusTypeCollection getCatalogingRecordStatusTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final RecordStatusTypeCollection container = new RecordStatusTypeCollection();
      container.setRecordStatusTypes(
        storageService.getRecordStatusTypes(lang)
          .stream()
          .map(toRecordStatusType)
          .collect(toList()));
      return container;
    }, tenant, configurator);

  }
}
