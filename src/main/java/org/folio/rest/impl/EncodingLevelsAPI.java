package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.EncodingLevel;
import org.folio.rest.jaxrs.model.EncodingLevelCollection;
import org.folio.rest.jaxrs.resource.CatalogingEncodingLevelsResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Encoding Levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class EncodingLevelsAPI implements CatalogingEncodingLevelsResource {

    protected final Log logger = new Log(EncodingLevelsAPI.class);

    private Function<Avp<String>, EncodingLevel> toEncodingLevel = source -> {
        final EncodingLevel encodingLevel = new EncodingLevel();
        encodingLevel.setCode(source.getValue());
        encodingLevel.setDescription(source.getLabel());
        return encodingLevel;
    };

    @Override
    public void getCatalogingEncodingLevels(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final EncodingLevelCollection container = new EncodingLevelCollection();
                container.setEncodingLevels(
                        storageService.getEncodingLevels(lang)
                                .stream()
                                .map(toEncodingLevel)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingEncodingLevels(String lang, EncodingLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
