package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.RecordType;
import org.folio.rest.jaxrs.model.RecordTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingRecordTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Record Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class RecordTypesAPI implements CatalogingRecordTypesResource {

    protected final Log logger = new Log(RecordTypesAPI.class);

    private Function<Avp<String>, RecordType> toRecordType = source -> {
        final RecordType recordType = new RecordType();
        recordType.setCode(source.getValue());
        recordType.setDescription(source.getLabel());
        return recordType;
    };

    @Override
    public void getCatalogingRecordTypes(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final RecordTypeCollection container = new RecordTypeCollection();
                container.setRecordTypes(
                        storageService.getRecordTypes(lang)
                                .stream()
                                .map(toRecordType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingRecordTypes(String lang, RecordType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
