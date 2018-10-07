package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.RecordType;
import org.folio.cataloging.resources.domain.RecordTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Record Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Record type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class RecordTypeAPI extends BaseResource {

  private Function <Avp <String>, RecordType> toRecordType = source -> {
    final RecordType recordType = new RecordType ( );
    recordType.setCode (source.getValue ( ));
    recordType.setDescription (source.getLabel ( ));
    return recordType;
  };

  @ApiOperation(value = "Returns all types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/record-types")
  public RecordTypeCollection getRecordTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final RecordTypeCollection container = new RecordTypeCollection ( );
      container.setRecordTypes (
        storageService.getRecordTypes (lang)
          .stream ( )
          .map (toRecordType)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);
  }
}
