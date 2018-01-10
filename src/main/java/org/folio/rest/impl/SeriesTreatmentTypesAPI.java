package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.SeriesTreatmentType;
import org.folio.rest.jaxrs.model.SeriesTreatmentTypeCollection;
import org.folio.rest.jaxrs.resource.SeriesTreatmentTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Series treatment type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
public class SeriesTreatmentTypesAPI implements SeriesTreatmentTypesResource {

    protected final Log logger = new Log(SeriesTreatmentTypesAPI.class);

    private Function<Avp<String>, SeriesTreatmentType> toSeriesTreatmentType = source -> {
        final SeriesTreatmentType seriesTreatmentType = new SeriesTreatmentType();
        seriesTreatmentType.setCode(source.getValue());
        seriesTreatmentType.setDescription(source.getLabel());
        return seriesTreatmentType;
    };

    @Override
    public void getSeriesTreatmentTypes(final String lang,
                                        final Map<String, String> okapiHeaders,
                                        final Handler<AsyncResult<Response>> asyncResultHandler,
                                        final Context vertxContext) throws Exception {

        doGet((storageService, future) -> {
            try {
                final SeriesTreatmentTypeCollection container = new SeriesTreatmentTypeCollection();
                container.setSeriesTreatmentTypes(
                        storageService.getSeriesTreatmentTypes(lang)
                                .stream()
                                .map(toSeriesTreatmentType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postSeriesTreatmentTypes(String lang, SeriesTreatmentType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
