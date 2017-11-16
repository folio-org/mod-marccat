package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
//import librisuite.hibernate.Template;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import org.folio.cataloging.integration.PieceOfExistingLogicAdapter;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.log.PublicMessageCatalog;
import org.folio.rest.client.ConfigurationsClient;
import org.folio.rest.jaxrs.model.Template;
import org.folio.rest.jaxrs.resource.TemplatesResource;
import org.folio.rest.tools.utils.TenantTool;
import static org.folio.cataloging.F.datasourceConfiguration;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Sample reference resource.
 * Although a template is a real cataloging resources, this first try here, is
 * just to understand how we can combine blocking (i.e. existing code) and no-blocking (OKAPI, Vertx & co).
 *
 * If things will be validated, we will use this class as a reference for implementing the required use cases.
 *
 * TODO: for each incoming request, a new instance of this class is created; what about immutable resources lifecycle (i.e. clients, proxies)?
 *
 * @author agazzarini
 * @since 1.0
 */
public class TemplateResource implements TemplatesResource{

    private final static Log LOGGER = new Log(TemplateResource.class);

    // Looking at the Hibernate code I'm quite confident we can statically manage the Hib Configuration.
    private final static Configuration HIBERNATE_CONFIGURATION = new Configuration();
    static {
        HIBERNATE_CONFIGURATION.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
        HIBERNATE_CONFIGURATION.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
        try {
            HIBERNATE_CONFIGURATION.configure("/hibernate.cfg.xml");
        } catch (final Throwable failure) {
            throw new ExceptionInInitializerError(failure);
        }
    }

    /**
     * This is an example of a GET method.
     * As you can see, most of Vertx controller code is hidden by the internal doGet method. The developer has to provide:
     *
     * <ul>
     *  <li>a {@link PieceOfExistingLogicAdapter} which is supposed to be the adapter (i.e the bridge) between the two worlds (blocking and no-blocking).</li>
     *  <li>a result handler, which is typed with a given kind of object (USR_ACNT in this example) which in turn depends by the current operation.</li>
     *  <li>the response handler, used for communicating back a response to the caller</li>
     *  <li>the okapi HTTP headers</li>
     *  <li>the Vertx Context</li>
     * </ul>
     *
     * The last three params come from the RMB generated code. The first two are up to the developer.

    public void getTemplate(String query, String orderBy, Order order, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        doGet( (session, future) -> {
            // This is the adapter core, where we need to call the existing logic and provide a valuable result in output
            try {
                final Template template = (Template) session.get(Template.class, "LICIUS");
                future.complete(template);
            } catch (Exception exception) {
                LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                future.fail(exception);
            }
        }, operation -> {
            // This is the adapter operation result management
            // Most probably this code will be the always same, we can see if it can be isolated somewhere, therefore
            // avoiding duplication and redundancy.
            /*
            //            if (operation.succeeded()) {

                asyncResultHandler.handle(
                        Future.succeededFuture(
                                GetTemplateResponse.withJsonOK(null)));
            } else {
                asyncResultHandler.handle(
                        Future.succeededFuture(
                                GetTemplateResponse.withPlainInternalServerError(
                                        PublicMessageCatalog.INTERNAL_SERVER_ERROR)));

            }
        });// , //asyncResultHandler, okapiHeaders, vertxContext);
    }
*/
    /**
     * This is the method where we are trying to hide the complexity of mixing non-blocking and blocking code.
     * This is called doGet because it handles GET methods, and I guess we will create one method for each
     * HTTP primitive.
     */
    public void doGet(
        final PieceOfExistingLogicAdapter adapter,
        final Handler<AsyncResult<Template>> resultHandler,
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
                                        final SessionFactory factory = HIBERNATE_CONFIGURATION.buildSessionFactory();
                                        session = factory.openSession(connection);

                                        adapter.execute(session, future);

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

    // Other RMb generated methods

    @Override
    public void getTemplates(String query, String orderBy, Order order, int offset, int limit, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
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