package org.folio.marccat.resources;

import org.folio.marccat.ModMarccat;
import org.folio.marccat.config.Global;
import org.folio.marccat.resources.domain.HeadingTypeCollection;
import org.springframework.web.bind.annotation.*;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.resources.shared.MappingUtils.mapToHeading;

/**
 * Header type code RESTful APIs.
 *
 * @author cchiama
 * @since 1.0
 */
@RestController
@RequestMapping(value = ModMarccat.BASE_URI, produces = "application/json")
public class HeaderTypeAPI extends BaseResource {


  @GetMapping("/header-types")
  public HeadingTypeCollection getHeadingTypes(
    @RequestParam final String code,
    @RequestParam final String lang,
    @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant) {
    return doGet((storageService, configuration) -> {

      final int category = Global.HEADER_CATEGORY;
      if (ofNullable(storageService.getFirstCorrelation(lang, category)).isPresent())
        return mapToHeading(storageService.getFirstCorrelation(lang, category), code);

      return null;
    }, tenant, configurator);
  }
}
