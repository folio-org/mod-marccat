package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import librisuite.hibernate.USR_ACNT;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.jaxrs.model.Template;
import org.folio.rest.jaxrs.resource.TemplatesResource;
import org.folio.rest.tools.utils.TenantTool;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.Map;

public class TemplateResource implements TemplatesResource{
    @Override
    public void getTemplates(String query, String orderBy, Order order, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

        final ConfigurationsClient configuration =
                new ConfigurationsClient(
                        "127.0.0.1",
                        8085,
                        TenantTool.tenantId(okapiHeaders));


        final SQLClient client = JDBCClient.createShared(
                vertxContext.owner(),
                (JsonObject) vertxContext.owner()
                    .sharedData()
                    .getLocalMap("KEY")
                    .computeIfAbsent(
                            TenantTool.tenantId(okapiHeaders),
                            t -> new JsonObject()
                                    .put("url", "jdbc:postgresql://192.168.0.158:5432/olidb_sv1")
                                    .put("port", 5432)
                                    .put("user", "amicus")
                                    .put("password", "oracle")
                                    .put("database", "olidb_sv1")));

        client.getConnection(operation -> {
            try (final Connection connection = operation.result().unwrap()) {
                final Configuration config = new Configuration();
                config.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
                config.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
                config.setProperty("show_sql", "true");
                config.configure("/hibernate.cfg.xml");

                SessionFactory factory = config.buildSessionFactory();
                final Session session = factory.openSession(connection);

                final USR_ACNT user = (USR_ACNT) session.get(USR_ACNT.class, "LICIUS");

                System.out.println(user.getBranchLibrary());

                session.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        asyncResultHandler.handle(Future.succeededFuture(GetTemplatesResponse.withJsonOK(null)));
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
}
