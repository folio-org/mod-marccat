package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.AcquisitionType;
import org.folio.rest.jaxrs.model.AcquisitionTypeCollection;
import org.folio.rest.jaxrs.model.BibliographicLevel;
import org.folio.rest.jaxrs.model.BibliographicLevelCollection;
import org.folio.rest.jaxrs.resource.CatalogingAcquisitionTypesResource;
import org.folio.rest.jaxrs.resource.CatalogingBibliographicLevelsResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Bibliographic Levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class BibliographicLevelsAPI implements CatalogingBibliographicLevelsResource {

    protected final Log logger = new Log(BibliographicLevelsAPI.class);

    private Function<Avp<String>, BibliographicLevel> toBibliographicLevel = source -> {
        final BibliographicLevel bibliographicLevel = new BibliographicLevel();
        bibliographicLevel.setCode(source.getValue());
        bibliographicLevel.setDescription(source.getLabel());
        return bibliographicLevel;
    };

    @Override
    public void getCatalogingBibliographicLevels(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final BibliographicLevelCollection container = new BibliographicLevelCollection();
                container.setBibliographicLevels(
                        storageService.getBibliographicLevels(lang)
                                .stream()
                                .map(toBibliographicLevel)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingBibliographicLevels(String lang, BibliographicLevel entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
