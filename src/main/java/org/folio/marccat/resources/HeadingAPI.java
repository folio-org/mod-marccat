package org.folio.marccat.resources;

import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.resources.domain.ErrorCollection;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.resources.shared.RecordUtils;
import org.folio.marccat.resources.shared.ValidationUtils;
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
    }, tenant, okapiUrl, configurator, () -> (isNotNullOrEmpty(heading.getDisplayValue())));
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
  public ResponseEntity<Object> deleteHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doDeleteWithResponse((storageService, configuration) -> {
      try {
        storageService.deleteHeadingById(heading, view);
        return new ResponseEntity(heading, HttpStatus.OK);
      } catch (final Exception exception) {
        return new ResponseEntity(heading, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }, tenant, okapiUrl, configurator);
  }

  @PostMapping("/save-heading")
  public ResponseEntity saveHeading(
    @RequestBody final Heading heading,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doPost((storageService, configuration) -> {
      final int category = RecordUtils.getTagCategory(heading, storageService);
      if (category != 0) {
        heading.setCategoryCode(category);
        storageService.saveHeading(heading, view, configuration);
        return heading;
      }
      else {
        return validateTag(heading.getTag());
      }
    }, tenant, okapiUrl, configurator, () -> (isNotNullOrEmpty(heading.getDisplayValue())));
  }

  private ResponseEntity <Object> validateTag(final String tag) {
    final ErrorCollection errors = new ErrorCollection();
    errors.getErrors().add(ValidationUtils.getError(Global.NO_TAG_FOUND, tag));
    return notFoundFailure(errors, String.format(Message.MOD_MARCCAT_00035_TAG_NOT_FOUND_FAILURE, tag));
  }


}
