package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.AuthoritySource;
import org.folio.rest.jaxrs.model.AuthoritySourceCollection;
import org.folio.rest.jaxrs.resource.CatalogingAuthoritySourcesResource;
import org.folio.rest.jaxrs.resource.CatalogingAuthoritySourcesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Authority Sources RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class AuthoritySourcesAPI implements CatalogingAuthoritySourcesResource {

    protected final Log logger = new Log(AuthoritySourcesAPI.class);

    private Function<Avp<String>, AuthoritySource> toAuthoritySource = source -> {
        final AuthoritySource authoritySource = new AuthoritySource();
        authoritySource.setCode(Integer.parseInt(source.getValue()));
        authoritySource.setDescription(source.getLabel());
        return authoritySource;
    };

    @Override
    public void getCatalogingAuthoritySources(final String lang,
                                    final Map<String, String> okapiHeaders,
                                    final Handler<AsyncResult<Response>> asyncResultHandler,
                                    final Context vertxContext) throws Exception {

        doGet((storageService, future) -> {
            try {
                final AuthoritySourceCollection container = new AuthoritySourceCollection();
                container.setAuthoritySources(
                        storageService.getAuthoritySources(lang)
                                .stream()
                                .map(toAuthoritySource)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingAuthoritySources(final String lang,
                                     final AuthoritySource entity,
                                     final Map<String, String> okapiHeaders,
                                     final Handler<AsyncResult<Response>> asyncResultHandler,
                                     final Context vertxContext) throws Exception {
        throw new IllegalArgumentException();

    }
}
