package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.resources.domain.FiltedTagsCollection;
import org.folio.marccat.resources.domain.FilteredTag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.folio.marccat.integration.MarccatHelper.doGet;

/**
 * Utility RESTful APIs.
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class AutoSuggestionAPI extends BaseResource{

  @ResponseStatus
  @GetMapping("/filterdTagsList")
  public FiltedTagsCollection getFilterdTagsList(
    @RequestParam final String tagNumber,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FiltedTagsCollection container = new FiltedTagsCollection();
      container.setTags(storageService.getFilteredTagsList(tagNumber));
      return container;
    }, tenant, configurator);
  }

  @ResponseStatus
  @GetMapping("/filterdTag")
  public ResponseEntity getFilterdTag(
    @RequestParam final String tagNumber,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FilteredTag container = new FilteredTag();
      List<String> tagList = storageService.getFilteredTag(tagNumber);
      if(!tagList.isEmpty()) {
        container.setTag(tagList.get(0));
        container.setInd1(tagList.get(1));
        container.setInd2(tagList.get(2));
        container.getSubfields(tagList.get(3));
      }
     return new ResponseEntity<>(container, HttpStatus.OK);
    }, tenant, configurator);
  }
}
