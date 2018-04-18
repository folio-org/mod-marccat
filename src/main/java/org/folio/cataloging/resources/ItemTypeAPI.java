package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.ItemType;
import org.folio.cataloging.resources.domain.ItemTypeCollection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * Item heading Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Item type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ItemTypeAPI  extends BaseResource {

    private Function<Avp<String>, ItemType> toItemType = source -> {
        final ItemType itemType = new ItemType();
        itemType.setCode(Integer.parseInt(source.getValue()));
        itemType.setDescription(source.getLabel());
        return itemType;
    };

    @ApiOperation(value = "Returns all item types associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested item types."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/item-types")
    public ItemTypeCollection getItemTypes(final String marcCategory,
                                           final String code,
                                           final String lang,
                                           final Map<String, String> okapiHeaders,
                                           final Handler<AsyncResult<Response>> asyncResultHandler,
                                           final Context vertxContext) throws Exception {
        doGet((storageService, configuration, future) -> {
            try {

                final int category = (marcCategory.equals("17") ? Global.NAME_CATEGORY_DEFAULT : Integer.parseInt(marcCategory));
                final int intCode = Integer.parseInt(code);
                return (storageService.existItemTypeByCategory(category))
                        ? ofNullable(storageService.getSecondCorrelation(category, intCode, lang))
                            .map(itemTypeList -> {
                                final ItemTypeCollection container = new ItemTypeCollection();
                                container.setItemTypes(itemTypeList
                                        .stream()
                                        .map(toItemType)
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
    public void postCatalogingItemTypes(String lang, ItemType entity, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) throws Exception {
        throw new IllegalArgumentException();
    }
}
