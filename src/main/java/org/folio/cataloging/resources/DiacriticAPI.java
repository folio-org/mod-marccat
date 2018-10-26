package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.resources.domain.Diacritic;
import org.folio.cataloging.resources.domain.DiacriticCollection;
import org.folio.cataloging.shared.MapDiacritic;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Authority Sources RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "diacritic API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class DiacriticAPI extends BaseResource {

  private Function <MapDiacritic, Diacritic> toDiacritic = source -> {
    final Diacritic diacritic = new Diacritic();
    diacritic.setCode(source.getCode());
    diacritic.setDescription(source.getDescription());
    diacritic.setUnicode(source.getUnicode());
    diacritic.setCharacterSet(source.getCharacterSet());
    diacritic.setCharacter(source.getCharacter());
    return diacritic;
  };

  @ApiOperation(value = "Returns all diacritics associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested authority sources"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/diacritics")
  public DiacriticCollection getDiacritics(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final DiacriticCollection container = new DiacriticCollection();
      container.setDiacritics(
        storageService.getDiacritics(lang)
          .stream()
          .map(toDiacritic)
          .collect(toList()));
      return container;
    }, tenant, configurator);

  }
}
