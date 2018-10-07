package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.ModifiedRecordType;
import org.folio.cataloging.resources.domain.ModifiedRecordTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Modified Record Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Modified Record type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ModifiedRecordTypeAPI extends BaseResource {

  private Function <Avp <String>, ModifiedRecordType> toModifiedRecordType = source -> {
    final ModifiedRecordType modifiedRecordType = new ModifiedRecordType ( );
    modifiedRecordType.setCode (source.getValue ( ));
    modifiedRecordType.setDescription (source.getLabel ( ));
    return modifiedRecordType;
  };

  @ApiOperation(value = "Returns all types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/modified-record-types")
  public ModifiedRecordTypeCollection getModifiedRecordTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final ModifiedRecordTypeCollection container = new ModifiedRecordTypeCollection ( );
      container.setModifiedRecordTypes (
        storageService.getModifiedRecordTypes (lang)
          .stream ( )
          .map (toModifiedRecordType)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);
  }
}
