package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.Currency;
import org.folio.cataloging.resources.domain.CurrencyCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Currency RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Currency resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class CurrencyAPI extends BaseResource {

  private Function <Avp <String>, Currency> toCurrency = source -> {
    final Currency currency = new Currency();
    currency.setCode(Integer.parseInt(source.getValue()));
    currency.setDescription(source.getLabel());
    return currency;
  };

  @ApiOperation(value = "Returns all currencies")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested currencies"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/currencies")
  public CurrencyCollection getCurrencies(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final CurrencyCollection container = new CurrencyCollection();
      container.setCurrencies(
        storageService.getCurrencies(lang)
          .stream()
          .map(toCurrency)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
