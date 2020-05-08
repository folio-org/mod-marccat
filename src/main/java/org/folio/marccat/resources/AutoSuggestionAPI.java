package org.folio.marccat.resources;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.resources.domain.*;
import org.folio.marccat.resources.shared.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import org.folio.marccat.config.log.Message;
/**
 *
 */
@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class AutoSuggestionAPI extends BaseResource {

  @GetMapping("/filteredTagsList")
  public FilteredTagsCollection getFilteredTagsList(
    @RequestParam final String tagNumber,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final FilteredTagsCollection container = new FilteredTagsCollection();
      container.setTags(storageService.getFilteredTagsList(tagNumber));
      return container;
    }, tenant, okapiUrl, configurator);
  }

  @ResponseStatus
  @GetMapping("/filteredTag")
  public ResponseEntity getFilteredTag(
    @RequestParam final String tagNumber,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME)  final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final FilteredTag container = storageService.getFilteredTag(tagNumber);
      return new ResponseEntity(container, HttpStatus.OK);
    }, tenant, okapiUrl, configurator);
  }


  @ResponseStatus
  @GetMapping("/validateTag")
  public ResponseEntity getValidateTag(
    @RequestParam final String ind1,
    @RequestParam final String ind2,
    @RequestParam final String tag,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME)  final String tenant,
    @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {
      final int category = storageService.getCategoryByTag(tag, ind1.charAt(0), ind2.charAt(0));
     if(category != 0) {
        final VariableField variableField = new VariableField();
        variableField.setCode(tag);
        variableField.setInd1(ind1);
        variableField.setInd2(ind2);
        return new ResponseEntity(variableField, HttpStatus.OK);
      }else {
       return validate(tag);
      }
    }, tenant, okapiUrl, configurator);
  }

  public ResponseEntity <Object> validate(@RequestParam String tag) {
    final ErrorCollection errors = new ErrorCollection();
    errors.getErrors().add(ValidationUtils.getError(Global.NO_TAG_FOUND, tag));
    return notFoundFailure(errors, String.format(Message.MOD_MARCCAT_00035_TAG_NOT_FOUND_FAILURE, tag));
  }


}


