package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.RecordStatusType;
import org.folio.rest.jaxrs.model.RecordStatusTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingRecordStatusTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Record Status Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class RecordStatusTypesAPI implements CatalogingRecordStatusTypesResource {

    protected final Log logger = new Log(RecordStatusTypesAPI.class);

    private Function<Avp<String>, RecordStatusType> toRecordStatusType = source -> {
        final RecordStatusType recordStatusType = new RecordStatusType();
        recordStatusType.setCode(source.getValue());
        recordStatusType.setDescription(source.getLabel());
        return recordStatusType;
    };

    @Override
    public void getCatalogingRecordStatusTypes(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final RecordStatusTypeCollection container = new RecordStatusTypeCollection();
                container.setRecordStatusTypes(
                        storageService.getRecordStatusTypes(lang)
                        .stream()
                        .map(toRecordStatusType)
                        .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingRecordStatusTypes(String lang, RecordStatusType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }
}
