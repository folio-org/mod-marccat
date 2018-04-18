package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.LogicalViewCollection;
import org.folio.rest.jaxrs.model.View;
import org.folio.rest.jaxrs.resource.CatalogingLogicalViewsResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Logical views RESTful APIs.
 *
 * @author agazzarini
 * @since 1.0
 */
public class LogicalViewsAPI implements CatalogingLogicalViewsResource {
    protected final Log logger = new Log(LogicalViewsAPI.class);

    // This is the adapter that converts existing stringValue objects (logical views in this case)
    // in OKAPI resources.
    private Function<Avp<String>, View> adapter = source -> {
        final View logicalView = new View();
        logicalView.setCode(source.getValue());
        logicalView.setLongDescription(source.getLabel());
        return logicalView;
    };

    @Override
    public void getCatalogingLogicalViews(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> resultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final LogicalViewCollection container = new LogicalViewCollection();
                container.setViews(
                        storageService.getLogicalViews(lang)
                                .stream()
                                .map(adapter)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, resultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingLogicalViews(String lang, View entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}