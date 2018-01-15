package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.ShelfListType;
import org.folio.rest.jaxrs.model.ShelfListTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingShelfListTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * ShelfList type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class ShelfListTypesAPI implements CatalogingShelfListTypesResource {

    protected final Log logger = new Log(ShelfListTypesAPI.class);

    private Function<Avp<String>, ShelfListType> toShelfListType = source -> {
        final ShelfListType shelfListType = new ShelfListType();
        shelfListType.setCode(source.getValue());
        shelfListType.setDescription(source.getLabel());
        return shelfListType;
    };

    @Override
    public void getCatalogingShelfListTypes(final String lang,
                                  final Map<String, String> okapiHeaders,
                                  final Handler<AsyncResult<Response>> asyncResultHandler,
                                  final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final ShelfListTypeCollection container = new ShelfListTypeCollection();
                container.setShelfListTypes(
                        storageService.getShelfListTypes(lang)
                                .stream()
                                .map(toShelfListType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingShelfListTypes(String lang, ShelfListType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
