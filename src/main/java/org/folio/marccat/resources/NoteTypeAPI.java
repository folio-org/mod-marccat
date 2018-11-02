package org.folio.marccat.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.marccat.Global;
import org.folio.marccat.ModMarccat;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.resources.domain.NoteType;
import org.folio.marccat.resources.domain.NoteTypeCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.marccat.integration.CatalogingHelper.doGet;

/**
 * Note Types RESTful APIs.
 *
 * @author aguercio
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Note type resource API")
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class NoteTypeAPI extends BaseResource {

  private Function <Avp <String>, NoteType> toNoteType = source -> {
    final NoteType noteType = new NoteType();
    noteType.setCode(Integer.parseInt(source.getValue()));
    noteType.setDescription(source.getLabel());
    return noteType;
  };

  @ApiOperation(value = "Returns all types associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested types."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/note-types")
  public NoteTypeCollection getNoteTypes(
    @RequestParam final String noteGroupType,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configurator) -> {
      final NoteTypeCollection container = new NoteTypeCollection();
      container.setNoteTypes(storageService.getNoteTypesByGroupTypeCode(noteGroupType, lang)
        .stream()
        .map(toNoteType)
        .collect(toList()));
      return container;
    }, tenant, configurator);
  }
}
