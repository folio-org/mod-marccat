package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.jdbc.impl.JDBCClientImpl;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import org.folio.rest.jaxrs.model.Template;
import org.folio.rest.jaxrs.resource.TemplatesResource;

import javax.ws.rs.core.Response;
import java.util.Map;

public class TemplateResource implements TemplatesResource{
    @Override
    public void getTemplates(String query, String orderBy, Order order, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        final JsonObject config =
                new JsonObject()
                    .put("url", "jdbc:postgresql://192.168.0.158:5432/olidb_sv1")
                    .put("port", 5432)
                    .put("user", "amicus")
                    .put("password", "oracle")
                    .put("database", "olidb_sv1");


        JDBCClient client = JDBCClient.createShared(vertxContext.owner(), config);

        client.getConnection(operation -> {
            if (operation.succeeded()) {

                SQLConnection connection = operation.result();

                connection.query("SELECT BIB_ITM_NBR FROM BIB_ITM LIMIT 10", res2 -> {
                    if (res2.succeeded()) {

                        ResultSet rs = res2.result();
                        rs.getResults().forEach(System.out::println);
                        asyncResultHandler.handle(Future.succeededFuture(GetTemplatesResponse.withJsonOK(null)));
                    }
                });
            } else {
                // Failed to get connection - deal with it
            }
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
}
