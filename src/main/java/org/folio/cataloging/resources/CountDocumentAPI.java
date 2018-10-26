package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.domain.CountDocument;
import org.springframework.web.bind.annotation.*;

import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * CountDocument RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Heading resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class CountDocumentAPI extends BaseResource {

  /**
   * Gets the count of the record.
   */
  @ApiOperation(value = "Return count of the record.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the count record"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/document-count-by-id")
  public CountDocument getDocumentCountById(
    @RequestParam final int id,
    @RequestParam final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      try {
        final CountDocument countDocument = storageService.getCountDocumentByAutNumber(id, view);
        return countDocument;
      } catch (final Exception exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator);
  }


}
