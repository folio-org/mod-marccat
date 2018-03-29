package org.folio.rest.impl;


import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.Category;
import org.folio.rest.jaxrs.model.MarcCategoryCollection;
import org.folio.rest.jaxrs.resource.CatalogingCategoriesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Marc Categories RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class CategoriesAPI implements CatalogingCategoriesResource {

    protected final Log logger = new Log(CategoriesAPI.class);

    private Function<Avp<String>, Category> toCategory = source -> {
        final Category category = new Category();
        category.setCode(Integer.parseInt(source.getValue()));
        category.setDescription(source.getLabel());
        return category;
    };

    @Override
    public void getCatalogingCategories(final String lang,
                                        final Map<String, String> okapiHeaders,
                                        final Handler<AsyncResult<Response>> asyncResultHandler,
                                        final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {
                final MarcCategoryCollection categories = new MarcCategoryCollection();
                categories.setCategories(
                        storageService.getMarcCategories(lang)
                                .stream()
                                .map(toCategory)
                                .collect(toList()));

                return categories;

            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingCategories(String lang, Category entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
