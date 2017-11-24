package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.rest.jaxrs.model.Index;
import org.folio.rest.jaxrs.resource.IndexesResource;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Browsing and searching indexes API
 *
 * @since 1.0
 */
public class IndexesAPI implements IndexesResource {
    @Override
    public void getIndexes(String category, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        // TODO
    }

    @Override
    public void getIndexesByCode(String code, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        // TODO
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