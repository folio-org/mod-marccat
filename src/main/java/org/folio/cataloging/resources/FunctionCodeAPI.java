package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.FunctionCode;
import org.folio.rest.jaxrs.model.FunctionCodeCollection;
import org.folio.rest.jaxrs.resource.CatalogingFunctionCodesResource;

import javax.ws.rs.core.Response;
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

public class FunctionCodeAPI implements CatalogingFunctionCodesResource {

    protected final Log logger = new Log(FunctionCodeAPI.class);

    private Function<Avp<String>, FunctionCode> toFunctionCode = source -> {
        final FunctionCode functionCode = new FunctionCode();
        functionCode.setCode(Integer.parseInt(source.getValue()));
        functionCode.setDescription(source.getLabel());
        return functionCode;
    };

    @Override
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
