package org.folio.marccat.resources;

import org.folio.marccat.business.common.View;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.search.SearchEngineFactory;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.engine.SearchEngine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.util.F.locale;

@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class SearchAPI extends BaseResource {

  @GetMapping("/search")
  public SearchResponse search(
    @RequestParam(name = "lang", defaultValue = "eng") final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestParam("q") final String q,
    @RequestParam(name = "from", defaultValue = "1") final int from,
    @RequestParam(name = "to", defaultValue = "10") final int to,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestParam(name = "ml", defaultValue = "170") final int mainLibraryId,
    @RequestParam(name = "dpo", defaultValue = "1") final int databasePreferenceOrder,
    @RequestParam(name = "sortBy", required = false) final String[] sortAttributes,
    @RequestParam(name = "sortOrder", required = false) final String[] sortOrders) {
    return doGet((StorageService storageService, Map<String, String> configuration) -> {
       final SearchEngine searchEngine =
        SearchEngineFactory.create(
          SearchEngineFactory.EngineType.LIGHTWEIGHT,
          mainLibraryId,
          databasePreferenceOrder,
          storageService);
      SearchResponse response;

      response = searchEngine.fetchRecords(
        searchEngine.expertSearch(q, locale(lang), view, from, to, sortAttributes, sortOrders),
        "F",
        1,
        ((to - from) + 1));

      final int AUTHORITY_VIEW = -1;
      if (view == AUTHORITY_VIEW) {
        searchEngine.injectDocCount(response, storageService);
      }
      return response;
    }, tenant, configurator);
  }


  @GetMapping("/mergedSearch")
  public List<SearchResponse> mergedSearch(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestParam("qbib") final String qbib,
    @RequestParam(name = "qauth", required = false) final String qauth,
    @RequestParam(name = "from", defaultValue = "1") final int from,
    @RequestParam(name = "to", defaultValue = "10") final int to,
    @RequestParam(name = "ml", defaultValue = "170") final int mainLibraryId,
    @RequestParam(name = "dpo", defaultValue = "1") final int databasePreferenceOrder,
    @RequestParam(name = "sortBy", required = false) final String[] sortAttributes,
    @RequestParam(name = "sortOrder", required = false) final String[] sortOrders) {
    SearchResponse authRecords = new SearchResponse(View.AUTHORITY, "", new int[0]);
    if (!("".equals(qauth) || qauth == null)) {
      authRecords = doGet((storageService, configuration) -> {
        final SearchEngine searchEngine =
          SearchEngineFactory.create(
            SearchEngineFactory.EngineType.LIGHTWEIGHT,
            mainLibraryId,
            databasePreferenceOrder,
            storageService);

        SearchResponse response = searchEngine.fetchRecords(
          searchEngine.expertSearch(qauth, locale(lang), View.AUTHORITY, from, to, sortAttributes, sortOrders),
          "F",
          1,
          ((to - from) + 1));

        searchEngine.injectDocCount(response, storageService);
        searchEngine.injectTagHighlight(response, storageService, locale(lang));
        return response;
      }, tenant, configurator);
    }
    SearchResponse bibRecords = doGet((storageService, configuration) -> {
      final SearchEngine searchEngine =
        SearchEngineFactory.create(
          SearchEngineFactory.EngineType.LIGHTWEIGHT,
          mainLibraryId,
          databasePreferenceOrder,
          storageService);

      SearchResponse response = searchEngine.fetchRecords(
        searchEngine.expertSearch(qbib, locale(lang), View.DEFAULT_BIBLIOGRAPHIC_VIEW, from, to, sortAttributes, sortOrders),
        "F",
        1,
        ((to - from) + 1));

      searchEngine.injectTagHighlight(response, storageService, locale(lang));
      return response;
    }, tenant, configurator);
    List<SearchResponse> mergedResult = new ArrayList<>();
    mergedResult.add(authRecords);
    mergedResult.add(bibRecords);
    return mergedResult;
  }

  @GetMapping("/searchVertical")
  public SearchResponse searchVertical(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestParam("q") final String q,
    @RequestParam(name = "from", defaultValue = "1") final int from,
    @RequestParam(name = "to", defaultValue = "10") final int to,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestParam("ml") final int mainLibraryId,
    @RequestParam(name = "dpo", defaultValue = "1") final int databasePreferenceOrder,
    @RequestParam(name = "sortBy", required = false) final String[] sortAttributes,
    @RequestParam(name = "sortOrder", required = false) final String[] sortOrders) {
    return doGet((storageService, configuration) -> {
      final SearchEngine searchEngine =
        SearchEngineFactory.create(
          SearchEngineFactory.EngineType.LIGHTWEIGHT_VERTICAL,
          mainLibraryId,
          databasePreferenceOrder,
          storageService);

      return searchEngine.fetchRecords(
        searchEngine.expertSearch(q, locale(lang), view, from, to, sortAttributes, sortOrders),
        "F",
        1,
        ((to - from) + 1));
    }, tenant, configurator);
  }

  @GetMapping("/countSearch")
  public ResponseEntity<Integer> countSearch(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
    @RequestParam("q") final String q,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestParam("ml") final int mainLibraryId,
    @RequestParam(name = "sortBy", required = false) final String[] sortAttributes,
    @RequestParam(name = "sortOrder", required = false) final String[] sortOrders) {
    return doGet((storageService, configuration) ->
      new ResponseEntity<>(storageService.getCountDocumentByQuery(q, sortAttributes, sortOrders, mainLibraryId, locale(lang), view), HttpStatus.OK), tenant, configurator);
  }
}
