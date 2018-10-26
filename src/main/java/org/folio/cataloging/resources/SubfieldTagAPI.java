package org.folio.cataloging.resources;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.resources.domain.SubfieldsTag;
import org.folio.cataloging.shared.Validation;
import org.springframework.web.bind.annotation.*;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Subfield codes for Tag entity RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Subfield resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class SubfieldTagAPI extends BaseResource {

  @ApiOperation(value = "Returns the subfield tag associated with the input data.")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested tag."),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/subfield-tag")
  public SubfieldsTag getSubfieldsTag(
    @RequestParam final String marcCategory,
    @RequestParam final String code1,
    @RequestParam final String code2,
    @RequestParam final String code3,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {
      final int category = Integer.parseInt(marcCategory);

      final Validation validation = storageService.getSubfieldsByCorrelations(
        category,
        Integer.parseInt(code1),
        Integer.parseInt(code2),
        Integer.parseInt(code3));

      final SubfieldsTag subfieldsTag = new SubfieldsTag();
      subfieldsTag.setCategory(category);
      subfieldsTag.setDefaultSubfield(String.valueOf(validation.getMarcTagDefaultSubfieldCode()));
      subfieldsTag.setSubfields(stream(validation.getMarcValidSubfieldStringCode().split("")).collect(toList()));
      subfieldsTag.setRepeatable(stream(validation.getRepeatableSubfieldStringCode().split("")).collect(toList()));
      subfieldsTag.setTag(validation.getKey().getMarcTag());
      return subfieldsTag;
    }, tenant, configurator);
  }
}
