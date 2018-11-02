package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.RecordDisplayFormat;
import org.folio.marccat.resources.domain.RecordDisplayFormatCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Record Display Format RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Record display format resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class RecordDisplayFormatAPI extends BaseResource {

  private Function <Avp <String>, RecordDisplayFormat> toRecordDisplayFormat = source -> {
    final RecordDisplayFormat recordDisplayFormat = new RecordDisplayFormat();
    recordDisplayFormat.setCode(Integer.parseInt(source.getValue()));
    recordDisplayFormat.setDescription(source.getLabel());
    return recordDisplayFormat;
  };

  @ApiOperation(value = "Returns all formats associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested formats."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/record-display-formats")
  public RecordDisplayFormatCollection getRecordDisplayFormats(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final RecordDisplayFormatCollection container = new RecordDisplayFormatCollection();
      container.setRecordDisplayFormats(
        storageService.getRecordDisplayFormats(lang)
          .stream()
          .map(toRecordDisplayFormat)
          .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
