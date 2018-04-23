package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.Subscription;
import org.folio.cataloging.resources.domain.SubscriptionCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Subscription RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Subscription resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class SubscriptionAPI extends BaseResource {

    private Function<Avp<String>, Subscription> toSubscription = source -> {
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
    public SubscriptionCollection getSubscriptions (
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final SubscriptionCollection container = new SubscriptionCollection();
                container.setSubscriptions (
                        storageService.getSubscriptions(lang)
                                .stream()
                                .map(toSubscription)
                                .collect(toList()));
                return container;
        }, tenant, configurator);
    }
}