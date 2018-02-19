package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.CatalogSource;
import org.folio.rest.jaxrs.model.CatalogSourceCollection;
import org.folio.rest.jaxrs.resource.CatalogingCatalogSourcesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Catalog Sources RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class CatalogSourcesAPI implements CatalogingCatalogSourcesResource {

    protected final Log logger = new Log(CatalogSourcesAPI.class);

    private Function<Avp<String>, CatalogSource> toCatalogSource = source -> {
        final CatalogSource catalogSource = new CatalogSource();
        catalogSource.setCode(source.getValue());
        catalogSource.setDescription(source.getLabel());
        return catalogSource;
    };

    @Override
    public void getCatalogingCatalogSources(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final CatalogSourceCollection container = new CatalogSourceCollection();
                container.setCatalogSources(
                        storageService.getCatalogSources(lang)
                                .stream()
                                .map(toCatalogSource)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingCatalogSources(String lang, CatalogSource entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
