package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import org.folio.cataloging.Global;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.jaxrs.model.HeadingType;
import org.folio.rest.jaxrs.model.ItemType;
import org.folio.rest.jaxrs.resource.CatalogingHeadingTypesResource;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Heading Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */

public class HeadingTypeAPI implements CatalogingHeadingTypesResource {

    protected final Log logger = new Log(HeadingTypeAPI.class);

    private Function<Avp<String>, ItemType> toItemType = source -> {
        final ItemType itemType = new ItemType();
        itemType.setCode(Integer.parseInt(source.getValue()));
        itemType.setDescription(source.getLabel());
        return itemType;
    };

    @Override
    public void getCatalogingHeadingTypes(final String marcCategory,
                                          final String lang,
                                          final Map<String, String> okapiHeaders,
                                          final Handler<AsyncResult<Response>> asyncResultHandler,
                                          final Context vertxContext) throws Exception {
        doGet((storageService, future) -> {
            try {
                //TODO : put default value for heading type name = 17 into configuration module!
                final String category = (marcCategory.equals("17") ? Global.NAME_CATEGORY_DEFAULT : marcCategory);

                final Class className = Global.headingClassType.get(category);
                final HeadingType headingType = new HeadingType();
                headingType.setMarcCategory(Integer.parseInt(category));
                headingType.setDescription(getDescription(category, storageService.getHeadingDescriptionByCode(category, lang)));

                headingType.setItemTypes(storageService.getItemTypesByCategoryCode(lang, className)
                        .stream()
                        .map(toItemType)
                        .collect(toList()));

                return headingType;

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

    private String getDescription(String marcCategory, String desc){
        if (marcCategory.equals("17"))
            return "Name";

        final Optional<String> description = Optional.ofNullable(desc);
        return description.isPresent() ? description.get() : description.orElse("");

    }
}
