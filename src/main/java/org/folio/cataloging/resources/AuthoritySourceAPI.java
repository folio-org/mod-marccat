package org.folio.cataloging.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.folio.cataloging.Global;
import org.folio.cataloging.ModCataloging;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.resources.domain.AuthoritySource;
import org.folio.cataloging.resources.domain.AuthoritySourceCollection;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.folio.cataloging.integration.CatalogingHelper.doGet;

/**
 * Authority Sources RESTful APIs.
 *
 * @author natasciab
 * @since 1.0
 */
@RestController
@Api(value = "modcat-api", description = "Authority source resource API")
@RequestMapping(value = ModCataloging.BASE_URI, produces = "application/json")
public class AuthoritySourceAPI extends BaseResource {

  private Function <Avp <String>, AuthoritySource> toAuthoritySource = source -> {
    final AuthoritySource authoritySource = new AuthoritySource ( );
    authoritySource.setCode (Integer.parseInt (source.getValue ( )));
    authoritySource.setDescription (source.getLabel ( ));
    return authoritySource;
  };

  @ApiOperation(value = "Returns all authority sources associated with a given language")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Method successfully returned the requested authority sources"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 414, message = "Request-URI Too Long"),
    @ApiResponse(code = 500, message = "System internal failure occurred.")
  })
  @GetMapping("/authority-sources")
  public AuthoritySourceCollection getAuthoritySources(
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet ((storageService, configuration) -> {
      final AuthoritySourceCollection container = new AuthoritySourceCollection ( );
      container.setAuthoritySources (
        storageService.getAuthoritySources (lang)
          .stream ( )
          .map (toAuthoritySource)
          .collect (toList ( )));
      return container;
    }, tenant, configurator);

  }
}
