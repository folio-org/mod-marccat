package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.resources.domain.Heading;
import org.folio.cataloging.resources.domain.HeadingCollection;
import org.folio.cataloging.resources.domain.RecordTemplate;
import org.folio.cataloging.shared.MapHeading;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.F.isNotNullOrEmpty;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;
import static org.folio.cataloging.integration.CatalogingHelper.doPost;

/**
 * Headings RESTful APIs.
 *
 * @author carment
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Heading resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class HeadingAPI extends BaseResource {

  private Function<MapHeading, Heading> toHeading = source -> {
    final Heading heading = new Heading();
    heading.setStringText(source.getStringText());
    return heading;
  };

  @ApiOperation(value = "Load bibliographic records from file.")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Method successfully loads the records."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })

  @PostMapping("/create-heading")
  public ResponseEntity<Heading> createHeading(
    @RequestBody final Heading heading,
    @RequestParam final String lang,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doPost((storageService, configuration) -> {
      storageService.saveHeading(heading, view, configuration);
      return heading;
    }, tenant, configurator, () -> (isNotNullOrEmpty(heading.getStringText())), "title","subject","name");
  }



}
