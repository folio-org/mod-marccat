package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.Index;
import org.folio.marccat.resources.domain.IndexCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Browsing and searching browse indexes API
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Browse index resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class BrowseIndexesAPI extends BaseResource {

  private Function <Avp <String>, Index> convertValueLabelToIndex = source -> {
    final Index index = new Index();
    index.setCode(source.getValue());
    index.setDescription(source.getLabel());
    return index;
  };


  @ApiOperation(value = "Returns the browse indexes associated with a given type.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested indexes."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/browse-indexes")
  public IndexCollection getBrowseIndexes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final IndexCollection container = new IndexCollection();
      container.setIndexes(
        storageService.getBrowseIndexes(lang)
          .stream()
          .map(convertValueLabelToIndex)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
