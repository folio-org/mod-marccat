package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.Constraint;
import org.folio.rest.jaxrs.model.Index;
import org.folio.rest.jaxrs.model.IndexCollection;
import org.folio.rest.jaxrs.resource.CatalogingIndexesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;
import static org.folio.cataloging.integration.CatalogingHelper.doPut;

/**
 * Browsing and searching indexes API
 *
 * @author carment
 * @since 1.0
 */
public class IndexesAPI implements CatalogingIndexesResource {

    protected final Log logger = new Log(IndexesAPI.class);

    private Function<Avp<String>, Index> convertValueLabelToIndex = source -> {
        final Index index = new Index();
        index.setCode(source.getValue());
        index.setDescription(source.getLabel());
        return index;
    };

    private Function<Avp<String>, Constraint> convertValueLabelToConstraint = source -> {
        final Constraint constraint = new Constraint();
        constraint.setCode(source.getValue());
        constraint.setLabel(source.getLabel());
        return constraint;
    };


    @Override
    public void getCatalogingIndexesByCode(
            final String code,
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                final Index container = new Index();
                container.setCode(code);
                container.setDescription(storageService.getIndexDescription(code, lang));
                container.setConstraints(
                        storageService.getIndexesByCode(code,lang)
                                .stream()
                                .map(convertValueLabelToConstraint)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void getCatalogingIndexes(
            final CatalogingIndexesResource.CategoryType categoryType,
            final int categoryCode,
            final String lang,
            final Map<String,String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
            doGet((storageService, future) -> {
            try {
                final IndexCollection container = new IndexCollection();
                container.setIndexes(
                        storageService.getIndexes(categoryType.name(), categoryCode, lang)
                                .stream()
                                .map(convertValueLabelToIndex)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingIndexes(String lang, Index entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
        throw new IllegalArgumentException();
    }

    @Override
    public void deleteCatalogingIndexesByCode(String code, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
        throw new IllegalArgumentException();
    }

    @Override
    public void putCatalogingIndexesByCode(String code, String lang, Index entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        doPut((storageService, future) -> {
            try {
                // Here we have to manage the update of the entity associated with the incoming id, using the given state
                // (the entity attribute).
                // Note that the validation predicate (the last parameter) will be invoked before entering here so at
                // this time the incoming state (the entity parameter) is supposed to be valid.

                // So: here there's the update logic...

                // And here, since the interface require something to be returned, in case everything goes well
                // we will return the same entity passed in input.
                return null;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext, () -> {
            // Here each service should provide a body where the incoming entity is validated.
            // The function return true or false depending on the "congruency" of the entity.
            // Note that if you return false here, no other method will be called, a 400 BAD request will be returned.
            return false;
        });
    }
}