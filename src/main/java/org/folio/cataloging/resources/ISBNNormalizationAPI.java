package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.web.bind.annotation.*;


/**
 * ISBNAlgorithm Utility  API.
 *
 * @author Christian Chiama
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "ISBNAlgorithm Utility  API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class ISBNNormalizationAPI extends BaseResource {


  public static void main(String[] args) throws Exception {

    @ISBN(message = "not valid isbn") final String isbn = "5";

  }

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
    @RequestParam @ISBN(message = "not valid isbn") final String isbn,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return "remove hyphen";
  }
}
