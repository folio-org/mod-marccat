package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.Retention;
import org.folio.marccat.resources.domain.RetentionCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Retentions RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Retention resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class RetentionAPI extends BaseResource {

  private Function <Avp <String>, Retention> toRetention = source -> {
    final Retention retention = new Retention();
    retention.setCode(Integer.parseInt(source.getValue()));
    retention.setDescription(source.getLabel());
    return retention;
  };

  @ApiOperation(value = "Returns all retentions associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested retentions."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/retentions")
  public RetentionCollection getCatalogingRetentions(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final RetentionCollection container = new RetentionCollection();
      container.setRetentions(
        storageService.getRetentions(lang)
          .stream()
          .map(toRetention)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
