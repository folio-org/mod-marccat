package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.CatalogSource;
import org.folio.marccat.resources.domain.CatalogSourceCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Catalog Sources RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Catalog source resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class CatalogSourceAPI extends BaseResource {

  private Function <Avp <String>, CatalogSource> toCatalogSource = source -> {
    final CatalogSource catalogSource = new CatalogSource();
    catalogSource.setCode(source.getValue());
    catalogSource.setDescription(source.getLabel());
    return catalogSource;
  };

  @ApiOperation(value = "Returns all catalog sources associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested catalog sources"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/catalog-sources")
  public CatalogSourceCollection getCatalogSources(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final CatalogSourceCollection container = new CatalogSourceCollection();
      container.setCatalogSources(
        storageService
          .getCatalogSources(lang)
          .stream()
          .map(toCatalogSource)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
