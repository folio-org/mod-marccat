package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.StatusType;
import org.folio.cataloging.resources.domain.StatusTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Status type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Status type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class StatusTypeAPI extends BaseResource {

  private Function <Avp <String>, StatusType> toStatusType = source -> {
    final StatusType statusType = new StatusType();
    statusType.setCode(source.getValue());
    statusType.setDescription(source.getLabel());
    return statusType;
  };

  @ApiOperation(value = "Returns all types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/status-types")
  public StatusTypeCollection getStatusTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final StatusTypeCollection container = new StatusTypeCollection();
      container.setStatusTypes(
        storageService.getStatusTypes(lang)
          .stream()
          .map(toStatusType)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
