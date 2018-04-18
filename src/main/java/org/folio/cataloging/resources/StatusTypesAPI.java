package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.StatusType;
import org.folio.rest.jaxrs.model.StatusTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingStatusTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Status type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
public class StatusTypesAPI implements CatalogingStatusTypesResource {

    protected final Log logger = new Log(StatusTypesAPI.class);

    private Function<Avp<String>, StatusType> toStatusType = source -> {
        final StatusType statusType = new StatusType();
        statusType.setCode(source.getValue());
        statusType.setDescription(source.getLabel());
        return statusType;
    };

    @Override
    public void getCatalogingStatusTypes(final String lang,
                               final Map<String, String> okapiHeaders,
                               final Handler<AsyncResult<Response>> asyncResultHandler,
                               final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final StatusTypeCollection container = new StatusTypeCollection();
                container.setStatusTypes(
                        storageService.getStatusTypes(lang)
                                .stream()
                                .map(toStatusType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingStatusTypes(String lang, StatusType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
