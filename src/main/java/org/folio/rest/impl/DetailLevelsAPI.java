package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.DetailLevel;
import org.folio.rest.jaxrs.model.DetailLevelCollection;
import org.folio.rest.jaxrs.resource.CatalogingDetailLevelsResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Detail levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class DetailLevelsAPI implements CatalogingDetailLevelsResource {
    protected final Log logger = new Log(DetailLevelsAPI.class);

    private Function<Avp<String>, DetailLevel> toDetailLevel = source -> {
        final DetailLevel detailLevel = new DetailLevel();
        detailLevel.setCode(source.getValue());
        detailLevel.setDescription(source.getLabel());
        return detailLevel;
    };

    @Override
    public void getCatalogingDetailLevels(final String lang,
                                final Map<String, String> okapiHeaders,
                                final Handler<AsyncResult<Response>> asyncResultHandler,
                                final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final DetailLevelCollection container = new DetailLevelCollection();
                container.setDetailLevels(
                        storageService.getDetailLevels(lang)
                                .stream()
                                .map(toDetailLevel)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingDetailLevels(String lang, DetailLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }
}
