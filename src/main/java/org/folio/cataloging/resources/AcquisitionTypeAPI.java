package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.AcquisitionType;
import org.folio.cataloging.resources.domain.AcquisitionTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Acquisition Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Acquisition Type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class AcquisitionTypeAPI extends BaseResource {
  private Function <Avp <String>, AcquisitionType> toAcquisitionType = source -> {
    final AcquisitionType acquisitionType = new AcquisitionType ( );
    acquisitionType.setCode (Integer.parseInt (source.getValue ( )));
    acquisitionType.setDescription (source.getLabel ( ));
    return acquisitionType;
  };

  @ApiOperation(value = "Returns all acquisition types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested acquisition types"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/acquisition-types")
  public AcquisitionTypeCollection getAcquisitionTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final AcquisitionTypeCollection container = new AcquisitionTypeCollection ( );
      container.setAcquisitionTypes (
        storageService.getAcquisitionTypes (lang)
          .stream ( )
          .map (toAcquisitionType)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);
  }
}
