package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.ControlType;
import org.folio.cataloging.resources.domain.ControlTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Control Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Control types resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ControlTypeAPI extends BaseResource {

  private Function <Avp <String>, ControlType> toControlType = source -> {
    final ControlType controlType = new ControlType ( );
    controlType.setCode (source.getValue ( ));
    controlType.setDescription (source.getLabel ( ));
    return controlType;
  };

  @ApiOperation(value = "Returns all control types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested control types"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("control-types")
  public ControlTypeCollection getControlTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final ControlTypeCollection container = new ControlTypeCollection ( );
      container.setControlTypes (
        storageService.getControlTypes (lang)
          .stream ( )
          .map (toControlType)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);
  }
}


