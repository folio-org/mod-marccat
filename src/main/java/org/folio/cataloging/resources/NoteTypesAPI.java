package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.NoteType;
import org.folio.rest.jaxrs.model.NoteTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingNoteTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Note Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class NoteTypesAPI implements CatalogingNoteTypesResource {
    protected final Log logger = new Log(NoteTypesAPI.class);

    private Function<Avp<String>, NoteType> toNoteType = source -> {
        final NoteType noteType = new NoteType();
        noteType.setCode(Integer.parseInt(source.getValue()));
        noteType.setDescription(source.getLabel());
        return noteType;
    };

    @Override
    public void getCatalogingNoteTypes(final String noteGroupType,
                                        final String lang,
                                        final Map<String, String> okapiHeaders,
                                        final Handler<AsyncResult<Response>> asyncResultHandler,
                                        final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final NoteTypeCollection container = new NoteTypeCollection();
                container.setNoteTypes(storageService.getNoteTypesByGroupTypeCode(noteGroupType, lang)
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
    public void postCatalogingNoteTypes(String lang, NoteType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

}
