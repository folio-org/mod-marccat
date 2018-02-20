package org.folio.cataloging.integration;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.folio.cataloging.Global;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.log.PublicMessageCatalog;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.tools.utils.TenantTool;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

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
    private final static Log LOGGER = new Log(CatalogingHelper.class);

    /**
     * Executes a GET request.
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
        exec(adapter, asyncResultHandler, okapiHeaders, ctx, execution ->
            Response
                .status(HttpStatus.SC_OK)
                .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .entity(execution.result())
                .build());
    }

    interface Valid<T> {
        Optional<T> validate(Function<T, Optional<T>> validator);
    }



    /**
     * Executes a POST request.
     *
     * @param adapter the bridge that carries on the existing logic.
     * @param asyncResultHandler the response handler.
     * @param okapiHeaders the incoming Okapi headers
     * @param ctx the vertx context.
     * @param validator a validator function for the entity associated with this resource.
     * @param id the identifier associated with the just created entity.
     * @throws Exception in case of failure.
     */
    public static void doPost(
            final PieceOfExistingLogicAdapter adapter,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Map<String, String> okapiHeaders,
            final Context ctx,
            final BooleanSupplier validator,
            final Supplier<String> id) throws Exception {
        if (validator.getAsBoolean()) {
            exec(adapter, asyncResultHandler, okapiHeaders, ctx, execution ->
                    Response
                            .status(HttpStatus.SC_CREATED)
                            .header(HttpHeaders.LOCATION, id.get())
                            .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                            .entity(execution.result())
                            .build());
        } else {
            asyncResultHandler.handle(Future.succeededFuture(Response.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).build()));
        }
    }

    /**
     * Executes a PUT request.
     *
     * @param adapter the bridge that carries on the existing logic.
     * @param asyncResultHandler the response handler.
     * @param okapiHeaders the incoming Okapi headers
     * @param ctx the vertx context.
     * @param validator a validator function for the entity associated with this resource.
     * @throws Exception in case of failure.
     */
    public static void doPut(
            final PieceOfExistingLogicAdapter adapter,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Map<String, String> okapiHeaders,
            final Context ctx,
            final BooleanSupplier validator) throws Exception {
        if (validator.getAsBoolean()) {
            exec(adapter, asyncResultHandler, okapiHeaders, ctx, execution -> Response.status(HttpStatus.SC_NO_CONTENT).build());
        } else {
            asyncResultHandler.handle(Future.succeededFuture(Response.status(HttpStatus.SC_BAD_REQUEST).build()));
        }
    }

    /**
     * Executes a DELETE request.
     *
     * @param adapter the bridge that carries on the existing logic.
     * @param asyncResultHandler the response handler.
     * @param okapiHeaders the incoming Okapi headers
     * @param ctx the vertx context.
     * @throws Exception in case of failure.
     */
    public static void doDelete(
            final PieceOfExistingLogicAdapter adapter,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Map<String, String> okapiHeaders,
            final Context ctx) throws Exception {
        exec(adapter, asyncResultHandler, okapiHeaders, ctx, execution -> Response.status(HttpStatus.SC_NO_CONTENT).build());
    }

    /**
     * Provides a unified approach (within the cataloging module) for wrapping an existing blocking flow.
     *
     * @param adapter the bridge that carries on the existing logic.
     * @param resultHandler the response handler.
     * @param okapiHeaders the incoming Okapi headers
     * @param ctx the vertx context.
     * @throws Exception in case of failure.
     */
    private static void exec(
            final PieceOfExistingLogicAdapter adapter,
            final Handler<AsyncResult<Response>> resultHandler,
            final Map<String, String> okapiHeaders,
            final Context ctx,
            final Function<AsyncResult<Object>, Response> responseFactory) throws Exception {

        final ConfigurationsClient configuration =
                new ConfigurationsClient(
                        System.getProperty("config.server.listen.address", "192.168.0.158"),
                        Integer.parseInt(System.getProperty("config.server.listen.port", "8085")),
                        TenantTool.tenantId(okapiHeaders));

        ctx.put(Global.CONFIGURATION_CLIENT, configuration);

        configuration.getEntries("module==CATALOGING and configName==datasource", 0, 4, "en", response ->
            response.bodyHandler(body -> {
                final SQLClient client = JDBCClient.createShared(ctx.owner(), datasourceConfiguration(body));
                client.getConnection(operation ->
                    ctx.executeBlocking(
                            future -> {
                                try (final Connection connection = operation.result().unwrap();
                                    final StorageService service =
                                            new StorageService(
                                                    HCONFIGURATION.buildSessionFactory().openSession(connection),
                                                    configuration)) {
                                    adapter.execute(service, future);
                                } catch (final SQLException exception) {
                                    LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                                    resultHandler.handle(
                                            Future.succeededFuture(
                                                    internalServerError(
                                                            PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                                } catch (final Throwable exception) {
                                    LOGGER.error(MessageCatalog._00011_NWS_FAILURE, exception);
                                    resultHandler.handle(
                                            Future.succeededFuture(
                                                    internalServerError(
                                                            PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                                }
                            },
                            false,
                            execution ->
                                resultHandler.handle(
                                        Future.succeededFuture(
                                                execution.succeeded()
                                                        ? responseFactory.apply(execution)
                                                        : internalServerError(PublicMessageCatalog.INTERNAL_SERVER_ERROR)))));
            }));
    }

    /**
     * Generates a 500 (Internal Server Error) HTTP response.
     *
     * @param message the explanation message.
     * @return a 500 (Internal Server Error) HTTP response.
     */
    private static Response internalServerError(final String message) {
        return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(message).build();
    }
}