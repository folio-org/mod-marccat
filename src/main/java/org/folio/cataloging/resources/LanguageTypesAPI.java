package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.LanguageType;
import org.folio.cataloging.resources.domain.LanguageTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Language Types RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Language type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class LanguageTypesAPI extends BaseResource {

    private Function<Avp<String>, LanguageType> toLanguageType = source -> {
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
    @GetMapping("/item-types")
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
