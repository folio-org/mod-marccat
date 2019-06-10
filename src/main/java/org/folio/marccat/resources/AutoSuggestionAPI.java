package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.resources.domain.FilteredTag;
import org.folio.marccat.resources.domain.FilteredTagsCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.folio.marccat.integration.MarccatHelper.doGet;


/**
 * Utility RESTful APIs.
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class AutoSuggestionAPI extends BaseResource{

  @GetMapping("/filteredTagsList")
  public FilteredTagsCollection getFilteredTagsList(
    @RequestParam final String tagNumber,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FilteredTagsCollection container = new FilteredTagsCollection();
      container.setTags(storageService.getFilteredTagsList(tagNumber));
      return container;
    }, tenant, configurator);
  }

  
  @ResponseStatus
  @GetMapping("/filteredTag")
  public ResponseEntity getFilteredTag(
    @RequestParam final String tagNumber,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME)  final String tenant) {
    return doGet((storageService, configuration) -> {
       final FilteredTag container = storageService.getFilteredTag(tagNumber);
        return new ResponseEntity(container, HttpStatus.OK);
    }, tenant, configurator);
  }
}
