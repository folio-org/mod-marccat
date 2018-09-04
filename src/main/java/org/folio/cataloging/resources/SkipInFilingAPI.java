package org.folio.cataloging.resources;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

import java.util.function.Function;

import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.SkipInFiling;
import org.folio.cataloging.resources.domain.SkipInFilingCollection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * SkipInFiling RESTful APIs.
 *
 * @author mpascucci
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "SkipInFiling resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class SkipInFilingAPI extends BaseResource {
    private Function<Avp<String>, SkipInFiling> toSkipInFile = source -> {
        final SkipInFiling skipInFile = new SkipInFiling();
        skipInFile.setCode(Integer.parseInt(source.getValue()));
        skipInFile.setDescription(source.getLabel());
        return skipInFile;
    };

    @ApiOperation(value = "Returns all skip in associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested skip in filing"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/skipinfiling")
    public SkipInFilingCollection getSkipInFiling(
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
            final SkipInFilingCollection container = new SkipInFilingCollection();
            container.setSkipInFiling(
                        storageService.getSkipInFiling(lang)
                            .stream()
                            .map(toSkipInFile)
                            .collect(toList()));
            return container;
        }, tenant, configurator);
    }
}