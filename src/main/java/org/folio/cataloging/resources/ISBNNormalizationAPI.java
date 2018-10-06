package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.springframework.web.bind.annotation.*;


/**
 * ISBN Utility  API.
 *
 * @since 1.0
 * @author Christian Chiama
 */
@RestController
@CrossOrigin("http://localhost:3000")
@Api(value = "modcat-api", description = "ISBN Utility  API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ISBNNormalizationAPI {
  

  @GetMapping("/isbn/removeHyphen")
  public String removeHypens(
    @RequestParam final String lang,
    @RequestParam final String ISBN,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return "remove hyphen";
  }

  @GetMapping("/isbn/conversion")
  public String convertISBN(
    @RequestParam final String lang,
    @RequestParam final String ISBN,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return "converison ok";

  }
}
