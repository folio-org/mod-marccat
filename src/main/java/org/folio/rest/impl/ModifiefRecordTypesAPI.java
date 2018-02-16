package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.ModifiedRecordType;
import org.folio.rest.jaxrs.model.ModifiedRecordTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingModifiedRecordTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Modified Record Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class ModifiefRecordTypesAPI implements CatalogingModifiedRecordTypesResource {

    protected final Log logger = new Log(ModifiefRecordTypesAPI.class);

    private Function<Avp<String>, ModifiedRecordType> toModifiedRecordType = source -> {
        final ModifiedRecordType modifiedRecordType = new ModifiedRecordType();
        modifiedRecordType.setCode(source.getValue());
        modifiedRecordType.setDescription(source.getLabel());
        return modifiedRecordType;
    };

    @Override
    public void getCatalogingModifiedRecordTypes(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final ModifiedRecordTypeCollection container = new ModifiedRecordTypeCollection();
                container.setModifiedRecordTypes(
                        storageService.getModifiedRecordTypes(lang)
                                .stream()
                                .map(toModifiedRecordType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingModifiedRecordTypes(String lang, ModifiedRecordType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
