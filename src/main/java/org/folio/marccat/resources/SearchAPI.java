package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import org.folio.marccat.config.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.common.View;
import org.folio.marccat.search.SearchEngineFactory;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.engine.SearchEngine;
import org.springframework.web.bind.annotation.*;

import static org.folio.marccat.util.F.locale;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Search Engine TEST RESTful APIs.
 *
 * @author cchiama
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "MARCCat Search API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class SearchAPI extends BaseResource {

  @GetMapping("/search")
  public SearchResponse search(
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
          SearchEngineFactory.EngineType.LIGHTWEIGHT,
          mainLibraryId,
          databasePreferenceOrder,
          storageService);
      SearchResponse response = searchEngine.fetchRecords(
        (sortAttributes != null && sortOrders != null && sortAttributes.length == sortOrders.length)
          ? searchEngine.sort(searchEngine.expertSearch(q, locale(lang), view), sortAttributes, sortOrders)
          : searchEngine.expertSearch(q, locale(lang), view),
        "F",
        from,
        to);
      final int AUTHORITY_VIEW = -1;
      if (view == AUTHORITY_VIEW) {
        searchEngine.injectDocCount(response, storageService);
      }
      return response;
    }, tenant, configurator);
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
        (sortAttributes != null && sortOrders != null && sortAttributes.length == sortOrders.length)
          ? searchEngine.sort(searchEngine.expertSearch(q, locale(lang), view), sortAttributes, sortOrders)
          : searchEngine.expertSearch(q, locale(lang), view),
        "F",
        from,
        to);
    }, tenant, configurator);
  }

}
