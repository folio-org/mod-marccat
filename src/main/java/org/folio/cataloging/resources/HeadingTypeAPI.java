package org.folio.cataloging.resources;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.HeadingType;
import org.folio.rest.jaxrs.model.HeadingTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingHeadingTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * Heading Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class HeadingTypeAPI implements CatalogingHeadingTypesResource {

    protected final Log logger = new Log(HeadingTypeAPI.class);

    private Function<Avp<String>, HeadingType> toHeadingType = source -> {
        final HeadingType headingType = new HeadingType();
        headingType.setCode(Integer.parseInt(source.getValue()));
        headingType.setDescription(source.getLabel());
        return headingType;
    };

    @Override
    public void getCatalogingHeadingTypes(final String marcCategory,
                                          final String lang,
                                          final Map<String, String> okapiHeaders,
                                          final Handler<AsyncResult<Response>> asyncResultHandler,
                                          final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {

                final int category = (marcCategory.equals("17") ? Global.NAME_CATEGORY_DEFAULT : Integer.parseInt(marcCategory));
                return (storageService.existHeadingTypeByCategory(category))
                        ? ofNullable(storageService.getFirstCorrelation(lang, category))
                            .map(headingTypeList -> {
                                final HeadingTypeCollection container = new HeadingTypeCollection();
                                container.setHeadingTypes(headingTypeList
                                        .stream()
                                        .map(toHeadingType)
                                        .collect(toList()));

                                return container;
                            }).orElse(null)
                        : null;

            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);

    }

    @Override
    public void postCatalogingHeadingTypes(String lang, HeadingType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }

}
