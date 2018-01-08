package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.NoteType;
import org.folio.rest.jaxrs.model.NoteTypeCollection;
import org.folio.rest.jaxrs.resource.NoteTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Note Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class NoteTypesAPI implements NoteTypesResource {
    protected final Log logger = new Log(NoteTypesAPI.class);

    // This is the adapter that converts existing value objects (logical views in this case)
    // in OKAPI resources.
    private Function<Avp<String>, NoteType> toNoteType = source -> {
        final NoteType noteType = new NoteType();
        //TODO: handle type Integer for value element or null value in Integer.parseInt
        noteType.setCode(Integer.parseInt(source.getValue()));
        noteType.setDescription(source.getLabel());
        return noteType;
    };

    @Override
    public void getNoteTypes(final String lang,
                             final Map<String, String> okapiHeaders,
                             final Handler<AsyncResult<Response>> asyncResultHandler,
                             final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final NoteTypeCollection container = new NoteTypeCollection();
                container.setNoteTypes(
                        storageService.getNoteTypes(lang)
                                .stream()
                                .map(toNoteType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postNoteTypes(String lang, NoteType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

}
