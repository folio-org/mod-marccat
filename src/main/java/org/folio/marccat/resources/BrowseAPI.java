package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.resources.domain.HeadingDecorator;
import org.folio.cataloging.resources.domain.HeadingDecoratorCollection;
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
@Api(value = "modcat-api", description = "Browse resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class BrowseAPI extends BaseResource {

  private Function <MapHeading, HeadingDecorator> toHeading = source -> {
    final HeadingDecorator heading = new HeadingDecorator();
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
    @ApiResponse(code = 200, message = "Method successfully returned the requested headings."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/first-page")
  public HeadingDecoratorCollection getFirstPage(
    @RequestParam final String query,
    @RequestParam final int view,
    @RequestParam final int mainLibrary,
    @RequestParam final int pageSize,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final HeadingDecoratorCollection container = new HeadingDecoratorCollection();
      container.setHeadings(
        storageService.getFirstPage(query, view, mainLibrary, pageSize, lang)
          .stream()
          .map(toHeading)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }

  @ApiOperation(value = "Returns all headings associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested headings."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/next-page")
  public HeadingDecoratorCollection getNextPage(
    @RequestParam final String query,
    @RequestParam final int view,
    @RequestParam final int mainLibrary,
    @RequestParam final int pageSize,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      List <MapHeading> headings = storageService.getNextPage(query, view, mainLibrary, pageSize, lang);
      final HeadingDecoratorCollection headingCollection = new HeadingDecoratorCollection();
      headingCollection.setHeadings(headings
        .stream()
        .map(toHeading)
        .collect(toList()));
      return headingCollection;
    }, tenant, configurator);
  }

  @ApiOperation(value = "Returns all headings associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested headings."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/previous-page")
  public HeadingDecoratorCollection getPreviousPage(
    @RequestParam final String query,
    @RequestParam final int view,
    @RequestParam final int mainLibrary,
    @RequestParam final int pageSize,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final HeadingDecoratorCollection container = new HeadingDecoratorCollection();
      container.setHeadings(
        storageService.getPreviousPage(query, view, mainLibrary, pageSize, lang)
          .stream()
          .map(toHeading)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }


  @ApiOperation(value = "Returns all headings associated with a tag")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested headings."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/headings-by-tag")
  public HeadingDecoratorCollection getHeadingsByTag(
    @RequestParam final String tag,
    @RequestParam final String indicator1,
    @RequestParam final String indicator2,
    @RequestParam final String stringText,
    @RequestParam final int view,
    @RequestParam final int mainLibrary,
    @RequestParam final int pageSize,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      List <MapHeading> headings = storageService.getHeadingsByTag(tag, indicator1, indicator2, stringText, view, mainLibrary, pageSize, lang);
      final HeadingDecoratorCollection headingCollection = new HeadingDecoratorCollection();
      headingCollection.setHeadings(headings
        .stream()
        .map(toHeading)
        .collect(toList()));
      return headingCollection;
    }, tenant, configurator);
  }

}
