package org.folio.marccat.resources;

import org.folio.marccat.annotation.ResourcePath;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.enumaration.CodeListsType;
import org.folio.marccat.resources.domain.FilteredTag;
import org.folio.marccat.resources.domain.FilteredTagsCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.folio.marccat.enumaration.CodeListsType.DATE_TYPE;
import static org.folio.marccat.enumaration.RequestMapping.AUTOSUGGESTION;
import static org.folio.marccat.integration.MarccatHelper.doGet;

/**
 *
 */
public class AutoSuggestionAPI extends BaseResource {

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
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final FilteredTag container = storageService.getFilteredTag(tagNumber);
      return new ResponseEntity(container, HttpStatus.OK);
    }, tenant, configurator);
  }
}
