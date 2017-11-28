package org.folio.cataloging.integration;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import net.sf.hibernate.Session;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.log.PublicMessageCatalog;
import org.folio.rest.client.ConfigurationsClient;
// import org.folio.rest.jaxrs.model.LogicalViewCollection;
import org.folio.rest.tools.utils.TenantTool;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.folio.cataloging.F.datasourceConfiguration;
import static org.folio.cataloging.Global.HCONFIGURATION;

/**
 * Helper functions used within the cataloging module.
 * Specifically, this class was originally thought as a supertype layer of each resource implementor; later, it has
 * been converted in this way (i.e. a collection of static methods) because https://issues.folio.org/browse/RMB-95
 *
 * @author agazzarini
 * @since 1.0
 */
public abstract class CatalogingHelper {
    protected final static Log LOGGER = new Log(CatalogingHelper.class);

    /**
     * Provides a unified approach (within the cataloging module) for wrapping an existing blocking flow.
     *
     * @param adapter the bridge that carries on the existing logic.
     * @param asyncResultHandler the response handler.
     * @param okapiHeaders the incoming Okapi headers
     * @param ctx the vertx context.
     * @throws Exception in case of failure.
     */
    public static void doGet(
            final PieceOfExistingLogicAdapter adapter,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Map<String, String> okapiHeaders,
            final Context ctx) throws Exception {
        final ConfigurationsClient configuration =
                new ConfigurationsClient(
                        System.getProperty("config.server.listen.address", "192.168.0.158"),
                        Integer.parseInt(System.getProperty("config.server.listen.port", "8085")),
                        TenantTool.tenantId(okapiHeaders));

        configuration.getEntries("module==CATALOGING and configName==datasource", 0, 4, "en", response -> {
            response.bodyHandler(body -> {
                final SQLClient client = JDBCClient.createShared(ctx.owner(), datasourceConfiguration(body));
                client.getConnection(operation -> {
                    ctx.executeBlocking(
                            future -> {
                                try (final Connection connection = operation.result().unwrap();
                                    final StorageService service =
                                            new StorageService(
                                                    HCONFIGURATION.buildSessionFactory().openSession(connection))) {

                                    adapter.execute(service, future);
                                } catch (final SQLException exception) {
                                    LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                                    asyncResultHandler.handle(
                                            Future.succeededFuture(
                                                    internalServerError(
                                                            PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                                } catch (final Exception exception) {
                                    LOGGER.error(MessageCatalog._00011_NWS_FAILURE, exception);
                                    asyncResultHandler.handle(
                                            Future.succeededFuture(
                                                    internalServerError(
                                                            PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                                }
                            },
                            false,
                            execution -> {
                                if (execution.succeeded()) {
                                    asyncResultHandler.handle(
                                            Future.succeededFuture(
                                                    Response
                                                            .status(200)
                                                            .header("Content-Type", "application/json")
                                                            .entity(execution.result())
                                                            .build()));
                                } else {
                                    asyncResultHandler.handle(
                                            Future.succeededFuture(
                                                    internalServerError(
                                                            PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                                }});
                });
            });
        });
    }


    public static Response internalServerError(final String message) {
        return Response.status(500)
                .header("Content-Type", "application/json")
                .entity(message)
                .build();
    }
}