package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.FunctionCode;
import org.folio.cataloging.resources.domain.FunctionCodeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Function codes RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Function code resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class FunctionCodeAPI extends BaseResource {

    private Function<Avp<String>, FunctionCode> toFunctionCode = source -> {
        final FunctionCode functionCode = new FunctionCode();
        functionCode.setCode(Integer.parseInt(source.getValue()));
        functionCode.setDescription(source.getLabel());
        return functionCode;
    };

    @ApiOperation(value = "Returns all function codes levels.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested code levels"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/function-codes")
    public FunctionCodeCollection getCatalogingFunctionCodes(
            @RequestParam final String marcCategory,
            @RequestParam final int code1,
            @RequestParam final int code2,
            @RequestParam final String code3,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
                final int category = "17".equals(marcCategory) ? Global.NAME_CATEGORY_DEFAULT : Integer.parseInt(marcCategory);
                return (storageService.existFunctionCodeByCategory(category))
                        ? ofNullable(storageService.getThirdCorrelation(category, code1, code2, lang))
                        .map(functionCodeList -> {
                            final FunctionCodeCollection container = new FunctionCodeCollection();
                            container.setFunctionCodes(functionCodeList
                                    .stream()
                                    .map(toFunctionCode)
                                    .collect(toList()));

                            return container;
                        }).orElse(null)
                        : null;
        }, tenant, configurator);
    }
}