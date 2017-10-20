package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import librisuite.hibernate.USR_ACNT;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.log.PublicMessageCatalog;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.jaxrs.model.Template;
import org.folio.rest.jaxrs.resource.TemplatesResource;
import org.folio.rest.tools.utils.TenantTool;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class TemplateResource implements TemplatesResource{
    private final static Log LOGGER = new Log(TemplateResource.class);
    private final static Configuration HIBERNATE_CONFIGURATION = new Configuration();
    static {
        HIBERNATE_CONFIGURATION.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
        HIBERNATE_CONFIGURATION.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
        HIBERNATE_CONFIGURATION.setProperty("show_sql", "true");
        try {
            HIBERNATE_CONFIGURATION.configure("/hibernate.cfg.xml");
        } catch (final Throwable failure) {
            throw new ExceptionInInitializerError(failure);
        }
    }

    @Override
    public void getTemplates(String query, String orderBy, Order order, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        final ConfigurationsClient configuration =
                new ConfigurationsClient(
                        "127.0.0.1",
                        8085,
                        TenantTool.tenantId(okapiHeaders));

        configuration.getEntries("module==CATALOGING and configName==datasource", 0, 4, "en", response -> {
            response.bodyHandler(body -> {
                final SQLClient client = JDBCClient.createShared(vertxContext.owner(), datasourceConfiguration(body));
                client.getConnection(operation -> {
                    Session session = null;
                    try (final Connection connection = operation.result().unwrap()) {
                        final SessionFactory factory = HIBERNATE_CONFIGURATION.buildSessionFactory();
                        session = factory.openSession(connection);

                        session.get(USR_ACNT.class, "LICIUS");
                        session.get(USR_ACNT.class, 1);

                        asyncResultHandler.handle(Future.succeededFuture(GetTemplatesResponse.withJsonOK(null)));
                    } catch (final SQLException exception) {
                        LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                        asyncResultHandler.handle(
                                Future.succeededFuture(
                                        GetTemplatesResponse.withPlainInternalServerError(
                                                PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                    } catch (final Exception exception) {
                        LOGGER.error(MessageCatalog._00011_NWS_FAILURE, exception);
                        asyncResultHandler.handle(
                                Future.succeededFuture(
                                        GetTemplatesResponse.withPlainInternalServerError(
                                                PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                    } finally {
                        try {
                            if (session != null) session.close();
                        } catch (final HibernateException exception) {
                            LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                        }
                    }
                });
            });
        });
    }

    @Override
    public void postTemplates(String lang, Template entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void getTemplatesByTemplateId(String templateId, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void deleteTemplatesByTemplateId(String templateId, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    @Override
    public void putTemplatesByTemplateId(String templateId, String lang, Template entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }

    private JsonObject datasourceConfiguration(final Buffer value) {
        return new JsonObject(value.toString())
                .getJsonArray("configs")
                .stream()
                .map(JsonObject.class::cast)
                .reduce(
                        new JsonObject(),
                        (r1, r2) -> r1.put(r2.getString("code"), r2.getValue("value")));
    }
}
