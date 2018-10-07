package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.DetailLevel;
import org.folio.cataloging.resources.domain.DetailLevelCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Detail levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Detail level resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class DetailLevelAPI extends BaseResource {
  private Function <Avp <String>, DetailLevel> toDetailLevel = source -> {
    final DetailLevel detailLevel = new DetailLevel ( );
    detailLevel.setCode (source.getValue ( ));
    detailLevel.setDescription (source.getLabel ( ));
    return detailLevel;
  };

  @ApiOperation(value = "Returns all detail levels associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested detail levels"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/detail-levels")
  public DetailLevelCollection getDetailLevels(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final DetailLevelCollection container = new DetailLevelCollection ( );
      container.setDetailLevels (
        storageService.getDetailLevels (lang)
          .stream ( )
          .map (toDetailLevel)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);

  }
}
