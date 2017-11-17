package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import static librisuite.bean.cataloguing.bibliographic.codelist.CodeListsBean.getDatabaseViewList;
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
import java.util.Locale;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.F.datasourceConfiguration;
import static org.folio.cataloging.Global.HCONFIGURATION;

public class LogicalViewAPI implements LogicalViewsResource {
    private final static Log LOGGER = new Log(LogicalViewAPI.class);

    @Override
    public void getLogicalViews(String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        doGet( (session, future) -> {
            try {
                final LogicalViewCollection container = new LogicalViewCollection();
                container.setViews(
                        getDatabaseViewList().getDAO().getList(session, DB_LIST.class, Locale.forLanguageTag(lang)).stream()
                                .map(nvp -> {
                                    final View logicalView = new View();
                                    logicalView.setCode(nvp.getValue());
                                    logicalView.setLongDescription(nvp.getLabel());
                                    return logicalView;
                                })
                                .collect(toList()));
                future.complete(container);
            } catch (final Exception exception) {
                LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                future.fail(exception);
            }
        }, operation -> {
            if (operation.succeeded()) {
                asyncResultHandler.handle(
                        Future.succeededFuture(
                                GetLogicalViewsResponse.withJsonOK(operation.result())));
            } else {
                asyncResultHandler.handle(
                        Future.succeededFuture(
                                GetLogicalViewsResponse.withPlainInternalServerError(
                                        PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
            }
        },asyncResultHandler, okapiHeaders, vertxContext);
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

    }

    public void doGet(
        final PieceOfExistingLogicAdapter adapter,
        final Handler<AsyncResult<LogicalViewCollection>> resultHandler,
        final Handler<AsyncResult<Response>> asyncResultHandler,
        final Map<String, String> okapiHeaders,
        final Context ctx) throws Exception {
        final ConfigurationsClient configuration =
                new ConfigurationsClient(
                        "127.0.0.1",
                        8085,
                        TenantTool.tenantId(okapiHeaders));

        configuration.getEntries("module==CATALOGING and configName==datasource", 0, 4, "en", response -> {
            response.bodyHandler(body -> {
                final SQLClient client = JDBCClient.createShared(ctx.owner(), datasourceConfiguration(body));
                client.getConnection(operation -> {
                        ctx.executeBlocking(
                                future -> {
                                    Session session = null;
                                    try (final Connection connection = operation.result().unwrap()) {
                                        final SessionFactory factory = HCONFIGURATION.buildSessionFactory();
                                        session = factory.openSession(connection);

                                        adapter.execute(session, future);

                                    } catch (final SQLException exception) {
                                        LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                                        asyncResultHandler.handle(
                                                Future.succeededFuture(
                                                        GetLogicalViewsResponse.withPlainInternalServerError(
                                                                PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                                    } catch (final Exception exception) {
                                        LOGGER.error(MessageCatalog._00011_NWS_FAILURE, exception);
                                        asyncResultHandler.handle(
                                                Future.succeededFuture(
                                                        GetLogicalViewsResponse.withPlainInternalServerError(
                                                                PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                                    } finally {
                                        if (session != null) {
                                            try {
                                                session.close();
                                            } catch (final HibernateException ignore) {
                                                // Ignore
                                            }
                                        }
                                    }
                                },
                                false,
                                resultHandler);
                });
            });
        });
    }
}
