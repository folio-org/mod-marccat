package org.folio.marccat.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.Subscription;
import org.folio.marccat.resources.domain.SubscriptionCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Subscription RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Subscription resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class SubscriptionAPI extends BaseResource {

  private Function <Avp <String>, Subscription> toSubscription = source -> {
    final Subscription subscription = new Subscription();
    subscription.setCode(source.getValue());
    subscription.setDescription(source.getLabel());
    return subscription;
  };

  @ApiOperation(value = "Returns all subscriptions associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested subscriptions."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/subscriptions")
  public SubscriptionCollection getSubscriptions(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final SubscriptionCollection container = new SubscriptionCollection();
      container.setSubscriptions(
        storageService.getSubscriptions(lang)
          .stream()
          .map(toSubscription)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
