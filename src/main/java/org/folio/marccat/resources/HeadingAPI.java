package org.folio.marccat.resources;

import org.folio.marccat.config.Global;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.MessageCatalog;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.ModMarccat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.folio.marccat.integration.CatalogingHelper.*;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

/**
 * Headings RESTful APIs.
 *
 * @author cchiama
 * @author carment
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class HeadingAPI extends BaseResource {


  @PostMapping("/create-heading")
  public ResponseEntity <Heading> createHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doPost((storageService, configuration) -> {
      storageService.saveHeading(heading, view, configuration);
      return heading;
    }, tenant, configurator, () -> (isNotNullOrEmpty(heading.getStringText())), "title", "subject", "name");
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/update-heading")
  public void updateHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    doPut((storageService, configuration) -> {
      try {
        storageService.updateHeading(heading, view);
        return heading;
      } catch (final Exception exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator, () -> (isNotNullOrEmpty(heading.getStringText()) && heading.getHeadingNumber() != 0));
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/delete-heading")
  public void deleteHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    doDelete((storageService, configuration) -> {
      try {
        storageService.deleteHeadingById(heading, view);
        return heading;
      } catch (final Exception exception) {
        logger.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, configurator);
  }
}
