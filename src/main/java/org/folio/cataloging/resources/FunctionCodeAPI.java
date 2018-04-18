package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.FunctionCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

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
    public void getCatalogingFunctionCodes(final String marcCategory,
                                           final String code1,
                                           final String code2,
                                           final String lang,
                                           final Map<String, String> okapiHeaders,
                                           final Handler<AsyncResult<Response>> asyncResultHandler,
                                           final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {

                final int category = (marcCategory.equals("17") ? Global.NAME_CATEGORY_DEFAULT : Integer.parseInt(marcCategory));
                final int intCode1 = Integer.parseInt(code1);
                final int intCode2 = Integer.parseInt(code2);

                return (storageService.existFunctionCodeByCategory(category))
                        ? ofNullable(storageService.getThirdCorrelation(category, intCode1, intCode2, lang))
                        .map(functionCodeList -> {
                            final FunctionCodeCollection container = new FunctionCodeCollection();
                            container.setFunctionCodes(functionCodeList
                                    .stream()
                                    .map(toFunctionCode)
                                    .collect(toList()));

                            return container;
                        }).orElse(null)
                        : null;

            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingFunctionCodes(String lang, FunctionCode entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
