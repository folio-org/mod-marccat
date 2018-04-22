package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.DateType;
import org.folio.cataloging.resources.domain.DateTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Date Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Date types resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class DateTypesAPI extends BaseResource {

    private Function<Avp<String>, DateType> toDateType = source -> {
        final DateType dateType = new DateType();
        dateType.setCode(source.getValue());
        dateType.setDescription(source.getLabel());
        return dateType;
    };

    @ApiOperation(value = "Returns all date types")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested date types"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/date-types")
    public DateTypeCollection getDateTypes(
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final DateTypeCollection container = new DateTypeCollection();
                container.setDateTypes(
                        storageService.getDateTypes(lang)
                                .stream()
                                .map(toDateType)
                                .collect(toList()));
                return container;
        }, tenant, configurator);
    }
}
