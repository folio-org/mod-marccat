package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.HeadingType;
import org.folio.marccat.resources.domain.HeadingTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Heading Types RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Heading type resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class HeadingTypeAPI extends BaseResource {

  private Function <Avp <String>, HeadingType> toHeadingType = source -> {
    final HeadingType headingType = new HeadingType();
    headingType.setCode(Integer.parseInt(source.getValue()));
    headingType.setDescription(source.getLabel());
    return headingType;
  };

  @ApiOperation(value = "Returns all heading types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested heading types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/heading-types")
  public HeadingTypeCollection getHeadingTypes(
    @RequestParam final String marcCategory,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
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
    }, tenant, configurator);
  }
}
