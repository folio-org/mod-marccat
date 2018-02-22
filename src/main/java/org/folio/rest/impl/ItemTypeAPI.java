package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.ItemType;
import org.folio.rest.jaxrs.model.ItemTypeCollection;
import org.folio.rest.jaxrs.resource.CatalogingItemTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Item heading Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class ItemTypeAPI  implements CatalogingItemTypesResource {

    protected final Log logger = new Log(ItemTypeAPI.class);

    private Function<Avp<String>, ItemType> toItemType = source -> {
        final ItemType itemType = new ItemType();
        itemType.setCode(Integer.parseInt(source.getValue()));
        itemType.setDescription(source.getLabel());
        return itemType;
    };

    @Override
    public void getCatalogingItemTypes(final String marcCategory,
                                       final String code,
                                       final String lang,
                                       final Map<String, String> okapiHeaders,
                                       final Handler<AsyncResult<Response>> asyncResultHandler,
                                       final Context vertxContext) throws Exception {

        doGet((storageService, future) -> {
            try {

                final String category = (marcCategory.equals("17") ? Global.NAME_CATEGORY_DEFAULT : marcCategory);
                final int intCode = Integer.parseInt(code);
                return ofNullable(Global.secondCorrelationClassMap.get(category))
                            .map(className -> {
                                final ItemTypeCollection container = new ItemTypeCollection();
                                container.setItemTypes(storageService.getSecondCorrelation(category, intCode, lang, className)
                                        .stream()
                                        .map(toItemType)
                                        .collect(toList()));

                                return container;
                            }).orElse(null);

            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingItemTypes(String lang, ItemType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
