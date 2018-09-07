package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.resources.domain.TagMarcEncoding;
import org.springframework.web.bind.annotation.*;

import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * ValidationTagAPI RestFul service to validate variable tag.
 *
 * @author nbianchini
 * @since 1.0
 */

@RestController
@Api(value = "modcat-api", description = "Validation tag API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ValidationTagAPI extends BaseResource {

    @ApiOperation(value = "Returns tag marc encoding associated with the given data.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested tag marc encoding."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/validation-tag")
    public TagMarcEncoding getTagMarcEncoding(
            @RequestParam final Integer categoryCode,
            @RequestParam final Integer code1,
            @RequestParam final Integer code2,
            @RequestParam final Integer code3,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

        return doGet((storageService, configuration) -> {
            final int wrapCode2 =  (code2 == null)? -1: code2;
            final int wrapCode3 =  (code3 == null)? -1: code3;

            final TagMarcEncoding tagMarcEncoding = storageService.getTagMarcEncoding(categoryCode, code1, wrapCode2, wrapCode3);
            return tagMarcEncoding;

        }, tenant, configurator);
    }
}
