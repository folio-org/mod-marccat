package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.DescriptiveCatalogForm;
import org.folio.rest.jaxrs.model.DescriptiveCatalogFormCollection;
import org.folio.rest.jaxrs.resource.CatalogingDescriptiveCatalogFormsResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Descriptive Catalog Forms RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */

public class DescriptiveCatalogFormsAPI implements CatalogingDescriptiveCatalogFormsResource {

    protected final Log logger = new Log(DescriptiveCatalogFormsAPI.class);

    private Function<Avp<String>, DescriptiveCatalogForm> toDescriptiveCatalogForm = source -> {
        final DescriptiveCatalogForm descriptiveCatalogForm = new DescriptiveCatalogForm();
        descriptiveCatalogForm.setCode(source.getValue());
        descriptiveCatalogForm.setDescription(source.getLabel());
        return descriptiveCatalogForm;
    };

    @Override
    public void getCatalogingDescriptiveCatalogForms(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final DescriptiveCatalogFormCollection container = new DescriptiveCatalogFormCollection();
                container.setDescriptiveCatalogForms(
                        storageService.getDescriptiveCatalogForms(lang)
                                .stream()
                                .map(toDescriptiveCatalogForm)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingDescriptiveCatalogForms(String lang, DescriptiveCatalogForm entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
