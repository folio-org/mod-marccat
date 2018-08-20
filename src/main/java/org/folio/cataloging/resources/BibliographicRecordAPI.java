package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.resources.domain.BibliographicRecord;
import org.folio.cataloging.search.SearchEngineFactory;
import org.folio.cataloging.search.SearchEngineType;
import org.folio.cataloging.search.SearchResponse;
import org.folio.cataloging.search.engine.SearchEngine;
import org.springframework.web.bind.annotation.*;

import static org.folio.cataloging.F.locale;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Bibliographic records API.
 *
 * @author nbianchini
 * @since 1.0
 */

@RestController
@Api(value = "modcat-api", description = "Get bibliographic record API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class BibliographicRecordAPI extends BaseResource {

    @ApiOperation(value = "Returns the bibliographic record associated with a given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested bibliographic record"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })
    @GetMapping("/bibliographic-record/{id}")
    public BibliographicRecord getRecord(@RequestParam final Integer id,
                                         @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
                                         @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {

        return doGet((storageService, configuration) -> {
            BibliographicRecord bibliographicRecord = new BibliographicRecord();
            return storageService.getBibliographicRecordById(bibliographicRecord, id, view);
        }, tenant, configurator);

    }

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
                            SearchEngineType.LIGHTWEIGHT,
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
