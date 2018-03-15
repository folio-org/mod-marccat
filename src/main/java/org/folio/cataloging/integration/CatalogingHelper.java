package org.folio.cataloging.integration;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.log.PublicMessageCatalog;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.tools.utils.TenantTool;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static org.folio.cataloging.F.safe;
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
    private final static Properties DEFAULT_VALUES = new Properties();
    static {
        try {
            DEFAULT_VALUES.load(CatalogingHelper.class.getResourceAsStream("defaults.properties"));
        } catch (final Throwable exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    final static String BASE_CQUERY = "module==CATALOGING and ( configName==datasource";

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
            final Context ctx,
            final String ... configurationSets) throws Exception {
        exec(adapter, asyncResultHandler, okapiHeaders, ctx, execution ->
            Response
                .status(HttpStatus.SC_OK)
                .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .entity(execution.result())
                .build(), configurationSets);
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
     * @param responseFactory the mapper between the asynch handler and the outgoing response.
     * @param configurationSets the configurationSets required by the current service.
     * @throws Exception in case of failure.
     */
    private static void exec(
            final PieceOfExistingLogicAdapter adapter,
            final Handler<AsyncResult<Response>> resultHandler,
            final Map<String, String> okapiHeaders,
            final Context ctx,
            final Function<AsyncResult<Object>, Response> responseFactory,
            final String ... configurationSets) throws Exception {

        final ConfigurationsClient configuration =
                new ConfigurationsClient(
                        System.getProperty("config.server.listen.address", "192.168.0.158"),
                        Integer.parseInt(System.getProperty("config.server.listen.port", "8085")),
                        TenantTool.tenantId(okapiHeaders));

        configuration.getEntries(cQuery(configurationSets), 0, 100, "en", response ->
            response.bodyHandler(body -> {
                try {
                    final SQLClient client = JDBCClient.createShared(ctx.owner(), datasourceConfiguration(body));
                    client.getConnection(operation ->
                            ctx.executeBlocking(
                                    future -> {
                                        try (final Connection connection = operation.result().unwrap();
                                             final StorageService service =
                                                     new StorageService(
                                                             HCONFIGURATION.buildSessionFactory().openSession(connection),
                                                             ctx)) {
                                            adapter.execute(service, configuration(body), future);
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
                } catch (final Throwable throwable) {
                    LOGGER.error(MessageCatalog._00011_NWS_FAILURE, throwable);
                    resultHandler.handle(
                            Future.succeededFuture(
                                    internalServerError(
                                            PublicMessageCatalog.INTERNAL_SERVER_ERROR)));
                }
            }));
    }

    /**
     * Creates a dedicated configuration for the current service.
     *
     * @param body the mod-configuration response.
     * @return a dedicated configuration for the current service.
     */
    private static Map<String,String> configuration(final Buffer body) {
        return new JsonObject(body.toString())
                .getJsonArray("configs")
                .stream()
                .map(JsonObject.class::cast)
                .filter(obj -> !"datasource".equals(obj.getString("configName", "")))
                .map(obj -> new AbstractMap.SimpleEntry<>(
                        obj.getString("code"),
                        obj.getString("value", DEFAULT_VALUES.getProperty(obj.getString("code")))))
                .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
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

    /**
     * Returns the selection criteria that will be used by the current service for gathering the required configuration.
     *
     * @param configurationsSets the configuration groups.
     * @return the selection criteria that will be used by the current service for gathering the required configuration.
     */
    static String cQuery(final String ... configurationsSets) {
        final String [] values = safe(configurationsSets);
        return BASE_CQUERY +
                stream(values).filter(Objects::nonNull).collect(joining(" or ", values.length != 0 ? " or " : "", "")) +
                ")";
    }

    /**
     * Retrieves the datasource configuration from the given buffer.
     * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
     *
     * @param value the configuration as it comes from the mod-configuration module.
     * @return the datasource configuration used within this module.
     */
    private static JsonObject datasourceConfiguration(final Buffer value) {
        return new JsonObject(value.toString())
                .getJsonArray("configs")
                .stream()
                .map(JsonObject.class::cast)
                .filter(obj -> "datasource".equals(obj.getString("configName", "")))
                .reduce(
                        new JsonObject(),
                        (r1, r2) -> r1.put(r2.getString("code"), r2.getValue("value")));
    }
}