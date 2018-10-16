package org.folio.cataloging.resources;

import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Data loader API.
 *
 * @author Matteo Pascucci
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Data Loader API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class DataLoaderAPI extends BaseResource {

  @ApiOperation(value = "Returns Data Loader")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested data loader."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/notification")
  public void getDataLoader(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }

  @ApiOperation(value = "Creates a new Data Loader.")
  @ApiResponses(value = {
    @ApiResponse(code = 201, message = "Method successfully created the new Data Loader."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @PostMapping("/notification")
  public void createNewDataLoader(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }

  @ApiOperation(value = "Updates an existing data loader.")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Method successfully updated the data loader."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/notification")
  public void updateDataLoader(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }

  @ApiOperation(value = "Deletes a data loader.")
  @ApiResponses(value = {
    @ApiResponse(code = 204, message = "Method successfully deleted the target data loader."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/notification")
  public void deleteDataLoader(
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
  }
}
