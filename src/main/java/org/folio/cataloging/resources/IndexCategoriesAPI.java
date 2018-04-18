package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.Category;
import org.folio.rest.jaxrs.model.IndexCategoryCollection;
import org.folio.rest.jaxrs.resource.CatalogingIndexCategoriesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Logical views RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
public class IndexCategoriesAPI implements CatalogingIndexCategoriesResource {
    protected final Log logger = new Log(IndexCategoriesAPI.class);

    // This is the convertValueLabelToCategory that converts existing stringValue objects (logical categories in this case)
    // in OKAPI resources.
    private Function<Avp<Integer>, Category> convertValueLabelToCategory = source -> {
        final Category category = new Category();
        category.setCode(source.getValue());
        return category;
    };


    @Override
    public void getCatalogingIndexCategories(
            final CatalogingIndexCategoriesResource.Type type,
            final String lang,
            final Map<String, String> okapiHeaders,
            final Handler<AsyncResult<Response>> asyncResultHandler,
            final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final IndexCategoryCollection container = new IndexCategoryCollection();
                container.setCategories(
                        storageService.getIndexCategories(type.name(),lang)
                                .stream()
                                .map(convertValueLabelToCategory)
                                .collect(toList()));
                return container;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingIndexCategories(String lang, Category entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
      throw new IllegalArgumentException();
    }
}
