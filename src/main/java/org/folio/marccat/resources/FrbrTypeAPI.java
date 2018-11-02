package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.FrbrType;
import org.folio.marccat.resources.domain.FrbrTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * FRBR type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "FRBR Type resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class FrbrTypeAPI extends BaseResource {

  private Function <Avp <String>, FrbrType> toFrbrType = source -> {
    final FrbrType frbrType = new FrbrType();
    frbrType.setCode(Integer.parseInt(source.getValue()));
    frbrType.setDescription(source.getLabel());
    return frbrType;
  };

  @ApiOperation(value = "Returns all frbr types.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested frbr types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/frbr-types")
  public FrbrTypeCollection getFrbrTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FrbrTypeCollection container = new FrbrTypeCollection();
      container.setFrbrTypes(
        storageService.getFrbrTypes(lang)
          .stream()
          .map(toFrbrType)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }

  @ApiOperation(value = "Returns all frbr types.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested frbr types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/frbr-types/{code}")
  public FrbrType getFrbrTypesByCode(
    @PathVariable("code") final String code,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FrbrType frbrType = new FrbrType();
      frbrType.setCode(Integer.parseInt(code));
      frbrType.setDescription(storageService.getFrbrDescriptionByCode(code, lang));
      return frbrType;
    }, tenant, configurator);
  }
}
