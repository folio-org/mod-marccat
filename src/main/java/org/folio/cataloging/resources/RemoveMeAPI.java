package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.F;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.resources.domain.AcquisitionType;
import org.folio.cataloging.resources.domain.AcquisitionTypeCollection;
import org.folio.cataloging.search.ModCatalogingSearchEngine;
import org.folio.cataloging.search.SearchResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.F.locale;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Search Engine TEST RESTful APIs.
 *
 * @author agazzarini
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Sample SE API - TO BE REMOVED!")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class RemoveMeAPI extends BaseResource {

    @GetMapping("/search")
    public SearchResponse search(
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
            @RequestParam("q") final String q,
            @RequestParam(name = "from", defaultValue = "1") final int from,
            @RequestParam(name = "to", defaultValue = "10") final int to,
            @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
            @RequestParam("ml") final int mainLibraryId,
            @RequestParam(name = "dpo", required = false) final int databasePreferenceOrder,
            @RequestParam(name = "sortBy", required = false) final String[] sortAttributes,
            @RequestParam(name = "sortOrder", required = false) final String[] sortOrders) {
        return doGet((storageService, configuration) -> {
            final ModCatalogingSearchEngine searchEngine =
                    new ModCatalogingSearchEngine(mainLibraryId, databasePreferenceOrder, storageService);

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
