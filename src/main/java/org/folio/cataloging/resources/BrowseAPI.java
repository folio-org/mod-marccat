package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.resources.domain.Heading;
import org.folio.cataloging.resources.domain.HeadingCollection;
import org.folio.cataloging.shared.MapHeading;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

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

    private Function<MapHeading, Heading> toHeading = source -> {
        final Heading heading = new Heading();
        heading.setHeadingNumber(source.getHeadingNumber());
        heading.setStringText(source.getStringText());
        heading.setCountAuthorities(source.getCountAuthorities());
        heading.setCountDocuments(source.getCountDocuments());
        heading.setCountCrossReferences(source.getCountCrossReferences());
        heading.setCountTitleNameDocuments(source.getCountTitleNameDocuments());
        heading.setIndexingLanguage(source.getIndexingLanguage());
        heading.setAccessPointlanguage(source.getAccessPointlanguage());
        heading.setVerificationlevel(source.getVerificationlevel());
        heading.setDatabase(source.getDatabase());
        return heading;
    };

    @ApiOperation(value = "Returns all headings associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested heading types."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })


    @GetMapping("/first-page")
    public HeadingCollection getFirstPage(
            @RequestParam final String query,
            @RequestParam final int view,
            @RequestParam final int mainLibrary,
            @RequestParam final int pageSize,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
         return doGet((storageService, configuration) -> {
            final HeadingCollection container = new HeadingCollection();
            container.setHeadings(
                    storageService.getFirstPage(query, view, mainLibrary, pageSize, lang)
                            .stream()
                            .map(toHeading)
                            .collect(toList()));
            return container;
        }, tenant, configurator);
    }

    @GetMapping("/next-page")
    public HeadingCollection getNextPage(
            @RequestParam final String query,
            @RequestParam final int view,
            @RequestParam final int mainLibrary,
            @RequestParam final int pageSize,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
            List<MapHeading> headings =  storageService.getNextPage(query, view, mainLibrary, pageSize, lang);
            final HeadingCollection headingCollection = new HeadingCollection();
            headingCollection.setHeadings(headings
                          .stream()
                          .map(toHeading)
                          .collect(toList()));
            return headingCollection;
        }, tenant, configurator);
    }


    @GetMapping("/previous-page")
    public HeadingCollection getPreviousPage(
            @RequestParam final String query,
            @RequestParam final int view,
            @RequestParam final int mainLibrary,
            @RequestParam final int pageSize,
            @RequestParam final String lang,
            @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
        return doGet((storageService, configuration) -> {
            final HeadingCollection container = new HeadingCollection();
            container.setHeadings(
                    storageService.getPreviousPage(query, view, mainLibrary, pageSize, lang)
                            .stream()
                            .map(toHeading)
                            .collect(toList()));
            return container;
        }, tenant, configurator);
    }

}
