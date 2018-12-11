package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.config.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.common.View;
import org.folio.marccat.resources.domain.ResultLoader;
import org.folio.marccat.resources.domain.ResultLoaderCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.folio.marccat.integration.CatalogingHelper.doPost;

/**
 * Loads bibliographic records contents in a binary marc21 file.
 *
 * @author nbianchini
 * @since 1.0
 */

@RestController
@Api(value = "modcat-api", description = "Load from file API")
@RequestMapping(value = ModMarccat.BASE_URI) 
public class LoadFromFileAPI extends BaseResource {

  @ApiOperation(value = "Load bibliographic records from file.")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Method successfully loads the records."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @PostMapping("/load-from-file")
  public ResponseEntity <?> loadRecords(
    @RequestParam("files") MultipartFile uploadfiles,
    @RequestParam(name = "view", defaultValue = View.DEFAULT_BIBLIOGRAPHIC_VIEW_AS_STRING) final int view,
    @RequestParam(name = "startRecord", defaultValue = "0") final int startRecord,
    @RequestParam(name = "numberOfRecords", defaultValue = "50") final int numberOfRecords,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {


    return doPost((storageService, configuration) -> {

      try {
        final ResultLoaderCollection container = new ResultLoaderCollection();
        final List <MultipartFile> files = Arrays.asList(uploadfiles);
        container.setResultLoaders(
          files.stream().map(file -> {
            final Map <String, Object> map = storageService.loadRecords(file, startRecord, numberOfRecords, view, configuration);
            return setMapToResult(map);
          }).collect(Collectors.toList()));
        return container;
      } catch (Exception e) {
        return new ResponseEntity <>(HttpStatus.BAD_REQUEST);
      }

    }, tenant, configurator, () -> !uploadfiles.isEmpty(), "title", "name", "subject");
  }

  private ResultLoader setMapToResult(final Map <String, Object> source) {
    final ResultLoader resultLoader = new ResultLoader();
    resultLoader.setFilename((String) source.get(Global.LOADING_FILE_FILENAME));
    resultLoader.setAdded((int) source.get(Global.LOADING_FILE_ADDED));
    resultLoader.setRejected((int) source.get(Global.LOADING_FILE_REJECTED));
    resultLoader.setErrorCount((int) source.get(Global.LOADING_FILE_ERRORS));
    resultLoader.setIds((List <Integer>) source.get(Global.LOADING_FILE_IDS));
    return resultLoader;
  }
}
