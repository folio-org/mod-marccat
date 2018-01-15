package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.FrbrType;
import org.folio.rest.jaxrs.model.FrbrTypeCollection;
import org.folio.rest.jaxrs.resource.FrbrTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * FRBR type RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class FrbrTypesAPI implements FrbrTypesResource {

    protected final Log logger = new Log(FrbrTypesAPI.class);

    private Function<Avp<String>, FrbrType> toFrbrType = source -> {
        final FrbrType frbrType = new FrbrType();
        frbrType.setCode(Integer.parseInt(source.getValue()));
        frbrType.setDescription(source.getLabel());
        return frbrType;
    };

    @Override
    public void getFrbrTypes(final String lang,
                             final Map<String, String> okapiHeaders,
                             final Handler<AsyncResult<Response>> asyncResultHandler,
                             final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final FrbrTypeCollection container = new FrbrTypeCollection();
                container.setFrbrTypes(
                        storageService.getFrbrTypes(lang)
                                .stream()
                                .map(toFrbrType)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void getFrbrTypesByCode(final String code,
                                   final String lang,
                                   final Map<String, String> okapiHeaders,
                                   final Handler<AsyncResult<Response>> asyncResultHandler,
                                   final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final FrbrType frbrType = new FrbrType();
                /*Integer icode = Integer.parseInt(code);
                frbrType.setCode(icode);
                frbrType.setDescription(
                    storageService.getFrbrDescriptionByCode(icode, lang)
                );*/
                frbrType.setCode(Integer.parseInt(code));
                frbrType.setDescription(
                        storageService.getFrbrDescriptionByCode(code, lang)
                );
                return frbrType;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postFrbrTypes(String lang, FrbrType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void deleteFrbrTypesByCode(String code, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putFrbrTypesByCode(String code, String lang, FrbrType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
