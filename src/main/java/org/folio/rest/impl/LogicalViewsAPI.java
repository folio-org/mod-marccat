package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import static org.folio.cataloging.bean.cataloguing.bibliographic.codelist.CodeListsBean.getDatabaseViewList;

import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.DB_LIST;
import net.sf.hibernate.Session;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.log.PublicMessageCatalog;
import org.folio.rest.jaxrs.model.LogicalViewCollection;
import org.folio.rest.jaxrs.model.View;
import org.folio.rest.jaxrs.resource.LogicalViewsResource;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;
import static org.folio.cataloging.integration.CatalogingHelper.internalServerError;

/**
 * Logical views RESTful APIs.
 *
 * @author agazzarini
 * @since 1.0
 */
public class LogicalViewsAPI implements LogicalViewsResource {
    protected final Log logger = new Log(LogicalViewsAPI.class);

    // This is the adapter that converts existing value objects (logical views in this case)
    // in OKAPI resources.
    private Function<ValueLabelElement, View> adapter = source -> {
        final View logicalView = new View();
        logicalView.setCode(source.getValue());
        logicalView.setLongDescription(source.getLabel());
        return logicalView;
    };

    @Override
    public void getLogicalViews(
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> resultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final LogicalViewCollection container = new LogicalViewCollection();
                container.setViews(
                        storageService.getLogicalViews(lang)
                                .stream()
                                .map(adapter)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, operation -> {
            if (operation.succeeded()) {
                resultHandler.handle(
                        Future.succeededFuture(
                                GetLogicalViewsResponse.withJsonOK(operation.result())));
            } else {
                resultHandler.handle(
                        Future.succeededFuture(
                                internalServerError(
                                        PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
            }
        }, resultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postLogicalViews(String lang, View entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void getLogicalViewsByViewId(String viewId, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void deleteLogicalViewsByViewId(String viewId, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putLogicalViewsByViewId(String viewId, String lang, View entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}