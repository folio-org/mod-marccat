package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.constants.Global;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.folio.marccat.config.constants.Global.TAGS;

/**
 * Utility RESTful APIs.
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class UtilsAPI extends BaseResource{


  @ResponseStatus
  @GetMapping("/marcTags")
  public ResponseEntity<Map<String, String>> getAllMARCTag(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return new ResponseEntity<>(TAGS, HttpStatus.OK);
  }

}
