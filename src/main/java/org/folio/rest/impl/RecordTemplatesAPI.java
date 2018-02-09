package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.RecordTemplate;
import org.folio.rest.jaxrs.model.RecordTemplateCollection;
import org.folio.rest.jaxrs.resource.CatalogingRecordTemplatesResource;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * BIB / AUT Record templates API.
 *
 * @since 1.0
 * @author agazzarini
 */
public class RecordTemplatesAPI implements CatalogingRecordTemplatesResource {
    protected final Log logger = new Log(RecordTemplate.class);

    private Function<Avp<Integer>, RecordTemplate> toRecordTemplate = avp -> {
        final RecordTemplate template = new RecordTemplate();
        template.setId(avp.getValue());
        template.setName(avp.getLabel());
        return template;

    };

    @Override
    public void getCatalogingRecordTemplates(
            final CatalogingRecordTemplatesResource.Type type,
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final List<Avp<Integer>> templates =
                        type == Type.A
                            ? storageService.getAuthorityRecordTemplates()
                            : storageService.getBibliographicRecordTemplates();

                final RecordTemplateCollection collection = new RecordTemplateCollection();
                collection.setRecordTemplates(templates.stream().map(toRecordTemplate).collect(toList()));
                return collection;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void getCatalogingRecordTemplatesById(String id, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void postCatalogingRecordTemplates(String lang, RecordTemplate entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void deleteCatalogingRecordTemplatesById(String id, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void putCatalogingRecordTemplatesById(String id, String lang, RecordTemplate entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }
}
