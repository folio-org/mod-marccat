package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.EncodingLevel;
import org.folio.cataloging.resources.domain.EncodingLevelCollection;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Encoding Levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Encoding level resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class EncodingLevelsAPI extends BaseResource {

    private Function<Avp<String>, EncodingLevel> toEncodingLevel = source -> {
        final EncodingLevel encodingLevel = new EncodingLevel();
        encodingLevel.setCode(source.getValue());
        encodingLevel.setDescription(source.getLabel());
        return encodingLevel;
    };

    @ApiOperation(value = "Returns all encoding levels associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested encoding levels"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/encoding-levels")
    public EncodingLevelCollection getEncodingLevels(
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final EncodingLevelCollection container = new EncodingLevelCollection();
                container.setEncodingLevels(
                        storageService.getEncodingLevels(lang)
                                .stream()
                                .map(toEncodingLevel)
                                .collect(toList()));
                return container;
        }, tenant, configurator);

    }
}
