package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.dao.persistence.NameSubType;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.ItemType;
import org.folio.rest.jaxrs.model.SubType;
import org.folio.rest.jaxrs.resource.CatalogingItemTypesResource;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Item heading Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class ItemTypeAPI  implements CatalogingItemTypesResource {

    protected final Log logger = new Log(HeadingTypeAPI.class);

    //put other marc category with associated subType class
    HashMap<String, Class> subTypeClass = new HashMap<>();
    {
        subTypeClass.put("2", NameSubType.class);
        subTypeClass.put("17", NameSubType.class);
    }

    private Function<Avp<String>, SubType> toSubType = source -> {
        final SubType subType = new SubType();
        subType.setCode(Integer.parseInt(source.getValue()));
        subType.setDescription(source.getLabel());
        return subType;
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

                final int intCode = Integer.parseInt(code);
                final Class className = subTypeClass.get(marcCategory);
                final ItemType itemType = new ItemType();
                itemType.setCode(intCode);
                itemType.setDescription(storageService.getSubTypeDescriptionByCode(code, lang));
                itemType.setSubTypes(storageService.getSubTypesByCategoryCode(marcCategory, intCode, lang, subTypeClass.get(marcCategory))
                        .stream()
                        .map(toSubType)
                        .collect(toList()));
                return itemType;
            } catch (final Exception exception) {
                logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
                return null;
            }
        }, asyncResultHandler, okapiHeaders, vertxContext);
    }

    @Override
    public void postCatalogingItemTypes(String lang, ItemType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {

    }
}
