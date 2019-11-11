package org.folio.marccat.resources;

import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.shared.RecordUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.*;
import static org.folio.marccat.util.F.isNotNullOrEmpty;

@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class HeadingAPI extends BaseResource {


  @PostMapping("/create-heading")
  public ResponseEntity<Heading> createHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doPost((storageService, configuration) -> {
      heading.setCategoryCode(RecordUtils.getTagCategory(heading, storageService));
      storageService.saveHeading(heading, view, configuration);
      return heading;
    }, tenant, okapiUrl, configurator, () -> (isNotNullOrEmpty(heading.getDisplayValue())), "title", "subject", "name", "controlNumber", "classification", "publisher", "nameTitle");
  }


  @PutMapping("/update-heading")
  public ResponseEntity<Heading> updateHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doPut((storageService, configuration) -> {
      try {
        storageService.updateHeading(heading, view);
        return heading;
      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, okapiUrl,configurator, () -> (isNotNullOrEmpty(heading.getDisplayValue()) && heading.getKeyNumber() != 0));
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/delete-heading")
  public void deleteHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    doDelete((storageService, configuration) -> {
      try {
        storageService.deleteHeadingById(heading, view);
        return heading;
      } catch (final Exception exception) {
        logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
        return null;
      }
    }, tenant, okapiUrl, configurator);
  }
}
