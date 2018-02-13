package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.ControlType;
import org.folio.rest.jaxrs.model.ControlTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingControlTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Acquisition Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class ControlTypesAPI implements CatalogingControlTypesResource {

    protected final Log logger = new Log(ControlTypesAPI.class);

    private Function<Avp<String>, ControlType> toControlType = source -> {
        final ControlType controlType = new ControlType();
        controlType.setCode(source.getValue());
        controlType.setDescription(source.getLabel());
        return controlType;
    };

    @Override
    public void getCatalogingControlTypes(final String lang,
                                    final Map<String, String> okapiHeaders,
                                    final Handler<AsyncResult<Response>> asyncResultHandler,
                                    final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final ControlTypeCollection container = new ControlTypeCollection();
                container.setControlTypes(
                        storageService.getControlTypes(lang)
                                .stream()
                                .map(toControlType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingControlTypes(String lang, ControlType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
