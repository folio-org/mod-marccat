package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.Retention;
import org.folio.rest.jaxrs.model.RetentionCollection;
import org.folio.rest.jaxrs.resource.CatalogingRetentionsResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


/**
 * Retentions RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class RetentionsAPI implements CatalogingRetentionsResource {

    protected final Log logger = new Log(RetentionsAPI.class);

    private Function<Avp<String>, Retention> toRetention = source -> {
        final Retention retention = new Retention();
        retention.setCode(Integer.parseInt(source.getValue()));
        retention.setDescription(source.getLabel());
        return retention;
    };

    @Override
    public void getCatalogingRetentions(final String lang,
                              final Map<String, String> okapiHeaders,
                              final Handler<AsyncResult<Response>> asyncResultHandler,
                              final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final RetentionCollection container = new RetentionCollection();
                container.setRetentions(
                        storageService.getRetentions(lang)
                                .stream()
                                .map(toRetention)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);


    }

    @Override
    public void postCatalogingRetentions(String lang, Retention entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
