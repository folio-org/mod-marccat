package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * HRID RESTful APIs.
 *
 * @description: API to handler human readble identifier in all app folio
 *
 * @author cchiama
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class HRIDAPI extends BaseResource {


  @GetMapping("/hrid")
  public ResponseEntity<Object> getHRID(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return new ResponseEntity<>(Global.EMPTY_STRING, HttpStatus.OK);
  }

  @PostMapping("/hrid")
  public ResponseEntity<Object> createHRID(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return new ResponseEntity<>(Global.EMPTY_STRING, HttpStatus.OK);
  }

  @PutMapping("/hrid/{id}")
  public ResponseEntity<Object> editHRID(
    @PathVariable final Integer id,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return new ResponseEntity<>(Global.EMPTY_STRING, HttpStatus.OK);
  }

  @DeleteMapping("/hrid/{id}")
  public ResponseEntity<Object> deleteHRID(
    @PathVariable final Integer id,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return new ResponseEntity<>(Global.EMPTY_STRING, HttpStatus.OK);
  }
}
