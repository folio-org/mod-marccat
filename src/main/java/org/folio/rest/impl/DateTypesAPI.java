package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.DateType;
import org.folio.rest.jaxrs.model.DateTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingDateTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Date Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class DateTypesAPI implements CatalogingDateTypesResource {

    protected final Log logger = new Log(DateTypesAPI.class);

    private Function<Avp<String>, DateType> toDateType = source -> {
        final DateType dateType = new DateType();
        dateType.setCode(source.getValue());
        dateType.setDescription(source.getLabel());
        return dateType;
    };

    @Override
    public void getCatalogingDateTypes(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final DateTypeCollection container = new DateTypeCollection();
                container.setDateTypes(
                        storageService.getDateTypes(lang)
                                .stream()
                                .map(toDateType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingDateTypes(String lang, DateType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
