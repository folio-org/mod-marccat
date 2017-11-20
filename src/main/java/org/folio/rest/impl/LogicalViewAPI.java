package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import static librisuite.bean.cataloguing.bibliographic.codelist.CodeListsBean.getDatabaseViewList;

import librisuite.business.codetable.ValueLabelElement;
import librisuite.business.common.DataAccessException;
import librisuite.hibernate.DB_LIST;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import org.folio.cataloging.integration.PieceOfExistingLogicAdapter;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.log.PublicMessageCatalog;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.jaxrs.model.LogicalViewCollection;
import org.folio.rest.jaxrs.model.View;
import org.folio.rest.jaxrs.resource.LogicalViewsResource;
import org.folio.rest.tools.utils.TenantTool;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.F.datasourceConfiguration;
import static org.folio.cataloging.Global.HCONFIGURATION;

public class LogicalViewAPI extends BaseResource implements LogicalViewsResource {
    private final static Log LOGGER = new Log(LogicalViewAPI.class);

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
        doGet((session, future) -> {
            try {
                final LogicalViewCollection container = new LogicalViewCollection();
                container.setViews(
                        logicalViews(lang, session).stream()
                                .map(adapter)
                                .collect(toList()));
                future.complete(container);
            } catch (final Exception exception) {
                LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                future.fail(exception);
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

    public List<ValueLabelElement> logicalViews(final String lang, final Session session) throws DataAccessException {
        return getDatabaseViewList().getDAO().getList(session, DB_LIST.class, Locale.forLanguageTag(lang));
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