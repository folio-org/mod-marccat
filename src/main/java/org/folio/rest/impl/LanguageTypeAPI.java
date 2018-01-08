package org.folio.rest.impl;


import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.LanguageType;
import org.folio.rest.jaxrs.model.LanguageTypeCollection;
import org.folio.rest.jaxrs.resource.LanguageTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Language Types RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */

public class LanguageTypeAPI implements LanguageTypesResource {

    protected final Log logger = new Log(LanguageTypeAPI.class);

    private Function<ValueLabelElement<String>, LanguageType> toLanguageType = source -> {
        final LanguageType languageType = new LanguageType();
        languageType.setCode(source.getValue());
        languageType.setDescription(source.getLabel());
        return languageType;
    };

    @Override
      public void getLanguageTypes(final String lang,
                                      final Map<String, String> okapiHeaders,
                                      final Handler<AsyncResult<Response>> asyncResultHandler,
                                      final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final LanguageTypeCollection container = new LanguageTypeCollection();
                container.setLanguageTypes(
                        storageService.getLanguageTypes(lang)
                                .stream()
                                .map(toLanguageType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }


    @Override
    public void postLanguageTypes(String lang, LanguageType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
