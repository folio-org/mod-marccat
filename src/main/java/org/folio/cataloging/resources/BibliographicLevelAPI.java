package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.BibliographicLevel;
import org.folio.cataloging.resources.domain.BibliographicLevelCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Bibliographic Levels RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Bibliographic Level resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class BibliographicLevelAPI extends BaseResource {

  private Function <Avp <String>, BibliographicLevel> toBibliographicLevel = source -> {
    final BibliographicLevel bibliographicLevel = new BibliographicLevel ( );
    bibliographicLevel.setCode (source.getValue ( ));
    bibliographicLevel.setDescription (source.getLabel ( ));
    return bibliographicLevel;
  };

  @ApiOperation(value = "Returns all bibliographic levels associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested bibliographic levels"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/bibliographic-levels")
  public BibliographicLevelCollection getBibliographicLevels(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final BibliographicLevelCollection container = new BibliographicLevelCollection ( );
      container.setBibliographicLevels (
        storageService.getBibliographicLevels (lang)
          .stream ( )
          .map (toBibliographicLevel)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);
  }
}
