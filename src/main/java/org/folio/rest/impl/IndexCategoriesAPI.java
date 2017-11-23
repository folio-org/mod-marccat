package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.rest.jaxrs.model.Category;
import org.folio.rest.jaxrs.resource.IndexCategoriesResource;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Logical views RESTful APIs.
 *
 * @author
 * @since 1.0
 */
public class IndexCategoriesAPI implements IndexCategoriesResource {

    @Override
    public void getIndexCategories(Type type, String lang, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        // TODO: implement following the LogicalViewsAPI.getLogicalViews() flow.
    }

    @Override
    public void postIndexCategories(String lang, Category entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
      throw new IllegalArgumentException();
    }
}
