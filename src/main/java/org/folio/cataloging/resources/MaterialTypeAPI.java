package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.MaterialType;
import org.folio.cataloging.resources.domain.MaterialTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Material type RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Material type resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class MaterialTypeAPI extends BaseResource {

  private Function <Avp <String>, MaterialType> toMaterialType = source -> {
    final MaterialType materialType = new MaterialType ( );
    materialType.setCode (source.getValue ( ));
    materialType.setDescription (source.getLabel ( ));
    return materialType;
  };

  @ApiOperation(value = "Returns all material types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested material types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/material-types")
  public MaterialTypeCollection getMaterialTypes(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final MaterialTypeCollection container = new MaterialTypeCollection ( );
      container.setMaterialTypes (
        storageService.getMaterialTypes (lang)
          .stream ( )
          .map (toMaterialType)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);
  }
}
