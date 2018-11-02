package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.LanguageType;
import org.folio.marccat.resources.domain.LanguageTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Language Types RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Language type resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class LanguageTypeAPI extends BaseResource {

  private Function <Avp <String>, LanguageType> toLanguageType = source -> {
    final LanguageType languageType = new LanguageType();
    languageType.setCode(source.getValue());
    languageType.setDescription(source.getLabel());
    return languageType;
  };

  @ApiOperation(value = "Returns all language types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested language types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/language-types")
  public LanguageTypeCollection getLanguageTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final LanguageTypeCollection container = new LanguageTypeCollection();
      container.setLanguageTypes(
        storageService.getLanguageTypes(lang)
          .stream()
          .map(toLanguageType)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
