package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
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
        heading.setIndexingLanguage(source.getIndexingLanguage());
        heading.setAccessPointlanguage(source.getAccessPointlanguage());
        heading.setVerificationlevel(source.getVerificationlevel());
        return heading;
    };

    @ApiOperation(value = "Returns all headings associated with a given language")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Method successfully returned the requested heading types."),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 414, message = "Request-URI Too Long"),
            @ApiResponse(code = 500, message = "System internal failure occurred.")
    })

    @PostMapping("/create-heading")
    public ResponseEntity<Heading> createHeading(
      @RequestBody final Heading heading,
      @RequestParam final String lang,
      @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
      return doPost((storageService, configuration) -> {
          storageService.saveHeading(heading);
        return heading;
      }, tenant, configurator, () -> isNotNullOrEmpty(heading.getStringText()));
    }



}
