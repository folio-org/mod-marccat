package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.CategoryType;
import org.folio.marccat.resources.domain.Constraint;
import org.folio.marccat.resources.domain.Index;
import org.folio.marccat.resources.domain.IndexCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Browsing and searching indexes API
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Index resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class IndexesAPI extends BaseResource {

  private Function <Avp <String>, Index> convertValueLabelToIndex = source -> {
    final Index index = new Index();
    index.setCode(source.getValue());
    index.setDescription(source.getLabel());
    return index;
  };

  private Function <Avp <String>, Constraint> convertValueLabelToConstraint = source -> {
    final Constraint constraint = new Constraint();
    constraint.setCode(source.getValue());
    constraint.setLabel(source.getLabel());
    return constraint;
  };

  @ApiOperation(value = "Returns the index associated with a given code")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested index."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/indexes/{code}")
  public Index getIndexByCode(
    @PathVariable final String code,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configurator) -> {
      final Index container = new Index();
      container.setCode(code);
      container.setDescription(storageService.getIndexDescription(code, lang));
      container.setConstraints(
        storageService.getIndexesByCode(code, lang)
          .stream()
          .map(convertValueLabelToConstraint)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }

  @ApiOperation(value = "Returns the indexes associated with a given type.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested indexes."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/indexes")
  public IndexCollection getIndexes(
    @RequestParam final CategoryType categoryType,
    @RequestParam final int categoryCode,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final IndexCollection container = new IndexCollection();
      container.setIndexes(
        storageService.getIndexes(categoryType.name(), categoryCode, lang)
          .stream()
          .map(convertValueLabelToIndex)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
