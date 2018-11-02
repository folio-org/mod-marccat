package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.ShelfListType;
import org.folio.cataloging.resources.domain.ShelfListTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * ShelfList type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Shelflist type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ShelfListTypeAPI extends BaseResource {

  private Function <Avp <String>, ShelfListType> toShelfListType = source -> {
    final ShelfListType shelfListType = new ShelfListType();
    shelfListType.setCode(source.getValue());
    shelfListType.setDescription(source.getLabel());
    return shelfListType;
  };

  @ApiOperation(value = "Returns all types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/shelflist-types")
  public ShelfListTypeCollection getShelfListTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final ShelfListTypeCollection container = new ShelfListTypeCollection();
      container.setShelfListTypes(
        storageService.getShelfListTypes(lang)
          .stream()
          .map(toShelfListType)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
