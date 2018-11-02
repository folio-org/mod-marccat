package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.ItemType;
import org.folio.marccat.resources.domain.ItemTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Item heading Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Item type resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class ItemTypeAPI extends BaseResource {

  private Function <Avp <String>, ItemType> toItemType = source -> {
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
  public ItemTypeCollection getItemTypes(
    @RequestParam final String marcCategory,
    @RequestParam final int code,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configurator) -> {
      final int category = (marcCategory.equals("17") ? Global.NAME_CATEGORY_DEFAULT : Integer.parseInt(marcCategory));
      final ItemTypeCollection container = new ItemTypeCollection();
      return (storageService.existItemTypeByCategory(category))
        ? ofNullable(storageService.getSecondCorrelation(category, code, lang))
        .map(itemTypeList -> {
          container.setItemTypes(
            itemTypeList.stream()
              .map(toItemType)
              .collect(toList()));
          return container;
        }).orElse(container)
        : container;
    }, tenant, configurator);
  }
}
