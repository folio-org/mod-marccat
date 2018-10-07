package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.Category;
import org.folio.cataloging.resources.domain.MarcCategoryCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Marc Categories RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "MARC Category resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class MarcCategoryAPI extends BaseResource {

  private Function <Avp <String>, Category> toCategory = source -> {
    final Category category = new Category ( );
    category.setCode (Integer.parseInt (source.getValue ( )));
    category.setDescription (source.getLabel ( ));
    return category;
  };

  @ApiOperation(value = "Returns all MARC categories associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested MARC categories"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/marc-categories")
  public MarcCategoryCollection getCategories(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final MarcCategoryCollection categories = new MarcCategoryCollection ( );
      categories.setCategories (
        storageService.getMarcCategories (lang)
          .stream ( )
          .map (toCategory)
          .collect (toList ( )));
      return categories;
    }, tenant, configurator);
  }
}
