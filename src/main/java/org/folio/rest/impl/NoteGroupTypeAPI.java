package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.NoteGroupType;
import org.folio.rest.jaxrs.model.NoteGroupTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingNoteGroupTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Note Group Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
*/
public class NoteGroupTypeAPI implements CatalogingNoteGroupTypesResource {

    protected final Log logger = new Log(NoteTypesAPI.class);

    private Function<Avp<String>, NoteGroupType> toNoteGroupType = source -> {
        final NoteGroupType noteGroupType = new NoteGroupType();
        noteGroupType.setCode(Integer.parseInt(source.getValue()));
        noteGroupType.setDescription(source.getLabel());
        return noteGroupType;
    };

    @Override
    public void getCatalogingNoteGroupTypes(final String lang,
                                            final Map<String, String> okapiHeaders,
                                            final Handler<AsyncResult<Response>> asyncResultHandler,
                                            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final NoteGroupTypeCollection container = new NoteGroupTypeCollection();
                container.setNoteGroupTypes(storageService.getNoteGroupTypeList(lang)
                        .stream()
                        .map(toNoteGroupType)
                        .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingNoteGroupTypes(String lang, NoteGroupType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
