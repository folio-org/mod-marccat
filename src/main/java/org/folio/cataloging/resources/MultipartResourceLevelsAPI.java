package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.MultipartResourceLevel;
import org.folio.rest.jaxrs.model.MultipartResourceLevelCollection;
import org.folio.rest.jaxrs.resource.CatalogingMultipartResourceLevelsResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Multipart Resource levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class MultipartResourceLevelsAPI implements CatalogingMultipartResourceLevelsResource {

    protected final Log logger = new Log(MultipartResourceLevelsAPI.class);

    private Function<Avp<String>, MultipartResourceLevel> toMultipartResourceLevel = source -> {
        final MultipartResourceLevel multipartResourceLevel = new MultipartResourceLevel();
        multipartResourceLevel.setCode(source.getValue());
        multipartResourceLevel.setDescription(source.getLabel());
        return multipartResourceLevel;
    };

    @Override
    public void getCatalogingMultipartResourceLevels(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final MultipartResourceLevelCollection container = new MultipartResourceLevelCollection();
                container.setMultipartResourceLevels(
                        storageService.getMultipartResourceLevels(lang)
                                .stream()
                                .map(toMultipartResourceLevel)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingMultipartResourceLevels(String lang, MultipartResourceLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
