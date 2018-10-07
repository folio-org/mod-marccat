package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.resources.domain.ResultLoader;
import org.folio.cataloging.resources.domain.ResultLoaderCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.folio.cataloging.integration.CatalogingHelper.doPost;

/**
 * Loads bibliographic records contents in a binary marc21 file.
 *
 * @author nbianchini
 * @since 1.0
 */

@RestController
@Api(value = "modcat-api", description = "Load from file API")
@RequestMapping(value = ModCataloging.BASE_URI) //, produces = "multipart/form-data"
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
    //@RequestHeader(name = Global.CONTENT_TYPE_HEADER_NAME, defaultValue = Global.DEFAULT_MULTIPART_HEADER_CONTENT) final String contentType) {


    return doPost ((storageService, configuration) -> {

      /*String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
        .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

      if (StringUtils.isEmpty(uploadedFileName)) {
        return new ResponseEntity("Please select a file!", HttpStatus.OK); //MessageCatalog
      }*/

      try {
        final ResultLoaderCollection container = new ResultLoaderCollection ( );
        final List <MultipartFile> files = Arrays.asList (uploadfiles);
        container.setResultLoaders (
          files.stream ( ).map (file -> {
            final Map <String, Object> map = storageService.loadRecords (file, startRecord, numberOfRecords, view, configuration);
            return setMapToResult (map);
          }).collect (Collectors.toList ( )));
        return container;
      } catch (Exception e) {
        return new ResponseEntity <> (HttpStatus.BAD_REQUEST);
      }

    }, tenant, configurator, () -> !uploadfiles.isEmpty ( ), "title", "name", "subject");
  }

  private ResultLoader setMapToResult(final Map <String, Object> source) {
    final ResultLoader resultLoader = new ResultLoader ( );
    resultLoader.setFilename ((String) source.get (Global.LOADING_FILE_FILENAME));
    resultLoader.setAdded ((int) source.get (Global.LOADING_FILE_ADDED));
    resultLoader.setRejected ((int) source.get (Global.LOADING_FILE_REJECTED));
    resultLoader.setErrorCount ((int) source.get (Global.LOADING_FILE_ERRORS));
    resultLoader.setIds ((List <Integer>) source.get (Global.LOADING_FILE_IDS));
    return resultLoader;
  }
}
