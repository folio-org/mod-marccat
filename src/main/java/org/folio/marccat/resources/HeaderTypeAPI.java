package org.folio.marccat.resources;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.config.constants.Global.BASE_URI;
import static org.folio.marccat.integration.MarccatHelper.doGet;
import static org.folio.marccat.resources.shared.MappingUtils.mapToHeading;

import java.util.ArrayList;
import java.util.List;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.resources.domain.HeadingType;
import org.folio.marccat.resources.domain.HeadingTypeCollection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = BASE_URI, produces = "application/json")
public class HeaderTypeAPI extends BaseResource {

  @GetMapping("/header-types")
  public HeadingTypeCollection getHeadingTypes(@RequestParam final String code, @RequestParam final String lang,
      @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {

      final int category = Global.HEADER_CATEGORY;
      if (ofNullable(storageService.getFirstCorrelation(lang, category)).isPresent())
        return mapToHeading(storageService.getFirstCorrelation(lang, category), code);

      return null;
    }, tenant, okapiUrl, configurator);
  }

  @GetMapping("/auth-header-types")
  public HeadingTypeCollection getAuthorityHeadingTypes(@RequestParam final String code,
      @RequestParam final String lang, @RequestHeader(Global.OKAPI_TENANT_HEADER_NAME) final String tenant,
      @RequestHeader(Global.OKAPI_URL) String okapiUrl) {
    return doGet((storageService, configuration) -> {

      HeadingTypeCollection headingTypeCollection = new HeadingTypeCollection();
      List<HeadingType> headingTypes = new ArrayList<>();
      HeadingType headingType = new HeadingType();
      headingType.setCode(Global.AUT_MATERIAL_DESCRIPTION_HEADER_TYPE);
      headingType.setDescription(Global.AUT_MATERIAL_DESCRIPTION_DESC);
      headingTypes.add(headingType);
      headingTypeCollection.setHeadingTypes(headingTypes);
      return headingTypeCollection;
    }, tenant, okapiUrl, configurator);
  }
}
