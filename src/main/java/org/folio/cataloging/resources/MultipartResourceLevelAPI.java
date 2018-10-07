package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.MultipartResourceLevel;
import org.folio.cataloging.resources.domain.MultipartResourceLevelCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Multipart Resource levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Multipart resource level resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class MultipartResourceLevelAPI extends BaseResource {

  private Function <Avp <String>, MultipartResourceLevel> toMultipartResourceLevel = source -> {
    final MultipartResourceLevel multipartResourceLevel = new MultipartResourceLevel ( );
    multipartResourceLevel.setCode (source.getValue ( ));
    multipartResourceLevel.setDescription (source.getLabel ( ));
    return multipartResourceLevel;
  };

  @ApiOperation(value = "Returns all levels associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested levels."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/multipart-resource-levels")
  public MultipartResourceLevelCollection getMultipartResourceLevels(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final MultipartResourceLevelCollection container = new MultipartResourceLevelCollection ( );
      container.setMultipartResourceLevels (
        storageService.getMultipartResourceLevels (lang)
          .stream ( )
          .map (toMultipartResourceLevel)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);
  }
}
