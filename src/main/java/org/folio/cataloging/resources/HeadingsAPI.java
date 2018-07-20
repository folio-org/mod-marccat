package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;
import static org.folio.cataloging.resources.domain.CatalogingEntityType.A;

/**
 * Headings RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Heading resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class HeadingsAPI extends BaseResource {

    @ApiOperation(value = "Returns all headings associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested heading types."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })


    @GetMapping("/first-headings")
    public HeadingCollection getFirstHeadings(
            @RequestParam final String searchBrowseTerm,
            @RequestParam final int cataloguingView,
            @RequestParam final int mainLibrary,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
            final List<Heading> headings =  storageService.getFirstPage(searchBrowseTerm, cataloguingView, mainLibrary, lang);
            final HeadingCollection container = new HeadingCollection();
            container.setHeadings(headings);
            return container;
        }, tenant, configurator);
    }

    @GetMapping("/next-headings")
    public HeadingCollection getNextHeadings(
            @RequestParam final String searchBrowseTerm,
            @RequestParam final int cataloguingView,
            @RequestParam final int mainLibrary,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
            List<Heading> headings =  storageService.getNextHeadings(searchBrowseTerm, cataloguingView, mainLibrary, lang);
            final HeadingCollection headingCollection = new HeadingCollection();
            headingCollection.setHeadings(headings);
            return headingCollection;
        }, tenant, configurator);
    }


    @GetMapping("/previous-headings")
    public HeadingCollection getPreviousHeadings(
            @RequestParam final String searchBrowseTerm,
            @RequestParam final int cataloguingView,
            @RequestParam final int mainLibrary,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
            final List<Heading> headings =  storageService.getPreviousHeadings(searchBrowseTerm, cataloguingView, mainLibrary, lang);
            final HeadingCollection headingCollection = new HeadingCollection();
            headingCollection.setHeadings(headings);
            return headingCollection;
        }, tenant, configurator);
    }

}