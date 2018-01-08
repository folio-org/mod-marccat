package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.Constraint;
import org.folio.rest.jaxrs.model.Index;
import org.folio.rest.jaxrs.model.IndexCollection;
import org.folio.rest.jaxrs.resource.IndexesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Browsing and searching indexes API
 *
 * @author carment
 * @since 1.0
 */
public class IndexesAPI implements IndexesResource {

    protected final Log logger = new Log(IndexesAPI.class);

    private Function<ValueLabelElement<String>, Index> convertValueLabelToIndex = source -> {
        final Index index = new Index();
        index.setCode(source.getValue());
        index.setDescription(source.getLabel());
        return index;
    };

    private Function<ValueLabelElement<String>, Constraint> convertValueLabelToConstraint = source -> {
        final Constraint constraint = new Constraint();
        constraint.setCode(source.getValue());
        constraint.setLabel(source.getLabel());
        return constraint;
    };


    @Override
    public void getIndexesByCode(
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
    public void getIndexes(
            final IndexesResource.CategoryType categoryType,
            final int categoryCode,
            final String lang,
            final Map<String,String>okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler,
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
    public void postIndexes(String lang, Index entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void deleteIndexesByCode(String code, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

    @Override
    public void putIndexesByCode(String code, String lang, Index entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}