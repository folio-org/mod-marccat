package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.AcquisitionType;
import org.folio.rest.jaxrs.model.AcquisitionTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingAcquisitionTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Acquisition Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class AcquisitionTypesAPI implements CatalogingAcquisitionTypesResource {

    protected final Log logger = new Log(AcquisitionTypesAPI.class);

    private Function<Avp<String>, AcquisitionType> toAcquisitionType = source -> {
        final AcquisitionType acquisitionType = new AcquisitionType();
        //TODO: handle type Integer for stringValue element or null stringValue in Integer.parseInt
        acquisitionType.setCode(Integer.parseInt(source.getValue()));
        acquisitionType.setDescription(source.getLabel());
        return acquisitionType;
    };

    @Override
    public void getCatalogingAcquisitionTypes(final String lang,
                                    final Map<String, String> okapiHeaders,
                                    final Handler<AsyncResult<Response>> asyncResultHandler,
                                    final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final AcquisitionTypeCollection container = new AcquisitionTypeCollection();
                container.setAcquisitionTypes(
                        storageService.getAcquisitionTypes(lang)
                                .stream()
                                .map(toAcquisitionType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingAcquisitionTypes(String lang,
                                     AcquisitionType entity,
                                     Map<String, String> okapiHeaders,
                                     Handler<AsyncResult<Response>> asyncResultHandler,
                                     Context vertxContext) throws Exception {
        throw new IllegalArgumentException();

    }
}
